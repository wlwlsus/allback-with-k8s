package com.allback.cygiuser.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
