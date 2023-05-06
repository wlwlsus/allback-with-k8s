package com.allback.cygipayment.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

@Getter
@Setter
@ToString
@Builder
public class ReservationResDto {

  private long reservationId;
  private String title;
  private String status;
  private int price;
  private String seat;
  private String modifiedDate;
}
