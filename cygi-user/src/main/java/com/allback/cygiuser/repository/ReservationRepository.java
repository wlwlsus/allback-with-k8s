package com.allback.cygiuser.repository;

//import com.allback.cygipayment.entity.Reservation;
import com.allback.cygiuser.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

//    @Query("SELECT COUNT(r.seat) FROM Reservation r WHERE r.concertId = :concertId AND r.status LIKE '예약%'")
//    int getSoldSeatCntByConcertId(Long concertId);
//
//    @Query("SELECT r.seat FROM Reservation r WHERE r.concertId = ?1 AND r.status LIKE '예약%'")
//    List<String> findSoldSeatByConcertId(Long concertId);
}
