package com.allback.cygiconcert.mapper;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.entity.Concert;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConcertMapper {

    ConcertResDto toDto(Concert concert);
    Concert toEntity(ConcertReqDto concertReqDto);
    List<ConcertPageResDto> toDtoList(List<Concert> concertList);

}
