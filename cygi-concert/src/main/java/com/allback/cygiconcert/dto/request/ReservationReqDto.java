package com.allback.cygiconcert.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReservationReqDto {

    private Long concertId;

    private Long stageId;

    private Long userId;

    private String status;

    private int price;

    private String seat;
}
