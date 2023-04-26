package com.allback.cygiadmin.sevice;

import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationResDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import java.util.List;

public interface DashBoardService {


    List<UserResDto> getUsers();

    List<ReservationResDto> getReservations();

    List<BalanceResDto> getBalances();
}
