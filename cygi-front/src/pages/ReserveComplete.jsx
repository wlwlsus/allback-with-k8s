import React from "react";
import SubHeader from "../components/common/SubHeader";
import style from "./ReserveComplete.module.css";
import Poster from "img/poster.png";

export default function ReserveComplete() {
  return (
    <div className={style.background}>
      <SubHeader />
      <div className={style.background_img}>
        <img src={Poster} alt="" />
      </div>
      <div className={style.complete_text}>예매가 완료되었습니다.</div>
      <div className={style.ticket_information}>
        <div className={style.contents}>
          <div className={style.name}>맘마미아!</div>
          <div className={style.reserve}>
            <div className={style.title}>예약 번호</div>
            <div className={style.content}>0312321321</div>
          </div>
          <div className={style.seat}>
            <div className={style.title}>좌석 번호</div>
            <div className={style.content}>A-01</div>
          </div>
          <div className={style.pay}>
            <div className={style.title}>결제 금액</div>
            <div className={style.content}>55,000원</div>
          </div>
          <div className={style.date}>
            <div className={style.title}>공연일</div>
            <div className={style.content}>2023-04-17 12:00</div>
          </div>
        </div>
      </div>
    </div>
  );
}
