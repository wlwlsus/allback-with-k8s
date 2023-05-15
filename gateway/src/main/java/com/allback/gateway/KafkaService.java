package com.allback.gateway;

import com.allback.gateway.filter.KafkaRequestFilter;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
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
    private KafkaConsumer<String, String> schedulerConsumer;
    private long firstOffset = 0;
    private PriorityQueue<Long> priorityQueue = new PriorityQueue<>();  // 대기 취소 요청들의 offset 값들

    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.group-id}")
    private String groupId;

    @Value("${kafka.partition}")
    private int partition;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConsumer = kafkaConsumer;
    }

    // 해당 offset 레코드의 발행 시각 반환
    private long getRecordTimeStamp(long offset) {
        KafkaConsumer<String, String> newKafkaConsumer = createKafkaConsumer("myClientId");

        TopicPartition topicPartition = new TopicPartition(topic, partition);
        newKafkaConsumer.assign(Collections.singletonList(topicPartition));

        newKafkaConsumer.seek(topicPartition, offset);

        ConsumerRecords<String, String> records = newKafkaConsumer.poll(1000);

        if (newKafkaConsumer != null) {
            System.out.println("myClientId consumer remove!!");
            newKafkaConsumer.close();
        }

        if (records.count() == 0) {
            return 0;
        }

        System.out.println("record count ::: " + records.count());

        ConsumerRecord<String, String> record = records.iterator().next();
        return record.timestamp();
    }

    // kafka consumer가 제일 마지막에 소비한 레코드의 offset 값 반환
    public long getCommittedOffset() {
        KafkaConsumer<String, String> newKafkaConsumer = createKafkaConsumer("myClientId");
        TopicPartition topicPartition = new TopicPartition(topic, partition);

        long position = newKafkaConsumer.position(topicPartition);

        if (newKafkaConsumer != null) {
            System.out.println("myClientId consumer remove!!");
            newKafkaConsumer.close();
        }

        return position;
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
        System.out.println(offset + " record will jump");
        priorityQueue.add(offset);
    }

    // 대기 취소표 건너뛰기
    public long jump() {
        Long poll = priorityQueue.poll();
        System.out.println(poll + " record jump!!");
        return poll;
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



    // kafka 맨 앞에 있는 대기표가 5초 이상 해결되지 않으면 삭제하기
    public void schedulerProcess() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        if (this.schedulerConsumer == null) {
            System.out.println("create Scheduler Consumer!!");
            this.schedulerConsumer = createKafkaConsumer("scheduler");
        }

        firstOffset = Math.max(getCommittedOffset(), firstOffset);
        long recordTimeStamp = getRecordTimeStamp(firstOffset);
        long currentTimestamp = System.currentTimeMillis();

        if (recordTimeStamp == 0) return ;

        if (currentTimestamp - recordTimeStamp >= 10000) {
            // 해당 offset의 레코드 건너뛰기
            cancel(firstOffset);
            firstOffset++;
        }

        if (schedulerConsumer != null) {
            System.out.println("scheduler consumer remove!!");
            schedulerConsumer.close();
            schedulerConsumer = null;
        }
    }

    private KafkaConsumer<String, String> createKafkaConsumer(String clientId) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
//        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        TopicPartition topicPartition = new TopicPartition(topic, partition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));

        return kafkaConsumer;
    }
}
