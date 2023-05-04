package com.allback.cygiuser.messagequeue;

import com.allback.cygiuser.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * author : cadqe13@gmail.com
 * date : 2023-05-02
 * description :
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
//	UserRepository repository;
//
//	@KafkaListener(topics = "example-user-topic")
//	public void updatePoint(String kafkaMessage) {
//		log.info("Kafka Message: ->" + kafkaMessage);
//		Map<Object, Object> map = new HashMap<>();
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
//			});
//		} catch (JsonProcessingException ex) {
//			ex.printStackTrace();
//		}
//
//		// amount 로직 처리하기
//		log.info("map data : {}", map);
//	}
}
