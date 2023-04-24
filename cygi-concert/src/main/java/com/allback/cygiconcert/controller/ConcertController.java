package com.allback.cygiconcert.controller;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.service.ConcertServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "concert", description = "공연 API")
@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertServiceImpl concertService;

    @Operation(summary = "공연 등록")
    @PostMapping("")
    public ResponseEntity<Void> registConcert(
        @RequestPart(name = "concert") ConcertReqDto concertReqDto,
        @RequestPart MultipartFile image)
        throws Exception {
        concertService.registConcert(concertReqDto, image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "공연 목록 조회")
    @GetMapping("")
    public ResponseEntity<List<ConcertPageResDto>> getConcerts(@RequestParam int page) {
        List<ConcertPageResDto> concertPage = concertService.getConcertPage(page);
        return new ResponseEntity<>(concertPage, HttpStatus.OK);
    }

    @Operation(summary = "공연 정보 조회")
    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertResDto> getConcert(@PathVariable long concertId) {
        ConcertResDto concert = concertService.getConcert(concertId);
        return new ResponseEntity<>(concert, HttpStatus.OK);
    }
}
