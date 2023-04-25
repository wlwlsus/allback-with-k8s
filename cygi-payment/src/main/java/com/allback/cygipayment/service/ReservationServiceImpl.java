package com.allback.cygipayment.service;

import com.allback.cygipayment.client.UserServerClient;
import com.allback.cygipayment.dto.request.RefundRequest;
import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.mapper.ReservationMapper;
import com.allback.cygipayment.repository.ReservationRepository;
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

  private final String refundMsg = "Refund Complete";

  @Override
  public List<ReservationResDto> getReservationList(Pageable pageable) {
    List<Reservation> reservationPage = reservationRepository.findAll(pageable).getContent();
    return reservationMapper.toDtoList(reservationPage);
  }

  @Override
  public ReservationResDto getReservationById(long reservationId) {
    Optional<Reservation> reservationPage = reservationRepository.findById(reservationId);
    Reservation reservation = reservationPage.orElse(null);

    if (reservation == null) throw new NullPointerException("reservationPage must not be null");

    return reservationMapper.toDto(reservation);
  }


  @Override
  @Transactional
  public void cancelReservation(long reservationId) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
    Reservation reservation = optionalReservation.orElseThrow(() -> new RuntimeException("예약 번호가 존재하지 않습니다."));

    if (reservation.getStatus().equals(refundMsg)) throw new RuntimeException("이미 환불 처리되었습니다.");

    // 환불 로직 수행
    reservation.setStatus(refundMsg);
    reservationRepository.save(reservation);

    // 환불 금액 되돌리기
    RefundRequest refundRequest = new RefundRequest();
    refundRequest.setUserId(reservation.getUserId());
    refundRequest.setRefundAmount(reservation.getPrice());
    userServerClient.refund(refundRequest);

  }


  @Override
  public void reserve(long reservationId, ReservationReqDto reservationReqDto) {

  }

  @Override
  public void charge(long reservationId, long cash) {

  }
}
