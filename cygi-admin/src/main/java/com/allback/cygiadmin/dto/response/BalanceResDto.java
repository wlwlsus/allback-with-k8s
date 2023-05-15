package com.allback.cygiadmin.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResDto {
    private long balanceId;
    private long concertId;
    private long userId;
    private long customer;
    private long proceed;
    private LocalDateTime createdDate;
}

