package com.allback.cygipayment.service;

import com.allback.cygipayment.client.UserServerClient;
import com.allback.cygipayment.dto.request.AmountReqDto;
import com.allback.cygipayment.dto.request.ReservationFillReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.mapper.ReservationMapper;
import com.allback.cygipayment.repository.ReservationRepository;
import com.allback.cygipayment.util.exception.BaseException;
import com.allback.cygipayment.util.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final UserServerClient userServerClient;
  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  private static final String REFUND_Message = "환불완료";
  private static final String BALANCE_Message = "정산완료";
  private static final String RESERVE_Message = "예약완료";

  @Override
  public List<ReservationResDto> getReservationList(long userId, Pageable pageable) {
    List<Reservation> reservationPage = reservationRepository.findByUserId(userId, pageable).getContent();
    return reservationMapper.toDtoList(reservationPage);
  }

  @Override
  public ReservationResDto getReservationById(long reservationId) {
    Optional<Reservation> reservationPage = reservationRepository.findById(reservationId);
    Reservation reservation = reservationPage.orElse(null);

    if (reservation == null) throw new BaseException(ErrorMessage.NOT_EXIST_RESERVATION_NUMBER);

    return reservationMapper.toDto(reservation);
  }


  @Override
  @Transactional
  public void cancelReservation(long reservationId) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
    Reservation reservation = optionalReservation.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_RESERVATION_NUMBER));

    if (reservation.getStatus().equals(BALANCE_Message)) throw new BaseException(ErrorMessage.ALREADY_BALANCE);

    if (reservation.getStatus().equals(REFUND_Message)) throw new BaseException(ErrorMessage.ALREADY_REFUND);

    // 환불 로직 수행
    reservation.setStatus(REFUND_Message);
    reservationRepository.save(reservation);

    // 환불 금액 되돌리기
    AmountReqDto amountReqDto = new AmountReqDto();
    amountReqDto.setUserId(reservation.getUserId());
    amountReqDto.setAmount(reservation.getPrice());
    userServerClient.amount(amountReqDto);

  }


  @Override
  @Transactional
  public void reserve(long reservationId, ReservationFillReqDto reservationFillReqDto) {
    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
    Reservation reservation = optionalReservation.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_RESERVATION_NUMBER));
    if (reservation.getStatus().equals(BALANCE_Message)) throw new BaseException(ErrorMessage.ALREADY_BALANCE);
    if (reservation.getStatus().equals(RESERVE_Message)) throw new BaseException(ErrorMessage.ALREADY_RESERVE);

    // 예약 정보에서 필요한 필드를 추출합니다.
    long userId = reservationFillReqDto.getUserId();
    int price = reservationFillReqDto.getPrice();

    // 통장 테이블의 유저포인트에서 좌석 가격만큼 차감합니다.
    userServerClient.deductUserCash(userId, price);

<<<<<<< HEAD
		reservation.setReservation(stageId, userId, reserveMessage, price);
		reservationRepository.save(reservation);
	}

	@Override
	public Page<ReservationResDto> getReservations(int page) {

		List<Reservation> list = reservationRepository.findAll();

		List<ReservationResDto> resList = list.stream().map(e -> {
			ReservationResDto dto = new ReservationResDto();
			dto.setSeat(e.getSeat());
			dto.setPrice(e.getPrice());
			dto.setConcertId(e.getConcertId());
			dto.setStatus(e.getStatus());
			dto.setUserId(e.getUserId());
			dto.setStageId(e.getStageId());
			return dto;
		}).toList();


//		list to page
		PageRequest pageRequestForList = PageRequest.of(page, 10);
		int start = (int) pageRequestForList.getOffset();
		int end = Math.min((start + pageRequestForList.getPageSize()), resList.size());
		Page<ReservationResDto> resPage = new PageImpl<>(resList.subList(start, end),
				pageRequestForList, resList.size());

		return resPage;
	}
=======
    reservation.setReservation(userId, RESERVE_Message, price);
    reservationRepository.save(reservation);
  }
>>>>>>> b1efe210ed7b05ac4d3f4932bab4225de29f8924
}
