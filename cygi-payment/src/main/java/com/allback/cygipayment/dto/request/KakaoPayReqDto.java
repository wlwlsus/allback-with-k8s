package com.allback.cygipayment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class KakaoPayReqDto {

	@Schema(description = "cid", example = "TC0ONETIME")
	@JsonProperty("cid")
	private String cid;

	@Schema(description = "partner_order_id", example = "1001")
	@JsonProperty("partner_order_id")
	private String partnerOrderId;

	@Schema(description = "partner_user_id", example = "user01")
	@JsonProperty("partner_user_id")
	private String partnerUserId;

	@Schema(description = "item_name", example = "테스트 상품")
	@JsonProperty("item_name")
	private String itemName;

	@Schema(description = "quantity", example = "1")
	@JsonProperty("quantity")
	private int quantity;

	@Schema(description = "total_amount", example = "10000")
	@JsonProperty("total_amount")
	private int totalAmount;

	@Schema(description = "tax_free_amount", example = "0")
	@JsonProperty("tax_free_amount")
	private int taxFreeAmount;

	@Schema(description = "approval_url", example = "http://localhost:3000/success")
	@JsonProperty("approval_url")
	private String approvalUrl;

	@Schema(description = "cancel_url", example = "http://localhost:3000/home")
	@JsonProperty("cancel_url")
	private String cancelUrl;

	@Schema(description = "fail_url", example = "http://localhost:3000/home")
	@JsonProperty("fail_url")
	private String failUrl;

}