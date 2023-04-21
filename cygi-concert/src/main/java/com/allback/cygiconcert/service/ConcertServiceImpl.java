package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.util.S3Upload;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConcertServiceImpl implements ConcertService {
    private final ConcertRepository concertRepository;
    private final S3Upload s3Upload;
    @Override
    public void registConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception {
        //주최자 id 있는지 확인

        //공연장 id 있는지 확인

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

    private String getImgLink(MultipartFile image) throws Exception {
        //랜덤식별자 생성
        UUID uuid = UUID.randomUUID();
        //파일이름 설정
        String fileName = uuid + "_" + image.getOriginalFilename();
        //aws s3 저장
        String imgLink = s3Upload.uploadFileV1(fileName, image);
        return imgLink;
    }
}
