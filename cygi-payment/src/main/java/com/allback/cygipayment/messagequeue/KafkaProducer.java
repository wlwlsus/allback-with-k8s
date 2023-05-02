package com.allback.cygipayment.messagequeue;

import com.allback.cygipayment.dto.request.AmountReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * author : cadqe13@gmail.com
 * date : 2023-05-02
 * description :
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
	private KafkaTemplate<String, String> kafkaTemplate;

	public AmountReqDto send(String topic, AmountReqDto amountReqDto) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(amountReqDto);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}
		kafkaTemplate.send(topic, jsonInString);
		log.info("Kafka Producer sent data from the Order microservice: " + amountReqDto);
		return amountReqDto;
	}
}
