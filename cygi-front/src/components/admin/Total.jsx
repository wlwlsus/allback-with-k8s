import React from "react";
import style from "./Total.module.css";

export default function Total() {
  return (
    <div className={style.container}>
      <div className={style.calculation}>
        <div className={style.title}>
          <span>정산내역</span>
        </div>
        <div className={style.calculation_header}>
          <div className={style.date}>정산 날짜</div>
          <div className={style.user_id}>주최자 ID</div>
          <div className={style.account}>계좌번호</div>
          <div className={style.bank_name}>은행명</div>
          <div className={style.audience}>관객 수</div>
          <div className={style.profits}>수익금</div>
          <div className={style.fee}>수수료</div>
          <div className={style.final_profits}>최종 수익금</div>
        </div>
      </div>
      <div className={style.bottom_div}>
        <div className={style.user}>
          <div className={style.title}>
            <span>사용자 목록</span>
          </div>
          <div className={style.user_header}>
            <div className={style.user_id}>아이디</div>
            <div className={style.nickname}>닉네임</div>
            <div className={style.signup_date}>가입일</div>
            <div className={style.role}>역할</div>
          </div>
        </div>
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
    </div>
  );
}
