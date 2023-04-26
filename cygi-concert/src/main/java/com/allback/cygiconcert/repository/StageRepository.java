package com.allback.cygiconcert.repository;

import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Stage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
}
