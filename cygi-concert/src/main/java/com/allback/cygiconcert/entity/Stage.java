package com.allback.cygiconcert.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "stage")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stageId;

    @Column(name = "\"row\"", nullable = false)
    private int row;

    @Column(nullable = false)
    private int col;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 1000)
    private String location; // 공연 장소
}
