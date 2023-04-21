package com.allback.cygiconcert.controller;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.service.ConcertServiceImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {
	private final ConcertServiceImpl concertService;

	@PostMapping("")
	public ResponseEntity<Void> registConcert(@RequestPart(name = "concert") ConcertReqDto concertReqDto, @RequestPart MultipartFile image)
			throws Exception {
		concertService.registConcert(concertReqDto, image);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@GetMapping("")
	public ResponseEntity<List<ConcertResDto>> getConcerts(@RequestParam int page) {
		List<ConcertResDto> concertPage = concertService.getConcertPage(page);
		return new ResponseEntity<>(concertPage, HttpStatus.OK);
	}

	@GetMapping("{concertId}")
	public ResponseEntity<ConcertResDto> getConcert(@PathVariable long concertId) {
		ConcertResDto concert = concertService.getConcert(concertId);
		return new ResponseEntity<>(concert, HttpStatus.OK);
	}
}
