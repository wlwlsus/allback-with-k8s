package com.allback.cygiconcert.service;

import com.allback.cygiconcert.client.PaymentServerClient;
import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatRestCntResDto;
import com.allback.cygiconcert.dto.response.SeatInfoResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Stage;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.repository.StageRepository;
import com.allback.cygiconcert.util.exception.BaseException;
import com.allback.cygiconcert.util.exception.ErrorMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final PaymentServerClient paymentServerClient;
    @Override
    public Map<String, Object> getStatus(long concertId) throws Exception {
        //우선 해당 concertID가 존재하는 확인
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));

        //전체 좌석 정보 확인
        Stage stage = stageRepository.findById(concert.getStageId())
            .orElseThrow(() -> new BaseException(ErrorMessage.STAGE_NOT_FOUND));
        int row = stage.getRow();
        int col = stage.getCol();

        //결제컨테이너이너의 예약완료거나 예약중 좌석리스트반환
        List<String> seatList = paymentServerClient.getSeatInfo(concertId);

        //반환
        Map<String, Object> map = new HashMap<>();
        map.put("row", row);
        map.put("col", col);
        map.put("seatList", seatList);

        return map;
    }


    @Override
    public Long changeStatus(SeatStatusChangeReqDto seatStatusChangeReqDto) throws Exception {
        //우선 해당 concertID가 존재하는 확인
        Concert concert = concertRepository.findById(seatStatusChangeReqDto.getConcertId())
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));

        //좌석정보를 바탕으로 결제 컨테이너의 reservation 테이블을 예약중으로 insert해야함
        Long reservationId = paymentServerClient.chageStatus(seatStatusChangeReqDto);

        return reservationId;
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        //결제컨테이너의 reservation 테이블 에서 resevationId기반으로 삭제
        paymentServerClient.deleteReservationById(reservationId);
    }

    @Override
    public SeatRestCntResDto getRestSeatCnt(Long concertId) {
        //concertId가 있는지 확인
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));

        //전체 좌석수 확인
        Stage stage = stageRepository.findById(concert.getStageId())
            .orElseThrow(() -> new BaseException(ErrorMessage.STAGE_NOT_FOUND));
        int all  = stage.getCol() * stage.getRow();

        //남은 좌석수 확인
        //결제 컨테이너의 resevation테이블 조회해서 확인
        int rest = paymentServerClient.getRestSeatCnt(concertId);;

        //반환
        SeatRestCntResDto seatRestCntResDto = SeatRestCntResDto.builder()
            .all(all)
            .rest(rest)
            .build();

        return seatRestCntResDto;
    }
}
