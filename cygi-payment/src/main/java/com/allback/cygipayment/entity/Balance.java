package com.allback.cygipayment.entity;

import com.allback.cygipayment.util.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "balance")
public class Balance extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id", nullable = false, length = 20, columnDefinition = "BIGINT UNSIGNED")
    private long balanceId;
    @Column(name = "concert_id", nullable = false, length = 20)
    private long concertId;

    @Column(name = "user_id", nullable = false, length = 20)
    private long userId;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "seat", nullable = false, length = 10)
    private String seat;
}
