package com.allback.cygipayment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * author : cadqe13@gmail.com date : 2023-04-25 description :
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReservationReqDto {

<<<<<<< HEAD
    private long concertId;
    private long stageId;
=======
	private long concertId;
	private long stageId;
>>>>>>> ff8c5f068021393590e5e042a33ed8acba26ef2b

    private long userId;

    private String status;

    private int price;

    private String seat;
}
