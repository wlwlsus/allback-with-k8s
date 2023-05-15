package com.allback.gateway.filter;

import com.allback.gateway.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaRequestFilter extends AbstractGatewayFilterFactory<KafkaRequestFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaRequestFilter.class);

    private KafkaService kafkaService;

    @Autowired
    public KafkaRequestFilter(KafkaService kafkaService) {
        super(Config.class);
        this.kafkaService = kafkaService;
    }

    @Data
    public static class Config {}

    /**
     * Client에게 보낼 대기표 Response 형식
     */
    @Data
    @AllArgsConstructor
    static class JsonResponse {
        private Long uuid;
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


            long uuid; // 대기표 고유값
            long offset;    // 나의 대기표 순번
            int partition;  // 나의 대기표가 들어있는 partition 번호


            // 1단계 : 해당 요청이 대기표를 가지고 있는지 판단하기
            // 대기표를 가지고 있지 않다면 -> 최초 요청 -> kafka에 넣기
            if (!request.getHeaders().containsKey("KAFKA.UUID")) {
                uuid = System.currentTimeMillis();

//                CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(config.topicName, uuid);
                logger.info("카프카에 데이터 넣기!");
                CompletableFuture<SendResult<String, String>> send = kafkaService.send(Long.toString(uuid));

                try {
                    SendResult<String, String> sendResult = send.get();
                    partition = sendResult.getRecordMetadata().partition(); // 나의 대기표가 어느 partition에 들어갔는지
                    offset = sendResult.getRecordMetadata().offset();   // 나의 대기표가 몇 번째 순서로 들어갔는지

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                HttpHeaders headers = new HttpHeaders();
                headers.addAll(exchange.getRequest().getHeaders());
                headers.add("KAFKA.UUID", Long.toString(uuid));
                headers.add("KAFKA.PARTITION", Integer.toString(partition));
//                headers.add("KAFKA.OFFSET", Long.toString(offset));

                // 변경된 header로 request를 갱신
                request = exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.addAll(headers)).build();
                exchange = exchange.mutate().request(request).build();
            }

            // 대기표를 가지고 있다면
            else {
                uuid = Long.parseLong(request.getHeaders().get("KAFKA.UUID").get(0));
                partition = Integer.parseInt(request.getHeaders().get("KAFKA.PARTITION").get(0));
                offset = Long.parseLong(request.getHeaders().get("KAFKA.OFFSET").get(0));
            }


            // Consumer가 마지막으로 읽은 레코드의 Offset 값 알아내기
            long committedOffset = kafkaService.getCommittedOffset();   // Consumer가 마지막으로 읽은 레코드의 Offset 값
            long endOffset = kafkaService.getEndOffset();   // Producer가 마지막으로 넣은 레코드의 Offset 값

            logger.info("[KafkaRequestFilter] offset : " + request.getHeaders().get("KAFKA.OFFSET"));
            logger.info("[KafkaRequestFilter] committedOffset : " + committedOffset);
            logger.info("[KafkaRequestFilter] endOffset : " + endOffset);
            logger.info("[KafkaRequestFilter] offset : " + offset);


            // 대기 취소 요청이라면 -> 해당 offset 차례가 도래했을 때, 건너뛰게 하기
            if (offset == 0) {
                kafkaService.resetCancel();
            }

            if (request.getHeaders().containsKey("KAFKA.QUIT")) {

                kafkaService.cancel(offset);


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

            // 내 앞에 취소표가 있다면, 그만큼 committedOffset 값을 늘려주기
            // committedOffset >= offset이어야 됨
            while (kafkaService.getCancelSize() > 0 && offset > kafkaService.getCancelPeek()) {
                Long peek = kafkaService.jump();
//                committedOffset = Math.max(committedOffset, peek);
                committedOffset++;
            }

            // 2단계 : 대기열이 존재하는지 판단하기
            // 대기열이 있다면
            // -> 토큰 반환하고 끝내기
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
                while (kafkaService.getCancelSize() > 0 && offset <= endOffset && offset + 1 == kafkaService.getCancelPeek()) {
                    logger.info(offset + 1 + " record delete");
                    offset++;
                    kafkaService.jump();
                }

                HttpHeaders headers = new HttpHeaders();
                headers.addAll(exchange.getRequest().getHeaders());
                if (headers.containsKey("KAFKA.OFFSET"))
                    headers.replace("KAFKA.OFFSET", Collections.singletonList(Long.toString(offset)));
                else
                    headers.add("KAFKA.OFFSET", Long.toString(offset));

                // 변경된 header로 request를 갱신
                request = exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.addAll(headers)).build();
                exchange = exchange.mutate().request(request).build();

                // TODO : 메시지 uuid 값을 같이 넘겨줘서, 같은 메시지가 commit 되어야함?
                logger.info("offset in header :::: " + exchange.getRequest().getHeaders().get("KAFKA.OFFSET"));
                return chain.filter(exchange);
            }
        };
    }
}
