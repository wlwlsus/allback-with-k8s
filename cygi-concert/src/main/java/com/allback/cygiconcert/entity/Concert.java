package com.allback.cygiconcert.entity;

import com.allback.cygiconcert.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "concert")
public class Concert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId; // 공연 ID

    @Column(nullable = false)
    private Long userId;    // 주최자 ID

    @Column(nullable = false)
    private Long stageId;   // 공연장 ID

    @Column(nullable = false)
    private String title;   // 공연 이름

    @Column(nullable = false, length = 1000)
    private String content; // 공연 설명

    @Column(nullable = false, length = 1000)
    private String image;   // 공연 이미지 URL

    @Column(nullable = false)
    private LocalDateTime startDate;    // 예매 시작 시각

    @Column(nullable = false)
    private LocalDateTime endDate;  // 공연 시작 시각
}
