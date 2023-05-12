package com.allback.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaRequestFilter extends AbstractGatewayFilterFactory<KafkaRequestFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaRequestFilter.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "concert-req";
    private PriorityQueue<Long> priorityQueue = new PriorityQueue<>();
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public KafkaRequestFilter(KafkaTemplate<String, String> kafkaTemplate) {
        super(Config.class);
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Client에게 보낼 대기표 Response 형식
     */
    @Data
    @AllArgsConstructor
    static class JsonResponse {
        private String uuid;
        private Integer partition;
        private Long offset;
        private Long committedOffset;
        private Long endOffset;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            // 대기열을 거치지 않는 요청은 header에 'KAFKA.PASS'라는 key가 존재한다.
            if (request.getHeaders().containsKey("KAFKA.PASS")) {
                return chain.filter(exchange);
            }

            String uuid = null;
            long offset;
            int partition;


            // 대기표를 가지고 있지 않다면 -> 최초 요청 -> kafka에 넣기
            if (!request.getHeaders().containsKey("KAFKA.UUID")) {
                uuid = UUID.randomUUID().toString();

                // TODO : 변경해야 됨
//                CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(config.topicName, uuid);
                CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(config.topicName, 3, null, uuid);

                try {
                    SendResult<String, String> sendResult = send.get();

                    // kafka에 넣은 메시지(대기표)가 몇 번째 partition의 몇 번째 offset에 들어갔는지
                    offset = sendResult.getRecordMetadata().offset();
                    partition = sendResult.getRecordMetadata().partition();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                HttpHeaders headers = new HttpHeaders();

                headers.add("KAFKA.UUID", uuid);
                headers.add("KAFKA.PARTITION", Integer.toString(partition));
//                headers.add("KAFKA.OFFSET", Long.toString(offset));

                // 변경된 header로 request를 갱신
                ServerHttpRequest request2 = exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.addAll(headers)).build();
                exchange = exchange.mutate().request(request2).build();

            }

            // 대기표를 가지고 있다면
            else {
                uuid = request.getHeaders().get("KAFKA.UUID").get(0);
                partition = Integer.parseInt(request.getHeaders().get("KAFKA.PARTITION").get(0));
                offset = Long.parseLong(request.getHeaders().get("KAFKA.OFFSET").get(0));
            }

            // Consumer가 마지막으로 읽은 레코드의 Offset 값 알아내기
            long committedOffset = getCommittedOffset(partition);
            long endOffset = getEndOffset(partition);
            logger.info("[header] " + request.getHeaders().get("KAFKA.OFFSET"));
            logger.info("[committedOffset] " + committedOffset);
            logger.info("[endOffset] " + endOffset);
            logger.info("[offset] " + offset);

            // 대기 취소라면
            if (request.getHeaders().containsKey("KAFKA.QUIT")) {

                priorityQueue.add(offset);

                // 응답 만들기
                ServerHttpResponse response = exchange.getResponse();

                // 200 상태 코드로 응답 설정
                response.setStatusCode(HttpStatus.OK);

                // 응답 본문에 메시지 설정
                String message = "waiting is successfully canceled";
                DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
                return response.writeWith(Mono.just(buffer))
                        .flatMap(Void -> Mono.error(new ResponseStatusException(HttpStatus.OK, message)));
            }

            // 대기열이 있다면 -> 토큰 반환하고 끝내기
            // committedOffset >= offset이어야 됨
            while (priorityQueue.size() > 0 && offset > priorityQueue.peek()) {
                priorityQueue.poll();
                committedOffset++;
            }

            if (committedOffset < offset) {
                // 클라이언트에게 보낼 응답 생성
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                response.getHeaders().add("Content-Type", "application/json");

                JsonResponse jsonResponse = new JsonResponse(uuid, partition, offset, committedOffset, endOffset);

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

            // 대기열이 없다면 -> 요청 처리하기
            else {
                long newOffset = offset;
                while (priorityQueue.size() > 0 && newOffset <= endOffset && newOffset + 1 == priorityQueue.peek()) {
                    System.out.println(offset + 1 + " record delete");
                    newOffset++;
                    priorityQueue.poll();
                }

                if (newOffset != offset) {
                    HttpHeaders headers3 = new HttpHeaders();
                    headers3.add("KAFKA.OFFSET", Long.toString(offset));

                    // 변경된 header로 request를 갱신
                    ServerHttpRequest request3 = exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.addAll(headers3)).build();
                    exchange = exchange.mutate().request(request3).build();
                }


                // TODO : 각 Spring 서버를 Kafka의 Consumer로 설정해놓고, 요청 하나 처리할 때마다 메시지 commit 해야됨
                //  메시지 uuid 값을 같이 넘겨줘서, 같은 메시지가 commit 되어야함
                //  파티션 번호!


                return chain.filter(exchange);
            }
        };
    }

    /**
     * 특정 파티션에서, Consumer가 제일 최근에 commit한 메시지의 offset 값 알아내기
     *
     * @param partition
     * @return
     */
    private long getCommittedOffset(int partition) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        KafkaConsumer<String, String> consumer = createConsumer("concert-req");
        consumer.assign(Collections.singletonList(topicPartition));
        return consumer.position(topicPartition);
    }

    private long getEndOffset(int partition) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        KafkaConsumer<String, String> consumer = createConsumer("concert-req");
        consumer.assign(Collections.singletonList(topicPartition));
        consumer.seekToEnd(Collections.singletonList(topicPartition));
        return consumer.position(topicPartition);
    }

    @Data
    public static class Config {
        private String topicName;
    }

    /**
     * Kafka Consumer 생성 메서드
     *
     * @param consumerGroupId
     * @return KafkaConsumer
     */
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
