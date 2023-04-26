import React from "react";
import SubHeader from "../components/common/SubHeader";
import style from "./MyPage.module.css";
import Profile from "img/profile.png";
import KakaoPay from "img/payment_icon_yellow_small.png";
import axios from "axios";

export default function MyPage() {
  const paymentData = {
    cid: "TC0ONETIME",
    partner_order_id: "1001",
    partner_user_id: "user01",
    item_name: "테스트 상품",
    quantity: 1,
    total_amount: 10000,
    tax_free_amount: 0,
    approval_url: "http://localhost:3000/success",
    cancel_url: "http://localhost:3000/home",
    fail_url: "http://localhost:3000/home",
  };

  const preparePayment = async (paymentData) => {
    const headers = {
      "Content-Type": "application/json; charset=UTF-8",
    };
    const response = await axios.post(
      "http://localhost:8001/api/v1/reservation/charge",
      paymentData,
      { headers }
    );

    if (response.status === 200) {
      // PC에서 결제 진행
      const tid = response.data.result.tid;
      localStorage.setItem("tid", tid);
      window.location.href = response.data.result.next_redirect_pc_url;
    } else {
      alert("문제가 발생하였습니다.");
    }
    return response.data;
  };

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
          <div>
            <div className={style.kakao_pay_title}>충전하기</div>
            <img
              className={style.kakao_pay_btn}
              src={KakaoPay}
              alt=""
              onClick={() => {
                console.log("클릭!");
                preparePayment(paymentData);
                // navigate("../home");
              }}
            />
          </div>
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
