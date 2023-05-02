package com.allback.cygiconcert.service;

import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatRestCntResDto;
import com.allback.cygiconcert.dto.response.SeatInfoResDto;
import java.util.List;
import java.util.Map;

public interface SeatService {
    Map<String, Object> getStatus(long concertId) throws Exception;

    Long changeStatus(SeatStatusChangeReqDto seatStatusChangeReqDto)throws Exception;

    void deleteReservationById(long reservationId);

    SeatRestCntResDto getRestSeatCnt(long concertId);
}
