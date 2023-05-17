package com.allback.cygipayment.dto.response.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CardInfo {
	@JsonProperty("interest_free_install")
	private String interestFreeInstall;
	@JsonProperty("bin")
	private String bin;
	@JsonProperty("card_type")
	private String cardType;
	@JsonProperty("card_mid")
	private String cardMid;
	@JsonProperty("approved_id")
	private String approvedId;
	@JsonProperty("install_month")
	private String installMonth;
	@JsonProperty("purchase_corp")
	private String purchaseCorp;
	@JsonProperty("purchase_corp_code")
	private String purchaseCorpCode;
	@JsonProperty("issuer_corp")
	private String issuerCorp;
	@JsonProperty("issuer_corp_code")
	private String issuerCorpCode;
	@JsonProperty("kakaopay_purchase_corp")
	private String kakaopayPurchaseCorp;
	@JsonProperty("kakaopay_purchase_corp_code")
	private String kakaopayPurchaseCorpCode;
	@JsonProperty("kakaopay_issuer_corp")
	private String kakaopayIssuerCorp;
	@JsonProperty("kakaopay_issuer_corp_code")
	private String kakaopayIssuerCorpCode;
}
