package com.allback.cygiconcert.repository;

import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

	@Query("SELECT new com.allback.cygiconcert.dto.response.ConcertPageResDto(c.concertId, c.title, c.image, c.endDate) FROM Concert c")
	Page<ConcertPageResDto> getConcertPage(Pageable pageable);

}
