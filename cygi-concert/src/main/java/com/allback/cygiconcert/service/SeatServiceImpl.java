package com.allback.cygiconcert.service;

import com.allback.cygiconcert.client.PaymentServerClient;
import com.allback.cygiconcert.dto.request.ReservationReqDto;
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
        int row = concert.getStage().getRow();
        int col = concert.getStage().getCol();

        //결제컨테이너이너의 예약완료거나 예약중 좌석리스트반환
        List<String> seatList = paymentServerClient.getSeatInfo(concertId).getBody();

        //반환
        Map<String, Object> map = new HashMap<>();
        map.put("row", row);
        map.put("col", col);
        map.put("seatList", seatList);
        log.info("[getStatus] : 공연 좌석정보 조회 성공");
        return map;
    }


    @Override
    public Long changeStatus(SeatStatusChangeReqDto seatStatusChangeReqDto) throws Exception {
        //우선 해당 concertID가 존재하는 확인
        Concert concert = concertRepository.findById(seatStatusChangeReqDto.getConcertId())
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));

        //해당 좌석이 점유 되어있는지 확인

        //좌석정보를 바탕으로 결제 컨테이너의 reservation 테이블을 예약중으로 insert해야함
        ReservationReqDto reservationReqDto = ReservationReqDto.builder()
            .concertId(concert.getConcertId())
            .stageId(concert.getStage().getStageId())
            .userId(concert.getUserId())
            .status("예약중")
            .price(concert.getStage().getPrice())
            .seat(seatStatusChangeReqDto.getSeat())
            .build();

        Long reservationId = paymentServerClient.chageStatus(reservationReqDto).getBody();
        log.info("[changeStatus] : 예약중 등록 성공, reservationId : {}", reservationId);
        return reservationId;
    }

    @Override
    public void deleteReservationById(long reservationId) {
        //결제컨테이너의 reservation 테이블 에서 resevationId기반으로 삭제
        paymentServerClient.deleteReservationById(reservationId);
        log.info("[deleteReservationById] : 예약중 삭제 성공");
    }

    @Override
    public SeatRestCntResDto getRestSeatCnt(long concertId) {
        //concertId가 있는지 확인
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new BaseException(ErrorMessage.CONCERT_NOT_FOUND));

        //전체 좌석수 확인
        int all  = concert.getStage().getCol() * concert.getStage().getRow();

        //남은 좌석수 확인
        //결제 컨테이너의 resevation테이블 조회해서 확인
        int rest = all - paymentServerClient.getRestSeatCnt(concertId).getBody();;

        //반환
        SeatRestCntResDto seatRestCntResDto = SeatRestCntResDto.builder()
            .all(all)
            .rest(rest)
            .build();

        log.info("[getRestSeatCnt] : 전체좌석, 남은좌석 조회 성공, all : {}, rest : {}", all, rest);
        return seatRestCntResDto;
    }
}
