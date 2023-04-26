import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./Reservation.module.css";

export default function Reservation() {
  const navigate = useNavigate();
  const location = useLocation();

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

  // 페이지 벗어날시 예약상태 초기화
  const preventGoBack = (e) => {
    console.log(e);
  };

  // 새로고침 막기 변수
  const preventClose = (e) => {
    e.preventDefault();
    e.returnValue = "";
  };

  useEffect(() => {
    (() => {
      window.addEventListener("popstate", preventGoBack);
      window.addEventListener("beforeunload", preventClose);
    })();

    return () => {
      window.removeEventListener("popstate", preventGoBack);
      window.removeEventListener("beforeunload", preventClose);
    };
  }, []);

  return (
    <div>
      <div className={style.total}>
        <div className={style.left_div}>
          <div>
            <img
              className={style.poster_img}
              src={location.state.image}
              alt=""
            />
          </div>
          <div>
            <button
              className={style.btn}
              onClick={() => {
                navigate("/complete", {
                  state: {
                    title: location.state.title,
                    seat: location.state.seat,
                    endDate: location.state.endDate,
                    price: location.state.price,
                  },
                });
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
              <div className={style.content}>{location.state.title}</div>
            </div>
            <div className={style.seat}>
              <div className={style.title}>좌석 번호</div>
              <div className={style.content}>
                {location.state.seat[0]}-{location.state.seat.slice(1)}
              </div>
            </div>
            <div className={style.location}>
              <div className={style.title}>공연장</div>
              <div className={style.content}>{location.state.location}</div>
            </div>
            <div className={style.date}>
              <div className={style.title}>공연일</div>
              <div className={style.content}>{location.state.endDate}</div>
            </div>
          </div>
          <div className={style.pay}>
            <div className={style.title}>총 결제 금액</div>
            <div className={style.content}>{location.state.price}원</div>
          </div>
        </div>
      </div>
    </div>
  );
}
