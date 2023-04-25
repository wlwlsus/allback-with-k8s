package com.allback.cygiuser.service;

import com.allback.cygiuser.dto.request.AmountRequest;

public interface UserService {

  void amount(AmountRequest request);

  void deductUserCash(long userId, int price);
}
