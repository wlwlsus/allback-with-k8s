package com.allback.cygipayment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ReservationOriginalDto {
  private Long reservationId;
  private Long concertId;
  private Long stageId;
  private Long userId;
  private String status;
  private int price;
  private String seat;
}
