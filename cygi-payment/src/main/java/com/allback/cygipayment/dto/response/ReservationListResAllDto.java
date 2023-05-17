package com.allback.cygipayment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class ReservationListResAllDto {

    private int totalPages;
    private List<ReservationResAllDto> reservationResAllDtoPage;
}
