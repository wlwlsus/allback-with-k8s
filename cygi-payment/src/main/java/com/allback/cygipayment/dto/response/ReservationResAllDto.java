package com.allback.cygipayment.dto.response;

import lombok.Builder;
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
@Builder
public class ReservationResAllDto {

  private long reservationId;
  private String title;
  private String status;
  private int price;
  private String seat;
  private String modifiedDate;
  private long userId;
}
