package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;

import java.util.List;

public interface SeatService {

    Long insertReservation(ReservationReqDto reservationReqDto);

    void deleteReservationById(Long reservationId);

    int getSoldSeatCnt(Long concertId);

    List<String> getSoldSeatList(Long concertId);

    List<Integer> getSoldSeatCntList(List<Long> concertIdList);
}
