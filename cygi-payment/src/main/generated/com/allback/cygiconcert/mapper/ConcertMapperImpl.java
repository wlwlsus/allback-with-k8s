package com.allback.cygiconcert.mapper;

import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto.ConcertPageResDtoBuilder;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto.ConcertResDtoBuilder;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Concert.ConcertBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-08T20:55:54+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Amazon.com Inc.)"
)
@Component
public class ConcertMapperImpl implements ConcertMapper {

    @Override
    public ConcertResDto toDto(Concert concert) {
        if ( concert == null ) {
            return null;
        }

        ConcertResDtoBuilder concertResDto = ConcertResDto.builder();

        concertResDto.concertId( concert.getConcertId() );
        concertResDto.userId( concert.getUserId() );
        concertResDto.title( concert.getTitle() );
        concertResDto.content( concert.getContent() );
        concertResDto.image( concert.getImage() );
        concertResDto.startDate( concert.getStartDate() );
        concertResDto.endDate( concert.getEndDate() );

        return concertResDto.build();
    }

    @Override
    public Concert toEntity(ConcertReqDto concertReqDto) {
        if ( concertReqDto == null ) {
            return null;
        }

        ConcertBuilder concert = Concert.builder();

        concert.userId( concertReqDto.getUserId() );
        concert.title( concertReqDto.getTitle() );
        concert.content( concertReqDto.getContent() );
        concert.image( concertReqDto.getImage() );
        concert.startDate( concertReqDto.getStartDate() );
        concert.endDate( concertReqDto.getEndDate() );

        return concert.build();
    }

    @Override
    public List<ConcertPageResDto> toDtoList(List<Concert> concertList) {
        if ( concertList == null ) {
            return null;
        }

        List<ConcertPageResDto> list = new ArrayList<ConcertPageResDto>( concertList.size() );
        for ( Concert concert : concertList ) {
            list.add( concertToConcertPageResDto( concert ) );
        }

        return list;
    }

    protected ConcertPageResDto concertToConcertPageResDto(Concert concert) {
        if ( concert == null ) {
            return null;
        }

        ConcertPageResDtoBuilder concertPageResDto = ConcertPageResDto.builder();

        concertPageResDto.concertId( concert.getConcertId() );
        concertPageResDto.title( concert.getTitle() );
        concertPageResDto.image( concert.getImage() );
        concertPageResDto.endDate( concert.getEndDate() );

        return concertPageResDto.build();
    }
}
