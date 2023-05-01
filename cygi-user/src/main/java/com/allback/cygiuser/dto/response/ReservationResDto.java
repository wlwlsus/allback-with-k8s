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

  private Long concertId;
  private Long stageId;
  private Long userId;
  private String status;
  private Integer price;
  private String seat;
}
