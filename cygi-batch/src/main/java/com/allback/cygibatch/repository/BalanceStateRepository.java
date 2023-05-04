package com.allback.cygibatch.repository;

import com.allback.cygibatch.entity.BalanceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceStateRepository extends JpaRepository<BalanceState, Long> {


}
