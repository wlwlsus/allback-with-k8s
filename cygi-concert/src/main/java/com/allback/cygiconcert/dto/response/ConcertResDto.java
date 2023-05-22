package com.allback.cygiconcert.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResDto {
    private long concertId;
    private long userId;    // 주최자 ID

    private long stageId;   // 공연장 ID

    private String title;   // 공연 이름

    private String content; // 공연 설명
    private int price; // 공연 설명
    private String location; // 공연 설명

    private String image;   // 공연 이미지 URL

    private LocalDateTime startDate;    // 예매 시작 시각

    private LocalDateTime endDate;  // 공연 시작 시각
}
