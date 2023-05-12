package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.response.BalanceStateResDto;
import org.springframework.data.domain.Page;

public interface BalanceService {
    Page<BalanceStateResDto> getBalances(int page, int size);
}
