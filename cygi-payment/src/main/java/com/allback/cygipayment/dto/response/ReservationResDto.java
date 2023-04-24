package com.allback.cygipayment.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

@Getter
@Setter
@ToString
public class ReservationResDto {

	private Long concertId;
	private Long stageId;
	private Long userId;
	private String status;
	private Integer price;
	private String seat;
}
