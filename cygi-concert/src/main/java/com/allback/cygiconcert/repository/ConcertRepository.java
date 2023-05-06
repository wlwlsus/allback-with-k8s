package com.allback.cygiconcert.repository;

import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.entity.Concert;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    @Query("SELECT c.concertId FROM Concert c WHERE date_format(c.endDate, '%Y-%m-%d') = date_format(:now, '%Y-%m-%d')")
    List<Long> getEndedConcert(LocalDate now);
}
