package com.allback.cygiuser.service;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.response.UserResDto;

import org.springframework.data.domain.Page;

public interface UserService {

  void amount(AmountRequest request);

  void deductUserCash(long userId, int price);

  Page<UserResDto> getAllUserInfo(int page);

  void updateCash(long userId, long cash);

  int getPoint(long userId);
}
