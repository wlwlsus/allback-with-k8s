package com.allback.cygiadmin.service;

import com.allback.cygiadmin.client.PaymentServerClient;
import com.allback.cygiadmin.client.UserServerClient;
import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationListResAllDto;
import com.allback.cygiadmin.dto.response.UserResDto;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {

	private final UserServerClient userServerClient;
	private final PaymentServerClient paymentServerClient;
//    private final String authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNzY2ODgyMTczIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4NTYzNDQ4OX0.NJMI5o7XlD2LCQ7FFbkQDiDnk2FghZ05lBtO_WNCeoo";

	@Override
	public Page<UserResDto> getUsers(int page) {
		System.out.println("dashboard impl get users");
		Page<UserResDto> r = userServerClient.getUsers(page).getBody();
//		Page<UserResDto> r = userServerClient.getUsers(page, authorization).getBody();
		System.out.println("success!!!!!!!!!!!!!!!!!");
//        System.out.println(userServerClient.getUsers());
		return r;
	}

	@Override
	public ReservationListResAllDto getReservations(int page, int size) {
		return paymentServerClient.getReservations(page, size).getBody();
	}

	@Override
	public Page<BalanceResDto> getBalances(int page, int size) {
		log.info("[getBalances] : 정산내역조회");
		return paymentServerClient.getBalances(page, size).getBody();
	}
}
