import React, { useEffect } from "react";
import SubHeader from "../components/common/SubHeader";
import style from "./ReserveComplete.module.css";
import { useLocation, useNavigate } from "react-router-dom";
import { history } from "../components/login-main/history";
import Poster from "img/poster.png";
import { reservation, userId, userPoint } from "util/store";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { $ } from "util/axios";
import { useQuery } from "@tanstack/react-query";

export default function ReserveComplete() {
  const location = useLocation();
  const navigate = useNavigate();
  const setReservationInfo = useSetRecoilState(reservation);
  const id = useRecoilValue(userId);
  const [point, setPoint] = useRecoilState(userPoint);

  const { isLoading, data: pointData } = useQuery(["getPoint"], () =>
    $.get(`/user-service/api/v1/user/point?id=${id}`)
  );

  useEffect(() => {
    if (!isLoading) setPoint(pointData.data);
  }, [isLoading]);

  useEffect(() => {
    setReservationInfo({
      title: "",
      reservationId: "",
      seat: "",
      price: 0,
      date: "",
    });
  }, []);

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

  return (
    <>
      {!isLoading && (
        <div className={style.background}>
          <SubHeader />
          <div className={style.background_img}>
            <img src={Poster} alt="" />
          </div>
          <div className={style.complete_text}>예매가 완료되었습니다.</div>
          <div className={style.ticket_information}>
            <div className={style.contents}>
              <div className={style.name}>{location.state.title}</div>
              <div className={style.reserve}>
                <div className={style.title}>예약 번호</div>
                <div className={style.content}>
                  {location.state.reservationId}
                </div>
              </div>
              <div className={style.seat}>
                <div className={style.title}>좌석 번호</div>
                <div className={style.content}>
                  {location.state.seat[0]}-{location.state.seat.slice(1)}
                </div>
              </div>
              <div className={style.pay}>
                <div className={style.title}>결제 금액</div>
                <div className={style.content}>{location.state.price}원</div>
              </div>
              <div className={style.date}>
                <div className={style.title}>공연일</div>
                <div className={style.content}>
                  {location.state.endDate.slice(0, 4)}년&nbsp;
                  {location.state.endDate.slice(5, 7)}월&nbsp;
                  {location.state.endDate.slice(8, 10)}일&nbsp;
                  {location.state.endDate.slice(11, 13)}시&nbsp;
                  {location.state.endDate.slice(14, 16)}분&nbsp;
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
