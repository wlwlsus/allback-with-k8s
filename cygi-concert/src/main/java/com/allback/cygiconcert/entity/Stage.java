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
    private Long stageId;

    @Column(name = "\"row\"", nullable = false)
    private Integer row;

    @Column(nullable = false)
    private Integer col;

    @Column(nullable = false)
    private Integer price;
}
