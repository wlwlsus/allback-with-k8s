package com.allback.cygiconcert.repository;

import com.allback.cygiconcert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
