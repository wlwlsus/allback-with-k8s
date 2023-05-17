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
    @Column(name = "concert_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long concertId; // 공연 ID

    @Column(name = "user_id", nullable = false)
    private long userId;    // 주최자 ID

    @Column(name = "title", nullable = false)
    private String title;   // 공연 이름

    @Column(name = "content", nullable = false, length = 1000)
    private String content; // 공연 설명


    @Column(name = "image", nullable = false, length = 1000)
    private String image;   // 공연 이미지 URL

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;    // 예매 시작 시각

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;  // 공연 시작 시각

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

}
