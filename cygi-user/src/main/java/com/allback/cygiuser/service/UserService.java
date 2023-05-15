package com.allback.cygiuser.service;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.request.UserTestReqDto;
import com.allback.cygiuser.dto.response.UserResDto;

import com.allback.cygiuser.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserService {

  void amount(AmountRequest request);

  void deductUserCash(long userId, int price);

  Page<UserResDto> getAllUserInfo(int page);

  void updateCash(long userId, long cash);

  int getPoint(long userId);

  ResponseEntity<?> logout(UserTestReqDto.Logout logout);

  Users getUser(long userId);

}
