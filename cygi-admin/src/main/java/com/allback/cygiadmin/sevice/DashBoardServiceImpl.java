package com.allback.cygiadmin.sevice;

import com.allback.cygiadmin.client.PaymentServerClient;
import com.allback.cygiadmin.client.UserServerClient;
import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationResDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<UserResDto> getUsers() {
//        System.out.println("dashboard impl get users");
//        System.out.println(userServerClient.getUsers());
        return userServerClient.getUsers().getBody();
    }

    @Override
    public List<ReservationResDto> getReservations() {
//        System.out.println("dashboard impl get reservations");
//        System.out.println(paymentServerClient.getReservations());
        return paymentServerClient.getReservations().getBody();
    }

    @Override
    public List<BalanceResDto> getBalances() {
//        System.out.println("dashboard impl get balances");
//        System.out.println(paymentServerClient.getReservations());
        return null;
    }
}
