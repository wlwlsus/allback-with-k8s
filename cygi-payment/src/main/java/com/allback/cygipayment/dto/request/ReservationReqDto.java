package com.allback.cygipayment.dto.request;

import lombok.Getter;
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
public class ReservationReqDto {

	private long stageId;
	private long userId;
	private String status;
	private int price;

}
