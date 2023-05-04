package com.allback.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class KafkaRequestFilter implements GatewayFilter {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    public KafkaRequestFilter(KafkaTemplate<String, String> kafkaTemplate, String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 요청을 Kafka로 전송하는 코드 작성
        String requestPayload = "{ \"path\": \"" + exchange.getRequest().getPath().value() + "\" }";
        kafkaTemplate.send(topicName, requestPayload);
        return chain.filter(exchange);
    }
}
