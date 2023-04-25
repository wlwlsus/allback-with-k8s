package com.allback.cygipayment.entity;

import com.allback.cygipayment.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@ToString
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id", nullable = false, length = 20, columnDefinition = "BIGINT UNSIGNED")
  private Long reservationId;

  @Column(name = "concert_id", nullable = false, length = 20)
  private Long concertId;

  @Column(name = "stage_id", nullable = false, length = 20)
  private Long stageId;

  @Column(name = "user_id", nullable = false, length = 20)
  private Long userId;

  @Column(name = "status", nullable = false, length = 150)
  private String status;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "seat", nullable = false, length = 10)
  private String seat;


  public void setStatus(String status) {
    this.status = status;
  }
}
