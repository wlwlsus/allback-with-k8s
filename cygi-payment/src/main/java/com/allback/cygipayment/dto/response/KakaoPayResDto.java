package com.allback.cygipayment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class KakaoPayResDto {

	private String tid;

	@JsonProperty("next_redirect_pc_url")
	private String nextRedirectPcUrl;

	@JsonProperty("next_redirect_mobile_url")
	private String nextRedirectMobileUrl;

	@JsonProperty("next_redirect_app_url")
	private String nextRedirectAppUrl;

	@JsonProperty("created_at")
	private Date createdAt;
}