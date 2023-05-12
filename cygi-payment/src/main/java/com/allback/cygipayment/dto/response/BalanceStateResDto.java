package com.allback.cygipayment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class BalanceStateResDto {
    private long balanceId;
    private long concertId;
    private long userId;
    private long customer;
    private long proceed;
    private LocalDateTime createdDate;
}
