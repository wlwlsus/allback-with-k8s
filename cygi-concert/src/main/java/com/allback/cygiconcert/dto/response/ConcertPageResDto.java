package com.allback.cygiconcert.dto.response;

import com.allback.cygiconcert.entity.Stage;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcertPageResDto {

    private long concertId;  //  공연 ID

    private String title;   // 공연 이름

    private String image;   // 공연 이미지 URL

    private LocalDateTime endDate;  // 공연 시작 시각
    private int all;  // 전체좌석
    private int rest;  // 남아있느 좌석

}
