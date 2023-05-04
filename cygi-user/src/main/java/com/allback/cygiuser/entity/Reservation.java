package com.allback.cygiuser.entity;

//import com.allback.cygipayment.util.BaseTimeEntity;
import com.allback.cygiuser.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false, length = 20, columnDefinition = "BIGINT UNSIGNED")
    private long reservationId;

    @Column(name = "concert_id", nullable = false, length = 20)
    private long concertId;

    @Column(name = "stage_id", nullable = false, length = 20)
    private long stageId;

    @Column(name = "user_id", nullable = false, length = 20)
    private long userId;

    @Column(name = "status", nullable = false, length = 150)
    private String status;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "seat", nullable = false, length = 10)
    private String seat;

    public void setReservation(long stageId, long userId, String status, int price) {
        this.stageId = stageId;
        this.userId = userId;
        this.status = status;
        this.price = price;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public void setStageId(long stageId) {
        this.stageId = stageId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
