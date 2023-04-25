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
public class Amount {
	@JsonProperty("total")
	private int total;
	@JsonProperty("tax_free")
	private int taxFree;
	@JsonProperty("vat")
	private int vat;
	@JsonProperty("point")
	private int point;
	@JsonProperty("discount")
	private int discount;
	@JsonProperty("green_deposit")
	private int greenDeposit;

}
