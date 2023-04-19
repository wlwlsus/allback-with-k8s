import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import style from "./SeatList.module.css";

export default function SeatList() {
  const navigate = useNavigate();

  const data = [
    {
      seq: 0,
      title: "맘마미아!",
      location: "싸피콘서트 A홀",
      seelct_seat: "A-01",
      concert_start: "23/04/18 15시",
    },
  ];

  const [title, setTitle] = useState(data.title);
  const [location, setlocation] = useState(data.location);
  const [seat, setSeat] = useState();
  const [date, setDate] = useState(data.concert_start);

  console.log(title, location, date);

  return (
    <div className={style.container}>
      <div className={style.header}>
        <div className={style.title}>맘마미아!</div>
        <div className={style.information}>
          <div className={style.name}>공연일</div>
          <div className={style.content}>23/04/18 15시</div>
          <div className={style.name}>공연장소</div>
          <div className={style.content}>싸피콘서트 A홀</div>
          <div className={style.name}>선택 좌석</div>
          <div className={style.content}>{seat}</div>
        </div>
        <div className={style.reserve_btn}>
          <button
            onClick={() => {
              navigate(`reservation`);
            }}
          >
            예매하기
          </button>
        </div>
      </div>
      <div className={style.main}>
        <div className={style.blank} />
        <div className={style.stage_div}>STAGE</div>
        <div>좌석 리스트 출력할 div</div>
      </div>
    </div>
  );
}
