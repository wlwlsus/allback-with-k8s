package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import org.springframework.web.multipart.MultipartFile;

public interface ConcertService {

    void registConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception;
}
