package com.allback.cygiadmin.sevice;

import com.allback.cygiadmin.client.PaymentServerClient;
import com.allback.cygiadmin.client.UserServerClient;
import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationResDto;
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

    @Override
    public Page<UserResDto> getUsers(int page) {
        System.out.println("dashboard impl get users");
        Page<UserResDto> r = userServerClient.getUsers(page).getBody();
        System.out.println("success!!!!!!!!!!!!!!!!!");
//        System.out.println(userServerClient.getUsers());
        return r;
    }

    @Override
    public Page<ReservationResDto> getReservations(int page) {
//        System.out.println("dashboard impl get reservations");
//        System.out.println(paymentServerClient.getReservations());
        return paymentServerClient.getReservations(page).getBody();
    }

    @Override
    public List<BalanceResDto> getBalances() {
//        System.out.println("dashboard impl get balances");
//        System.out.println(paymentServerClient.getReservations());
        return null;
    }
}
