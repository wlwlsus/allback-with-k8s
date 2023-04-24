package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatRestCntResDto;
import com.allback.cygiconcert.dto.response.SeatStatusResDto;
import java.util.List;

public interface SeatService {
    List<SeatStatusResDto> getStatus(long concertId) throws Exception;

    Long changeStatus(SeatStatusChangeReqDto seatStatusChangeReqDto)throws Exception;

    void deleteReservationId(Long reservationId);

    SeatRestCntResDto getSeatRestCnt(Long concertId);
}
