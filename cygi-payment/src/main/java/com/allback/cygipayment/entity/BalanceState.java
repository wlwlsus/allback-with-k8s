package com.allback.cygipayment.entity;

import com.allback.cygipayment.util.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "balance_state")
public class BalanceState extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_state_id", nullable = false, length = 20, columnDefinition = "BIGINT UNSIGNED")
    private long balanceId;

    @Column(name = "concert_id", nullable = false, length = 20)
    private long concertId;
    @Column(name = "user_id", nullable = false, length = 20)
    private long userId;

    @Column(name = "customer", nullable = false)
    private long customer;

    @Column(name = "proceed", nullable = false)
    private long proceed;

}
