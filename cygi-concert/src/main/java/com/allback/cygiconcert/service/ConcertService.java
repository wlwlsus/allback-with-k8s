package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConcertService {

    void registConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception;
    List<ConcertResDto> getConcertPage(int page);

    ConcertResDto getConcert(Long concertId);
}
