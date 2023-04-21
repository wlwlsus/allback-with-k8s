package com.allback.cygiconcert.controller;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.service.ConcertServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertServiceImpl concertService;
    @PostMapping("")
    public void registConcert(@RequestPart(name = "concert") ConcertReqDto concertReqDto, @RequestPart MultipartFile image)
        throws Exception {
        concertService.registConcert(concertReqDto, image);
    }
}
