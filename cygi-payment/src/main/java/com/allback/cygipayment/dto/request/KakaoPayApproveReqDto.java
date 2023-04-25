package com.allback.cygipayment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-26
 * description :
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class KakaoPayApproveReqDto {

	@Schema(description = "결제승인 요청을 인증하는 토큰", example = "pg_token")
	@JsonProperty("pg_token")
	private String pgToken;

	@Schema(description = "결제 고유번호, 결제 준비 API 응답에 포함", example = "tid")
	@JsonProperty("tid")
	private String tid;

	@Schema(description = "cid", example = "TC0ONETIME")
	@JsonProperty("cid")
	private String cid;

	@Schema(description = "partner_order_id", example = "1001")
	@JsonProperty("partner_order_id")
	private String partnerOrderId;

	@Schema(description = "partner_user_id", example = "user01")
	@JsonProperty("partner_user_id")
	private String partnerUserId;
}
