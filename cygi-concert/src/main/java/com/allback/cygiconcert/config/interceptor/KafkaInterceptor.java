package com.allback.cygiconcert.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.*;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;


@Component
public class KafkaInterceptor implements HandlerInterceptor {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @Value("${spring.kafka.consumer.partition}")
    private int partition;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Kafka Consumer 생성 메서드
     *
     * @param consumerGroupId
     * @return Kafka Consumer
     */
    private KafkaConsumer<String, String> createConsumer(String consumerGroupId) {
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(GROUP_ID_CONFIG, consumerGroupId);
        properties.setProperty(MAX_POLL_RECORDS_CONFIG, "1");   // consumer는 한 번에 1개만 poll 할 수 있다.
        properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(CLIENT_ID_CONFIG, "myClientId");  // TODO : 파티션 번호로 지정하기
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(properties);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 대기열 시스템이 필요없는 애들은 넘기기
        if (request.getHeader("KAFKA.UUID") == null) {
            return ;
        }

        // 10초 대기 (일부러 성능 떨어뜨리기)
        Thread.sleep(3000);

        // kafka consumer 생성
        KafkaConsumer<String, String> consumer = createConsumer(groupId);

        // consume할 topic과 particion
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        consumer.assign(Collections.singletonList(topicPartition));

        // 레코드 읽어오기
//        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        Enumeration<String> headers = request.getHeaders("KAFKA.OFFSET");
        long lastValue = -1;
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value != null) {
                lastValue = Long.parseLong(value);
            }
        }

        long offset = lastValue;
        long committedOffset = offset + 1;

        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

//        currentOffsets.put(partition, new OffsetAndMetadata(consumer.position(partition)));
        currentOffsets.put(topicPartition, new OffsetAndMetadata(committedOffset));

        // 읽은 메시지 commit 하기 (Offset 증가)
        consumer.commitSync(currentOffsets);

        System.out.println("committedOffset :::: " + committedOffset);
    }

}
