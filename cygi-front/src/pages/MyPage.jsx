import React, { useEffect, useState } from "react";
import SubHeader from "../components/common/SubHeader";
import style from "./MyPage.module.css";
import Profile from "img/profile.png";
import KakaoPay from "img/payment_icon_yellow_small.png";
import axios from "axios";
import { useQuery, useMutation } from "@tanstack/react-query";
import { userId, userNick, createdTime, userPoint } from "../util/store";
import { useRecoilState, useRecoilValue } from "recoil";
import { $ } from "util/axios";
import { useNavigate } from "react-router-dom";
import { history } from "components/login-main/history";

export default function MyPage() {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [reservationId, setReservationId] = useState();

  const id = useRecoilValue(userId);
  const nickName = useRecoilValue(userNick);
  const createTime = useRecoilValue(createdTime);
  const [point, setPoint] = useRecoilState(userPoint);

  //예약 목록 조회
  const { isLoading, data, refetch } = useQuery(["mypage"], () =>
    $.get(`/payment-service/api/v1/reservation/id/${id}?size=10&page=${page}`)
  );

  // API_PUT 함수
  const res_put = () => {
    return $.put(`/payment-service/api/v1/reservation/refund/${reservationId}`);
  };

  // 예약 취소 & 환불
  const { mutate: onRefund } = useMutation(res_put, {
    onSuccess: () => {
      alert("환불이 완료되었습니다.");
      refetch();
      window.location.reload();
    },
    onError: () => {
      alert("오류가 발생했습니다.");
      setReservationId();
      window.location.reload();
    },
  });

  const paymentData = {
    cid: "TC0ONETIME",
    partner_order_id: "1000",
    partner_user_id: nickName,
    item_name: "포인트충전",
    quantity: 1,
    total_amount: 100000,
    tax_free_amount: 0,
    approval_url: "http://allback.site/success",
    cancel_url: "http://allback.site/home",
    fail_url: "http://allback.site/home",
  };

  const preparePayment = async (paymentData) => {
    const response = await $.post(
      "/payment-service/api/v1/reservation/charge",
      paymentData
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

  useEffect(() => {
    if (reservationId > 0) onRefund();
  }, [reservationId]);

  // 뒤로가기 이벤트 감지
  // useEffect(() => {
  //   const listenBackEvent = () => {
  //     navigate("/");
  //   };
  //   const historyEvent = history.listen(({ action }) => {
  //     if (action === "POP") {
  //       listenBackEvent();
  //     }
  //   });
  //   return historyEvent;
  // }, []);

  useEffect(() => {
    if (!isLoading) console.log(data);
  }, [isLoading]);

  return (
    <div className={style.container}>
      <SubHeader />
      <div className={style.inner_container}>
        <div className={style.profile}>
          <div className={style.picture}>
            <img className={style.picture_img} src={Profile} alt="" />
          </div>
          <div className={style.profile_name}>
            <span>{nickName}</span>님
          </div>
          <div className={style.profile_point}>{point}원</div>
          <div className={style.profile_signup_title}>가입일</div>
          <div className={style.profile_signup_date}>{createTime}</div>
          <div>
            <div className={style.kakao_pay_title}>충전하기</div>
            <img
              className={style.kakao_pay_btn}
              src={KakaoPay}
              alt=""
              onClick={() => {
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
              <div className={style.seat_num}>좌석</div>
              <div className={style.pay}>결제 금액</div>
              <div className={style.status}>예약 상태</div>
              <div className={style.cancel}>예약 취소</div>
            </div>
            {!isLoading &&
              data.data.reservationResAllDtoPage.map((content) => {
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
                    key={content.reservationId}
                    className={style.reservation_list}
                  >
                    <div className={style.reserve_date}>{date}</div>
                    <div className={style.concert_name}>{content.title}</div>
                    <div className={style.seat_num}>
                      {content.seat[0]}-{content.seat.slice(1)}
                    </div>
                    <div className={style.pay}>{content.price}원</div>
                    <div className={style.status}>{content.status}</div>
                    <div></div>
                    {content.status === "예약완료" ? (
                      <div
                        className={style.cancel_btn}
                        onClick={() => {
                          if (window.confirm("예약을 취소하시겠습니까?")) {
                            setReservationId(content.reservationId);
                          }
                        }}
                      >
                        예약 취소
                      </div>
                    ) : (
                      <div></div>
                    )}
                  </div>
                );
              })}
          </div>
        </div>
      </div>
    </div>
  );
}
