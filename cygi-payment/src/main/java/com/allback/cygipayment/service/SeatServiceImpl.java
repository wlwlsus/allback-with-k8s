package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.request.SeatStatusChangeReqDto;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.mapper.ReservationMapper;
import com.allback.cygipayment.repository.ReservationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    @Override
    public Long insertReservation(ReservationReqDto reservationReqDto) {
        Reservation reservation = reservationMapper.toEntity(reservationReqDto);
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation.getReservationId();
    }

    @Override
    public void deleteReservationById(Long reservationId) {

    }

    @Override
    public int getSoldSeatCnt(Long concertId) {
        return 0;
    }

    @Override
    public List<String> getSoldSeatList(long concertId) {
        return null;
    }
}
