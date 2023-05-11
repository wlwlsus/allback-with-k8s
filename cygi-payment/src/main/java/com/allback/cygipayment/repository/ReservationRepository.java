package com.allback.cygipayment.repository;

import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r.seat) FROM Reservation r WHERE r.concertId = :concertId AND r.status LIKE '예약%'")
    int getSoldSeatCntByConcertId(Long concertId);

    @Query("SELECT r.seat FROM Reservation r WHERE r.concertId = ?1 AND r.status LIKE '예약%'")
    List<String> findSoldSeatByConcertId(Long concertId);

    Page<Reservation> findByUserId(long userId, Pageable pageable);

    Page<Reservation> findAllBy(Pageable pageable);

    Optional<Reservation> findReservationByUserId(long userId);

}
