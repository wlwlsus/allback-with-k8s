package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.mapper.ReservationMapper;
import com.allback.cygipayment.repository.ReservationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationMapper reservationMapper;


	@Override
	public List<ReservationResDto> getReservationList(Pageable pageable) {
		List<Reservation> reservationPage = reservationRepository.findAll(pageable).getContent();
		return reservationMapper.toDtoList(reservationPage);
	}

	@Override
	public ReservationResDto getReservationById(long reservationId) {
		Optional<Reservation> reservationPage = reservationRepository.findById(reservationId);
		Reservation reservation = reservationPage.orElse(null);

		if (reservation == null)
			throw new NullPointerException("reservationPage must not be null");

		return reservationMapper.toDto(reservation);
	}
}
