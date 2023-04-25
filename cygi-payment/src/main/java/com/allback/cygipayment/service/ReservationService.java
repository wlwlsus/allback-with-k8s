package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

public interface ReservationService {

	List<ReservationResDto> getReservationList(Pageable pageable);

	ReservationResDto getReservationById(long reservationId);

	void cancelReservation(long reservationId);

	void reserve(long reservationId, ReservationReqDto reservationReqDto);

	void charge(long reservationId, long cash);

}
