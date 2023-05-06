package com.allback.cygipayment.repository;

import com.allback.cygipayment.entity.BalanceState;
import com.allback.cygipayment.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceStateRepository extends JpaRepository<BalanceState, Long> {


}
