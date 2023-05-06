package com.allback.cygipayment.entity;

import com.allback.cygipayment.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
public class ReservationTest {
//    @Autowired
//    ReservationRepository reservationRepository;
//
//    @Test
//    @Commit
//    public void addReservation() {
//        for (int i = 0; i < 100; i++) {
//            Reservation reservation = Reservation.builder()
//                .concertId(i%3)
//                .price(100)
//                .seat("A-" + i)
//                .build();
//            if(i%3==0) {
//                reservation.setStatus("예약중");
//            } else if(i%3==1) {
//                reservation.setStatus("예약완료");
//            } else {
//                reservation.setStatus("환불완료");
//            }
//            reservationRepository.save(reservation);
//        }
//    }
}
