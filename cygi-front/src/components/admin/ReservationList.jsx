import React from "react";
import style from "./ReservationList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $ } from "util/axios";

export default function ReservationList() {
  // 전체 예매내역 조회
  const { isLoading, data } = useQuery(["reservation"], () =>
    $.get(`/dashboard/reservation`)
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
      </div>
    </div>
  );
}
