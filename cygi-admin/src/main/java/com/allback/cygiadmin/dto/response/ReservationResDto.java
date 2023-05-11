package com.allback.cygiadmin.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResDto {
    private long reservationId;
    private String title;
    private String status;
    private int price;
    private String seat;
    private String modifiedDate;
}
