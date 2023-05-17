package com.allback.cygipayment.mapper;

import com.allback.cygipayment.dto.response.BalanceStateResDto;
import com.allback.cygipayment.entity.BalanceState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceStateMapper {
    BalanceStateResDto toDto(BalanceState balanceState);
}
