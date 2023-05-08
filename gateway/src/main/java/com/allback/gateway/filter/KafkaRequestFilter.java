package com.allback.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.kafka.clients.consumer.*;
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
        private Integer partition;
        private Long offset;
        private Long committedOffset;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String uuid = null;
            long offset;
            int partition;

            // 대기표를 가지고 있지 않다면 -> 최초 요청 -> kafka에 넣기
            if (!request.getHeaders().containsKey("KAFKA.UUID")) {
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
                uuid = request.getHeaders().get("KAFKA.UUID").get(0);
                partition = Integer.parseInt(request.getHeaders().get("KAFKA.PARTITION").get(1));
                offset = Long.parseLong(request.getHeaders().get("KAFKA.OFFSET").get(2));
            }


            TopicPartition topicPartition = new TopicPartition(topic, partition);
            KafkaConsumer<String, String> consumer = createConsumer("concert-req");
            consumer.assign(Collections.singletonList(topicPartition));
            ConsumerRecords<String, String> record = consumer.poll(Duration.ZERO);// Trigger partition assignment
            Optional<OffsetAndMetadata> committed = Optional.ofNullable(consumer.committed(topicPartition));

            long committedOffset = 0;

//            ConsumerRecords<String, String> records = consumer.poll(Duration.ZERO);// Trigger partition assignment
//            Optional<OffsetAndMetadata> committed = Optional.ofNullable(consumer.committed(topicPartition));
//
//            long committedOffset = consumer.position(topicPartition);

            if (committed.isPresent()) {
                consumer.committed(topicPartition).offset();
            }

            // 대기열이 있다면 -> 토큰 반환하고 끝내기
            if (committedOffset < offset) {
//            if (false) {
                // 클라이언트에게 보낼 응답 생성
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                response.getHeaders().add("Content-Type", "application/json");

                JsonResponse jsonResponse = new JsonResponse(uuid, partition, offset, committedOffset);

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
            //  파티션 번호!

            // 대기열이 없다면 -> 요청 처리하기
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private String topicName;
    }

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
