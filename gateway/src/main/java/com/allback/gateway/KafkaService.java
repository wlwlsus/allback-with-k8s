package com.allback.gateway;

import com.allback.gateway.filter.KafkaRequestFilter;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConsumer<String, String> kafkaConsumer;

    private PriorityQueue<Long> priorityQueue = new PriorityQueue<>();  // 대기 취소 요청들의 offset 값들

    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.partition}")
    private int partition;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConsumer = kafkaConsumer;
    }

    // kafka consumer가 제일 마지막에 소비한 레코드의 offset 값 반환
    public long getCommittedOffset() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "3.34.8.99:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, topic);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "myClientId");  // TODO : 파티션 번호로 지정하기
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<Object, Object> newKafkaConsumer = new KafkaConsumer<>(properties);


        TopicPartition topicPartition = new TopicPartition(topic, partition);
        newKafkaConsumer.assign(Collections.singletonList(topicPartition));
        return newKafkaConsumer.position(topicPartition);
    }

    // kafka producer가 제일 마지막에 발행한 레코드의 offset 값 반환
    public long getEndOffset() {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
//        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seekToEnd(Collections.singletonList(topicPartition));
        return kafkaConsumer.position(topicPartition);
    }

    // 대기표 추가 (kafka 메시지 발행)
    // TODO : partition은 gateway 서버가 실행될 때 외부매개변수로 직접 넣어줘야할 듯
    public CompletableFuture<SendResult<String, String>> send(String data) {
        return kafkaTemplate.send(topic, partition, null, data);
    }

    // 대기표 취소
    public void cancel(long offset) {
        priorityQueue.add(offset);
    }

    // 대기 취소표 건너뛰기
    public long jump() {
        return priorityQueue.poll();
    }

    public long getCancelSize() {
        return priorityQueue.size();
    }

    public long getCancelPeek() {
        return priorityQueue.peek();
    }

    // 대기표 취소 초기화
    public void resetCancel() {
        priorityQueue.clear();
    }

    // 해당 offset 레코드의 발행 시각 반환
    private long getRecordTimeStamp(long offset) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seek(topicPartition, offset);
        ConsumerRecord<String, String> record = kafkaConsumer.poll(1000).iterator().next();
        return record.timestamp();
    }

    // kafka 맨 앞에 있는 대기표가 5초 이상 해결되지 않으면 삭제하기
    public void schedulerProcess() {
        long committedOffset = getCommittedOffset();
        long recordTimeStamp = getRecordTimeStamp(committedOffset);
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp - recordTimeStamp >= 5000) {
            // 해당 offset의 레코드 건너뛰기
            cancel(committedOffset);
        }
    }
}
