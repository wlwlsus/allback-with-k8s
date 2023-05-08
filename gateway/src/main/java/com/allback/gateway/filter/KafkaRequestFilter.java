package com.allback.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaRequestFilter extends AbstractGatewayFilterFactory<KafkaRequestFilter.Config> {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "concert-req";
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public KafkaRequestFilter(KafkaTemplate<String, String> kafkaTemplate) {
        super(Config.class);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Data
    @AllArgsConstructor
    static class JsonResponse {
        private String uuid;
        private String time;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String uuid = null;
            long offset;
            int partition;

            // 대기표를 가지고 있지 않다면 -> 최초 요청 -> kafka에 넣기
            if (!request.getHeaders().containsKey("QUEUE")) {
                uuid = UUID.randomUUID().toString();
                CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(config.topicName, uuid);
                try {
                    SendResult<String, String> sendResult = send.get();

                    // kafka에 넣은 메시지(대기표)가 몇 번째 partition의 몇 번째 offset에 들어갔는지
                    offset = sendResult.getRecordMetadata().offset();
                    partition = sendResult.getRecordMetadata().partition();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // 대기표를 가지고 있다면
            else {
                uuid = request.getHeaders().get("uuid").get(0);
                partition = Integer.parseInt(request.getHeaders().get("partition").get(1));
                offset = Long.parseLong(request.getHeaders().get("offset").get(2));
            }

            // 대기열이 있다면 -> 토큰 반환하고 끝내기
            // TODO : 대기열이 존재하는지 판단하는 로직 필요
            //  Consumer가 제일 최근에 commit한 메시지의 offset < 나의 offset

            TopicPartition topicPartition = new TopicPartition(topic, partition);
            KafkaConsumer<String, String> consumer = createConsumer("concert-req");
            consumer.assign(Collections.singletonList(topicPartition));
            consumer.poll(Duration.ZERO); // Trigger partition assignment

            long committedOffset =  consumer.committed(topicPartition).offset();

            if (false){
                // 클라이언트에게 보낼 응답 생성
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                response.getHeaders().add("Content-Type", "application/json");

                JsonResponse jsonResponse = new JsonResponse(uuid, Long.toString(committedOffset));

                // JSON 문자열로 변환
                ObjectMapper objectMapper = new ObjectMapper();

                String responseBody;
                try {
                    responseBody = objectMapper.writeValueAsString(jsonResponse);
                } catch (JsonProcessingException e) {
                    // JSON 변환 실패 시 에러 응답 전송
                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    responseBody = "Error: " + e.getMessage();
                }


                byte[] responseBytes = responseBody.getBytes();

                // 응답을 클라이언트에게 전송하고 filter 체인 종료
                return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));
            }

            // TODO : 각 Spring 서버를 Kafka의 Consumer로 설정해놓고, 요청 하나 처리할 때마다 메시지 commit 해야됨
            //  메시지 uuid 값을 같이 넘겨줘서, 같은 메시지가 commit 되어야함

            // 대기열이 없다면 -> 요청 처리하기
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private String topicName;
    }

//    @KafkaListener(topics = "concert-req", groupId = "concert-req")
//    public void consumeMessage(ConsumerRecord<String, String> record) {
//        String key = record.key();
//        String value = record.value();
//        long offset = record.offset();
//        int partition = record.partition();
//
//        System.out.printf("Received record: key=%s, value=%s, partition=%d, offset=%d%n", key, value, partition, offset);
//    }

    private KafkaConsumer<String, String> createConsumer(String consumerGroupId) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(properties);
    }
}
