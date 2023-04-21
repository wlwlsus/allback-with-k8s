package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ConcertService {

    void registConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception;
    Page<ConcertResDto.CustomConcertResDto> getConcertPage(int page);

    ConcertResDto.CustomConcertResDto getConcert(Long concertId);
}
