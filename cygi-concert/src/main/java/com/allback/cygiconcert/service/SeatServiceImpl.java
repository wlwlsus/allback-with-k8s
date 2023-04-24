package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatRestCntResDto;
import com.allback.cygiconcert.dto.response.SeatStatusResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Stage;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.repository.StageRepository;
import com.allback.cygiconcert.util.exception.BaseException;
import com.allback.cygiconcert.util.exception.ErrorMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final ConcertRepository concertRepository;
    private final StageRepository stageRepository;
    @Override
    public List<SeatStatusResDto> getStatus(long concertId) throws Exception {
        //우선 해당 concertID가 존재하는 확인
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));
        //결제컨테이너이너의 예약완료된 좌석은 true로 반환

        //이거는 물어봐야함
        return null;
    }


    @Override
    public Long changeStatus(SeatStatusChangeReqDto seatStatusChangeReqDto) throws Exception {
        //우선 해당 concertID가 존재하는 확인
        Concert concert = concertRepository.findById(seatStatusChangeReqDto.getConcertId())
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));
        //좌석정보를 바탕으로 결제 컨테이너의 reservation 테이블을 예약중으로 insert해야함

        //생성된 reservationId 반환
        return 0L;
    }

    @Override
    public void deleteReservationId(Long reservationId) {
        //결제컨테이너의 reservation 테이블 에서 resevationId기반으로 삭제
        return;
    }

    @Override
    public SeatRestCntResDto getSeatRestCnt(Long concertId) {
        //concertId가 있는지 확인
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));

        //전체 좌석수 확인
        Stage stage = stageRepository.findById(concert.getStageId())
            .orElseThrow(() -> new BaseException(ErrorMessage.STAGE_NOT_FOUND));
        int all  = stage.getCol() * stage.getRow();

        //남은 좌석수 확인
        //결제 컨테이너의 resevation테이블 조회해서 화깅ㄴ
        int rest = 0;

        //반환
        SeatRestCntResDto seatRestCntResDto = SeatRestCntResDto.builder()
            .all(all)
            .rest(rest)
            .build();
        return seatRestCntResDto;
    }
}
