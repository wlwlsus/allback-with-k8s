package com.allback.cygibatch.repository;

import com.allback.cygibatch.entity.Balance;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Query("SELECT b.concertId as concertId, SUM(b.price) as total, COUNT(*) as cnt FROM Balance b GROUP BY b.concertId HAVING b.concertId = :concertId")
    Map<String, Object> getBalanceByConcertId(Long concertId);

}
