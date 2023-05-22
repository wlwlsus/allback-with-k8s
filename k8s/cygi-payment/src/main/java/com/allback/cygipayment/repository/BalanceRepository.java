package com.allback.cygipayment.repository;

import com.allback.cygipayment.entity.Balance;
import com.allback.cygipayment.entity.Reservation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Query("SELECT b.concertId as concertId, SUM(b.price) as total, COUNT(*) as cnt FROM Balance b GROUP BY b.concertId HAVING b.concertId = :concertId")
    Map<String, Object> getBalanceByConcertId(Long concertId);

}
