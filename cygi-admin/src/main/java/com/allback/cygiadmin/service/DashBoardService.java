package com.allback.cygiadmin.service;

import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationListResAllDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DashBoardService {


    Page<UserResDto> getUsers(int page, String authorization);

    ReservationListResAllDto getReservations(int page, int size, String authorization);

    Page<BalanceResDto> getBalances(int page, int size, String authorization);
}
