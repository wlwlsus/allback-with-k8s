package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.request.KakaoPayApproveReqDto;
import com.allback.cygipayment.dto.request.KakaoPayReqDto;
import com.allback.cygipayment.dto.response.KakaoPayApproveResDto;
import com.allback.cygipayment.dto.response.KakaoPayResDto;

public interface KakaoPayService {

	KakaoPayResDto requestPayment(KakaoPayReqDto request);

	KakaoPayApproveResDto approvePayment(long userId, KakaoPayApproveReqDto requestDto);

}
