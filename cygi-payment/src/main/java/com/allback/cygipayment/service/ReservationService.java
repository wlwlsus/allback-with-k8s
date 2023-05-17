package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.request.ReservationFillReqDto;
import com.allback.cygipayment.dto.response.ReservationListResAllDto;
import com.allback.cygipayment.dto.response.ReservationListResDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import org.springframework.data.domain.Pageable;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

public interface ReservationService {

	ReservationListResDto getReservationList(long userId, Pageable pageable);

	ReservationResDto getReservationById(long reservationId);

	void cancelReservation(long reservationId);

	void reserve(long reservationId, ReservationFillReqDto reservationFillReqDto);

	ReservationListResAllDto getAllReservations(Pageable pageable);
}
