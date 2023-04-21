package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.util.S3Upload;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConcertServiceImpl implements ConcertService {
	private final ConcertRepository concertRepository;
	private final S3Upload s3Upload;

	//    private final RestTemplate restTemplate;
	@Override
	public void registConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception {
		//주최자 id 있는지 확인

		log.info("[registConcert] : 주최자id 조회 성공, userId : {}", concertReqDto.getStageId());
		//공연장 id 있는지 확인

		log.info("[registConcert] : 공연장id 조회 성공, stageId : {}", concertReqDto.getStageId());
		//이미지 링크 생성
		String imgLink = getImgLink(image);
		log.info("[registConcert] : 이미지 링크생성, imgLink : {}", imgLink);

		//공연 등록
		Concert concert = Concert.builder()
				.userId(concertReqDto.getUserId())
				.stageId(concertReqDto.getStageId())
				.title(concertReqDto.getTitle())
				.content(concertReqDto.getContent())
				.image(imgLink)
				.startDate(concertReqDto.getStartDate())
				.endDate(concertReqDto.getEndDate())
				.build();

		concertRepository.save(concert);
		log.info("[registConcert] : 공연장 등록 성공");

	}

	@Override
	public List<ConcertResDto> getConcertPage(int page) {
		PageRequest pageRequest = PageRequest.of(page - 1, 10);
		List<ConcertResDto> concertPage = concertRepository.getConcertPage(pageRequest).getContent();
		log.info("[getConcertPage] : 공연장 목록 조회 성공 : {}", concertPage);
		return concertPage;
	}

	@Override
	public ConcertResDto getConcert(Long id) {
		Concert concert = concertRepository.findById(id).get();
		ConcertResDto concertResDto = ConcertResDto.builder()
//            .concertId(concert.getConcertId())
//            .userId(concert.getUserId())
//            .stageId(concert.getStageId())
				.title(concert.getTitle())
//            .content(concert.getContent())
//            .image(concert.getImage())
//            .startDate(concert.getStartDate())
//            .endDate(concert.getEndDate())
				.build();
//        log.info("[getConcert] : 공연장 조회 성공, concertId : {}", concertResDto.getConcertId());
		return concertResDto;
	}

	private String getImgLink(MultipartFile image) throws Exception {
		//파일이름 설정
		String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
		//aws s3 저장
		String imgLink = s3Upload.uploadFileV1(fileName, image);
		return imgLink;
	}
}
