package com.allback.cygiconcert.service;

import com.allback.cygiconcert.client.PaymentServerClient;
import com.allback.cygiconcert.client.UserServerClient;
import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Stage;
import com.allback.cygiconcert.mapper.ConcertMapper;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.repository.StageRepository;
import com.allback.cygiconcert.util.S3Upload;

import com.allback.cygiconcert.util.exception.BaseException;
import com.allback.cygiconcert.util.exception.ErrorMessage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final StageRepository stageRepository;
    private final ConcertMapper concertMapper;
    private final PaymentServerClient paymentServerClient;
    private final UserServerClient userServerClient;
    private final S3Upload s3Upload;

    @Override
    public long registerConcert(ConcertReqDto concertReqDto, MultipartFile image) throws Exception {
        //주최자 id 있는지 확인
        long userId = concertReqDto.getUserId();
        log.info("[registerConcert] : 주최자id 조회 시도, userId : {}", userId);
        ResponseEntity<Void> responseEntity = userServerClient.checkUser(userId);

        // userServer에서 400 에러가 발생한 경우
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new BaseException(ErrorMessage.USER_NOT_FOUND);
        }
        log.info("[registerConcert] : 주최자id 조회 성공, userId : {}", userId);


        //공연장 id 있는지 확인
        Stage stage = stageRepository.findById(concertReqDto.getStageId())
            .orElseThrow(() -> new BaseException(ErrorMessage.STAGE_NOT_FOUND));
        log.info("[registerConcert] : 공연장id 조회 성공, stageId : {}", stage.getStageId());

        //이미지 링크 생성
        String imgLink = getImgLink(image);
        log.info("[registerConcert] : 이미지 링크생성, imgLink : {}", imgLink);

        //공연 등록
        System.out.println(concertMapper.toEntity(concertReqDto).toString());
        concertReqDto.setImage(imgLink);
        Concert savedConcert = concertRepository.save(concertMapper.toEntity(concertReqDto));

        log.info("[registerConcert] : 공연장 등록 성공");
        return savedConcert.getConcertId();
    }

    @Override
    public List<ConcertPageResDto> getConcertPage(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        List<Concert> concertPage = concertRepository.findAll(pageRequest).getContent();
        log.info("[getConcertPage] : 공연장 목록 조회 성공 : {}", concertPage.size());
        //all, rest 추가 해야함
        //우선 concertId만 빼기
        List<ConcertPageResDto> concertPageList = concertMapper.toDtoList(concertPage);
        List<Long> concertIdList = new ArrayList<>();
        int size = concertPageList.size();
        for (int i = 0; i < size; i++) {
            concertIdList.add(concertPage.get(i).getConcertId());
        }
        List<Integer> allList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Concert concert = concertRepository.findById(concertIdList.get(i))
                .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));
            allList.add(concert.getStage().getCol() * concert.getStage().getRow());
        }
        List<Integer> soldList = paymentServerClient.getRestSeatCntList(concertIdList).getBody();
        for (int i = 0; i < size; i++) {
            int all = allList.get(i);
            concertPageList.get(i).setAll(all);
            concertPageList.get(i).setRest(all - soldList.get(i));
        }
        return concertPageList;
    }

    @Override
    public ConcertResDto getConcert(long id) {
        Concert concert = concertRepository.findById(id)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));
        ConcertResDto concertResDto = concertMapper.toDto(concert);
        concertResDto.setStageId(concert.getStage().getStageId());
        concertResDto.setPrice(concert.getStage().getPrice());
        concertResDto.setLocation(concert.getStage().getLocation());
        log.info("[getConcert] : 공연장 조회 성공, concertId : {}", concert.getConcertId());
        return concertResDto;
    }

    @Override
    public List<Long> getEndedConcert(LocalDate now) {
        List<Long> endedConcert = concertRepository.getEndedConcert(now);
        log.info("[getEndedConcert] : 종료된 공연장 조회 성공, size : {}", endedConcert.size());
        return endedConcert;
    }

    @Override
    public Long getUserId(long concertId) {
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));
        log.info("[getUserId] : 주최자 id 조회 성공, userId : {}", concert.getUserId());
        return concert.getUserId();
    }

    @Override
    public String getConcertTitle(long concertId) {
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));
        log.info("[getTitle] : 콘서트 title 조회 성공, title : {}", concert.getTitle());
        return concert.getTitle();
    }

    @Override
    public String getImgLink(MultipartFile image) throws Exception {
        //파일이름 설정
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        //aws s3 저장
        String imgLink = s3Upload.uploadFileV1(fileName, image);

        return imgLink;
    }

    @Override
    public void deleteConcert(long concertId) {
        concertRepository.deleteById(concertId);
    }
}
