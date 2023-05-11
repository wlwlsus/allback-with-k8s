import React from "react";
import style from "./ReservationList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $_admin } from "util/axios";

export default function ReservationList() {
  // 전체 예매내역 조회
  const { data: reservation } = useQuery(["reservation"], () =>
    $_admin.get(`/dashboard/reservation?page=${1}&size=10`)
  );

  return (
    <div className={style.container}>
      <div className={style.reservation}>
        <div className={style.title}>
          <span>전체 예매 내역</span>
        </div>
        <div className={style.reservation_header}>
          <div className={style.reserve_date}>예매 일자</div>
          <div className={style.concert_name}>공연명</div>
          <div className={style.reservationist}>예매자</div>
          <div className={style.seat_num}>좌석 번호</div>
          <div className={style.status}>상태</div>
        </div>
        {reservation &&
          reservation.data.reservationResAllDtoPage.map((content) => {
            let date =
              content.modifiedDate.slice(0, 4) +
              "." +
              content.modifiedDate.slice(5, 7) +
              "." +
              content.modifiedDate.slice(8, 10) +
              "." +
              content.modifiedDate.slice(11, 13) +
              ":" +
              content.modifiedDate.slice(14, 16) +
              "";
            return (
              <div
                className={style.reservation_body}
                key={content.reservationId}
              >
                <div className={style.reserve_date}>{date}</div>
                <div className={style.concert_name}>{content.title}</div>
                <div className={style.reservationist}>{content.userId}</div>
                <div className={style.seat_num}>{content.seat}</div>
                <div className={style.status}>{content.status}</div>
              </div>
            );
          })}
      </div>
    </div>
  );
}
