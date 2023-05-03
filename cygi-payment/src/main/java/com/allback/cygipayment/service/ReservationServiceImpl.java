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

  private final String refundMessage = "Refund Complete";
  private final String reserveMessage = "Reserve Complete";

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

    if (reservation.getStatus().equals(refundMessage)) throw new BaseException(ErrorMessage.ALREADY_REFUND);

    // 환불 로직 수행
    reservation.setStatus(refundMessage);
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
    if (reservation.getStatus().equals(reserveMessage)) throw new BaseException(ErrorMessage.ALREADY_RESERVE);

    // 예약 정보에서 필요한 필드를 추출합니다.
    long stageId = reservationFillReqDto.getStageId();
    long userId = reservationFillReqDto.getUserId();
    int price = reservationFillReqDto.getPrice();

    // 통장 테이블의 유저포인트에서 좌석 가격만큼 차감합니다.
    userServerClient.deductUserCash(userId, price);

    reservation.setReservation(stageId, userId, reserveMessage, price);
    reservationRepository.save(reservation);
  }
}
