import React from "react";
import { useNavigate } from "react-router-dom";
import style from "./SideNav.module.css";

export default function SideNav() {
  const navigate = useNavigate();
  return (
    <div className={style.container}>
      <div className={style.menu_list}>
        <div
          className={style.total_menu}
          onClick={() => {
            navigate("total");
          }}
        >
          DASH BOARD
        </div>
        <div
          className={style.calculation_menu}
          onClick={() => {
            navigate("calculation-list");
          }}
        >
          정산 내역
        </div>
        <div
          className={style.user_menu}
          onClick={() => {
            navigate("user-list");
          }}
        >
          사용자 목록
        </div>
        <div
          className={style.reservation_menu}
          onClick={() => {
            navigate("reservation-list");
          }}
        >
          전체 예매 내역
        </div>
      </div>
    </div>
  );
}
