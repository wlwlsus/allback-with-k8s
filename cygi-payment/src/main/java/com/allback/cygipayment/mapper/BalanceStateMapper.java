package com.allback.cygipayment.mapper;

import com.allback.cygipayment.dto.response.BalanceStateResDto;
import com.allback.cygipayment.entity.BalanceState;
import org.mapstruct.Mapper;
<<<<<<< HEAD
import org.mapstruct.Mapping;
=======
>>>>>>> ff8c5f068021393590e5e042a33ed8acba26ef2b

@Mapper(componentModel = "spring")
public interface BalanceStateMapper {
    BalanceStateResDto toDto(BalanceState balanceState);
}
