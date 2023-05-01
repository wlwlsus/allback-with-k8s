package com.allback.cygipayment.init;

import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationInit {

    @Autowired
    ReservationRepository reservationRepository;

    @PostConstruct
    public void addReservation() {
        for (int i = 0; i < 100; i++) {
            Reservation reservation = Reservation.builder()
                .concertId(i%3)
                .stageId(i%3)
                .price(100)
                .seat("A-" + i)
                .build();
            if(i%3==0) {
                reservation.setStatus("예약중");
            } else if(i%3==1) {
                reservation.setStatus("예약완료");
            } else {
                reservation.setStatus("환불완료");
            }
            reservationRepository.save(reservation);
        }
    }
}
