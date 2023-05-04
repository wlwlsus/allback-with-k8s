package com.allback.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class KafkaRequestFilterFactory extends AbstractGatewayFilterFactory<KafkaRequestFilterFactory.Config> {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaRequestFilterFactory(KafkaTemplate<String, String> kafkaTemplate) {
        super(Config.class);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new KafkaRequestFilter(kafkaTemplate, config.getTopicName());
    }

    public static class Config {
        private String topicName;

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }
    }
}
