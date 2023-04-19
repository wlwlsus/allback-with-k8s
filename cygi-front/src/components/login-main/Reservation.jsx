import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import style from "./Reservation.module.css";
import Poster from "img/poster_detail.png";

export default function Reservation() {
  const navigate = useNavigate();

  const [isPointBtn, setIsPointBtn] = useState(false);
  const [isKakaoBtn, setIsKakaoBtn] = useState(false);

  const onClickBtn = (e) => {
    if (e === "point") {
      if (isKakaoBtn) setIsKakaoBtn(false);
      setIsPointBtn(!isPointBtn);
    } else {
      if (isPointBtn) setIsPointBtn(false);
      setIsKakaoBtn(!isKakaoBtn);
    }
  };

  return (
    <div>
      <div className={style.total}>
        <div className={style.left_div}>
          <div>
            <img className={style.poster_img} src={Poster} alt="" />
          </div>
          <div>
            <button
              className={style.btn}
              onClick={() => {
                navigate("/complete");
              }}
            >
              결제하기
            </button>
          </div>
        </div>
        <div className={style.right_div}>
          <div className={style.pay_type}>결제 수단</div>
          <div className={style.btn_type}>
            <div>
              <input
                className={style.radio_btn}
                type="radio"
                name="pay_type"
                id="point"
                value="point"
                onClick={(e) => {
                  onClickBtn(e.target.value);
                }}
              />
              <label className={style.radio_btn} htmlFor="point">
                포인트
              </label>
            </div>
            <div>
              <input
                className={style.radio_btn}
                type="radio"
                name="pay_type"
                id="kakao"
                value="kakao"
                onClick={(e) => {
                  onClickBtn(e.target.value);
                }}
              />
              <label className={style.radio_btn} htmlFor="kakao">
                카카오페이
              </label>
            </div>
          </div>
          <div className={style.contents}>
            <div className={style.name}>
              <div className={style.title}>공연명</div>
              <div className={style.content}>맘마미아!</div>
            </div>
            <div className={style.seat}>
              <div className={style.title}>좌석 번호</div>
              <div className={style.content}>A-01</div>
            </div>
            <div className={style.location}>
              <div className={style.title}>공연장</div>
              <div className={style.content}>싸피콘서트 A홀</div>
            </div>
            <div className={style.date}>
              <div className={style.title}>공연일</div>
              <div className={style.content}>2023-04-17 12:00</div>
            </div>
          </div>
          <div className={style.pay}>
            <div className={style.title}>총 결제 금액</div>
            <div className={style.content}>21,000원</div>
          </div>
        </div>
      </div>
    </div>
  );
}
