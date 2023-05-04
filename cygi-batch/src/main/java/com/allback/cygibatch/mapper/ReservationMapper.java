package com.allback.cygibatch.mapper;

import com.allback.cygibatch.dto.request.ReservationReqDto;
import com.allback.cygibatch.dto.response.ReservationResDto;
import com.allback.cygibatch.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

@Mapper(componentModel = "spring")
public interface ReservationMapper {
	@Mapping(target = "price", source = "price", defaultValue = "null")
	ReservationResDto toDto(Reservation reservation);
	@Mapping(target = "price", source = "price", defaultValue = "null")
	Reservation toEntity(ReservationReqDto reservationReqDto);

	@Mapping(target = "price", source = "price", defaultValue = "null")
	List<ReservationResDto> toDtoList(List<Reservation> reservationList);

}