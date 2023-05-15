package com.allback.cygipayment.service;

import com.allback.cygipayment.dto.response.BalanceStateResDto;
import com.allback.cygipayment.entity.Balance;
import com.allback.cygipayment.entity.BalanceState;
import com.allback.cygipayment.mapper.BalanceStateMapper;
import com.allback.cygipayment.repository.BalanceStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{

    private final BalanceStateRepository balanceStateRepository;
    private final BalanceStateMapper balanceStateMapper;

    @Override
    public Page<BalanceStateResDto> getBalances(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BalanceState> balanceStates = balanceStateRepository.findAll(pageRequest);
        log.info("[getBalances] : 정산내역 조회 성공");
        return balanceStates.map(balanceState -> balanceStateMapper.toDto(balanceState));
    }
}
