package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConcertService {

    long registerConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception;

    List<ConcertPageResDto> getConcertPage(int page);

    ConcertResDto getConcert(long concertId);

    List<Long> getEndedConcert(LocalDate now);

    Long getUserId(long concertId);

    String getConcertTitle(long concertId);

    String getImgLink(MultipartFile image) throws Exception;

    void deleteConcert(long concertId);
}
