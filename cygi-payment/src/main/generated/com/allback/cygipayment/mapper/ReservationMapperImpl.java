package com.allback.cygipayment.mapper;

import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.entity.Reservation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-28T16:08:45+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationResDto toDto(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationResDto reservationResDto = new ReservationResDto();

        reservationResDto.setPrice( reservation.getPrice() );
        reservationResDto.setConcertId( reservation.getConcertId() );
        reservationResDto.setStageId( reservation.getStageId() );
        reservationResDto.setUserId( reservation.getUserId() );
        reservationResDto.setStatus( reservation.getStatus() );
        reservationResDto.setSeat( reservation.getSeat() );

        return reservationResDto;
    }

    @Override
    public Reservation toEntity(ReservationReqDto reservationReqDto) {
        if ( reservationReqDto == null ) {
            return null;
        }

        Reservation reservation = new Reservation();

        reservation.setPrice( reservationReqDto.getPrice() );
        if ( reservationReqDto.getConcertId() != null ) {
            reservation.setConcertId( reservationReqDto.getConcertId() );
        }
        if ( reservationReqDto.getStageId() != null ) {
            reservation.setStageId( reservationReqDto.getStageId() );
        }
        if ( reservationReqDto.getUserId() != null ) {
            reservation.setUserId( reservationReqDto.getUserId() );
        }
        reservation.setStatus( reservationReqDto.getStatus() );
        reservation.setSeat( reservationReqDto.getSeat() );

        return reservation;
    }

    @Override
    public List<ReservationResDto> toDtoList(List<Reservation> reservationList) {
        if ( reservationList == null ) {
            return null;
        }

        List<ReservationResDto> list = new ArrayList<ReservationResDto>( reservationList.size() );
        for ( Reservation reservation : reservationList ) {
            list.add( toDto( reservation ) );
        }

        return list;
    }
}
