package com.allback.cygipayment.mapper;

import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.dto.response.ReservationResDto.ReservationResDtoBuilder;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.entity.Reservation.ReservationBuilder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-05T15:02:41+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationResDto toDto(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationResDtoBuilder reservationResDto = ReservationResDto.builder();

        reservationResDto.price( reservation.getPrice() );
        reservationResDto.reservationId( reservation.getReservationId() );
        reservationResDto.status( reservation.getStatus() );
        reservationResDto.seat( reservation.getSeat() );
        if ( reservation.getModifiedDate() != null ) {
            reservationResDto.modifiedDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( reservation.getModifiedDate() ) );
        }

        return reservationResDto.build();
    }

    @Override
    public Reservation toEntity(ReservationReqDto reservationReqDto) {
        if ( reservationReqDto == null ) {
            return null;
        }

        ReservationBuilder reservation = Reservation.builder();

        reservation.price( reservationReqDto.getPrice() );
        reservation.concertId( reservationReqDto.getConcertId() );
        reservation.userId( reservationReqDto.getUserId() );
        reservation.status( reservationReqDto.getStatus() );
        reservation.seat( reservationReqDto.getSeat() );

        return reservation.build();
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
