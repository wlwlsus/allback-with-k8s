package com.allback.cygiconcert.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcertPageResDto {

	private Long concertId;  //  공연 ID

	private String title;   // 공연 이름

	private String image;   // 공연 이미지 URL

	private LocalDateTime endDate;  // 공연 시작 시각

}
