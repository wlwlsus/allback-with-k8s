package com.allback.cygiadmin.sevice;

import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationResDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DashBoardService {


    Page<UserResDto> getUsers(int page);

    Page<ReservationResDto> getReservations(int page);

    List<BalanceResDto> getBalances();
}
