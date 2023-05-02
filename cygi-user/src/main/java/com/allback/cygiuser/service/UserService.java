package com.allback.cygiuser.service;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.response.ReservationResDto;
import com.allback.cygiuser.dto.response.UserResDto;

import java.util.List;

public interface UserService {

  void amount(AmountRequest request);

  void deductUserCash(long userId, int price);

  List<UserResDto> getAllUserInfo();

  List<ReservationResDto> getReservations();

  void updateCash(Long userId, Long cash);
}
