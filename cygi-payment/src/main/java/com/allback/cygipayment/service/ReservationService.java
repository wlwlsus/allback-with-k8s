package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.request.ReservationFillReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

public interface ReservationService {

	List<ReservationResDto> getReservationList(long userId, Pageable pageable);

	ReservationResDto getReservationById(long reservationId);

	void cancelReservation(long reservationId);

	void reserve(long reservationId, ReservationFillReqDto reservationFillReqDto);

	Page<ReservationResDto> getReservations(int page);
}
