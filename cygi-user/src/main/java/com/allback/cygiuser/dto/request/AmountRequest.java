package com.allback.cygiuser.dto.request;

import lombok.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AmountRequest {
	private long userId;
	private int amount;
}