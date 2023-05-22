package com.allback.cygipayment.service;

import com.allback.cygipayment.client.UserServerClient;
import com.allback.cygipayment.dto.request.AmountReqDto;
import com.allback.cygipayment.dto.request.KakaoPayApproveReqDto;
import com.allback.cygipayment.dto.request.KakaoPayReqDto;
import com.allback.cygipayment.dto.response.KakaoPayApproveResDto;
import com.allback.cygipayment.dto.response.KakaoPayResDto;
import com.allback.cygipayment.util.exception.BaseException;
import com.allback.cygipayment.util.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayServiceImpl implements KakaoPayService {

	private final UserServerClient userServerClient;
	private final RestTemplate restTemplate = new RestTemplate();


	@Value("${kakao.host}")
	private String HOST;

	@Value("${kakao.app-key}")
	private String APP_KEY;

	@Override
	public KakaoPayResDto requestPayment(KakaoPayReqDto request) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + APP_KEY);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cid", request.getCid());
		params.add("partner_order_id", request.getPartnerOrderId());
		params.add("partner_user_id", request.getPartnerUserId());
		params.add("item_name", request.getItemName());
		params.add("quantity", Integer.toString(request.getQuantity()));
		params.add("total_amount", Integer.toString(request.getTotalAmount()));
		params.add("tax_free_amount", Integer.toString(request.getTaxFreeAmount()));
		params.add("approval_url", request.getApprovalUrl());
		params.add("cancel_url", request.getCancelUrl());
		params.add("fail_url", request.getFailUrl());

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

		ResponseEntity<KakaoPayResDto> responseEntity = restTemplate.postForEntity(HOST + "/v1/payment/ready", requestEntity, KakaoPayResDto.class);
		log.info("data : {}", responseEntity);
		return responseEntity.getBody();
	}

	@Override
	public KakaoPayApproveResDto approvePayment(long userId, KakaoPayApproveReqDto request) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + APP_KEY);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("pg_token", request.getPgToken());
		params.add("cid", request.getCid());
		params.add("tid", request.getTid());
		params.add("partner_order_id", request.getPartnerOrderId());
		params.add("partner_user_id", request.getPartnerUserId());

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

		ResponseEntity<KakaoPayApproveResDto> responseEntity = restTemplate.postForEntity(HOST + "/v1/payment/approve",
				requestEntity, KakaoPayApproveResDto.class);

		KakaoPayApproveResDto response = responseEntity.getBody();

		if (response == null)
			throw new BaseException(ErrorMessage.FAIL_PAYMENT);

		// 포인트 충전하기
		AmountReqDto amountReqDto = new AmountReqDto();
		amountReqDto.setUserId(userId);

		amountReqDto.setAmount(response.getAmount().getTotal());
		userServerClient.amount(amountReqDto);

		return response;
	}
}
