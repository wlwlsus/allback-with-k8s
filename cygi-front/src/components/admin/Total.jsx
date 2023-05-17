import React, { useEffect } from "react";
import style from "./Total.module.css";
import { useQuery } from "@tanstack/react-query";
import { $ } from "util/axios";

export default function Total() {
  // 정산 내역 조회
  const { data: balance } = useQuery(["balance"], () =>
    $.get(`/admin-service/api/v1/dashboard/balance?page=1&size=10`)
  );

  // 사용자 목록 조회
  const { data: user } = useQuery(["user"], () =>
    $.get(`/admin-service/api/v1/dashboard/user?page=1`)
  );

  // 전체 예매내역 조회
  const { data: reservation } = useQuery(["reservation"], () =>
    $.get(`/admin-service/api/v1/dashboard/reservation?page=1&size=10`)
  );

  return (
    <div className={style.container}>
      <div className={style.calculation}>
        <div className={style.title}>
          <span>정산내역</span>
        </div>
        <div className={style.calculation_header}>
          <div className={style.date}>정산 날짜</div>
          <div className={style.user_id}>주최자 ID</div>
          <div className={style.audience}>관객 수</div>
          <div className={style.final_profits}>최종 수익금</div>
        </div>
        {balance &&
          balance.data.content.map((content, index) => {
            let date =
              content.createdDate.slice(2, 4) +
              "." +
              content.createdDate.slice(5, 7) +
              "." +
              content.createdDate.slice(8, 10) +
              "." +
              content.createdDate.slice(11, 13) +
              ":" +
              content.createdDate.slice(14, 16) +
              "";
            return (
              <div className={style.calculation_body} key={index}>
                <div className={style.date}>{date}</div>
                <div className={style.user_id}>{content.userId}</div>
                <div className={style.audience}>{content.customer}</div>
                <div className={style.final_profits}>{content.proceed}</div>
              </div>
            );
          })}
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
            <div className={style.role}>권한</div>
          </div>
          {user &&
            user.data.content.map((content) => {
              let date =
                content.createDate.slice(2, 4) +
                "." +
                content.createDate.slice(5, 7) +
                "." +
                content.createDate.slice(8, 10) +
                "." +
                content.createDate.slice(11, 13) +
                ":" +
                content.createDate.slice(14, 16) +
                "";
              return (
                <div className={style.user_body} key={content.uuid}>
                  <div className={style.user_id}>{content.email}</div>
                  <div className={style.nickname}>{content.nickname}</div>
                  <div className={style.signup_date}>{date}</div>
                  <div className={style.role}>
                    {content.role === "ROLE_USER" ? "일반" : "관리자"}
                  </div>
                </div>
              );
            })}
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
          {reservation &&
            reservation.data.reservationResAllDtoPage.map((content) => {
              let date =
                content.modifiedDate.slice(2, 4) +
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
                  <div className={style.seat_num}>
                    {content.seat[0]}-{content.seat.slice(1)}
                  </div>
                  <div className={style.status}>{content.status}</div>
                </div>
              );
            })}
        </div>
      </div>
    </div>
  );
}
