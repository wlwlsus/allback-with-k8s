import React from "react";
import { useNavigate } from "react-router-dom";
import style from "./Reservation.module.css";
import Poster from "img/poster_detail.png";

export default function Reservation() {
  const navigate = useNavigate();
  return (
    <div>
      <div className={style.total}>
        <div className={style.left_div}>
          <img className={style.poster_img} src={Poster} alt="" />
          <button
            onClick={() => {
              navigate("/complete");
            }}
          >
            결제하기
          </button>
        </div>
        <div className={style.right_div}></div>
      </div>
    </div>
  );
}
