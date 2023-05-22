package com.allback.cygipayment.dto.response;

import com.allback.cygipayment.dto.response.kakao.Amount;
import com.allback.cygipayment.dto.response.kakao.CardInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-26
 * description :
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class KakaoPayApproveResDto {
	@JsonProperty("aid")
	private String aid;
	@JsonProperty("tid")
	private String tid;
	@JsonProperty("cid")
	private String cid;
	@JsonProperty("sid")
	private String sid;
	@JsonProperty("partner_order_id")
	private String partnerOrderId;
	@JsonProperty("partner_user_id")
	private String partnerUserId;
	@JsonProperty("payment_method_type")
	private String paymentMethodType;
	@JsonProperty("amount")
	private Amount amount;
	@JsonProperty("card_info")
	private CardInfo cardInfo;
	@JsonProperty("item_name")
	private String itemName;
	@JsonProperty("item_code")
	private String itemCode;
	@JsonProperty("quantity")
	private int quantity;
	@JsonProperty("created_at")
	private Date createdAt;
	@JsonProperty("approved_at")
	private Date approvedAt;

}
