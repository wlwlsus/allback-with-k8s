package com.allback.cygipayment.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReservationResDto {

  private long concertId;
  private long stageId;
  private long userId;
  private String status;
  private int price;
  private String seat;
}
