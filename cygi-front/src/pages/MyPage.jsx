import React from "react";
import SubHeader from "../components/common/SubHeader";
import style from "./MyPage.module.css";
import Profile from "img/profile.png";

export default function MyPage() {
  return (
    <div className={style.container}>
      <SubHeader />
      <div className={style.inner_container}>
        <div className={style.profile}>
          <div className={style.picture}>
            <img className={style.picture_img} src={Profile} alt="" />
          </div>
          <div className={style.profile_name}>
            <span>홍길동</span>님
          </div>
          <div className={style.profile_point}>500,000원</div>
          <div className={style.profile_signup_title}>가입일</div>
          <div className={style.profile_signup_date}>2023-04-11</div>
        </div>
        <div className={style.reserve_list}>
          <div className={style.reservation}>
            <div className={style.title}>
              <span>마이 페이지</span>
            </div>
            <div className={style.reservation_header}>
              <div className={style.reserve_date}>예매 일자</div>
              <div className={style.concert_name}>공연명</div>
              <div className={style.reservationist}>좌석</div>
              <div className={style.seat_num}>결제 금액</div>
              <div className={style.status}>예약 상태</div>
              <div className={style.cancel}>예약 취소</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
