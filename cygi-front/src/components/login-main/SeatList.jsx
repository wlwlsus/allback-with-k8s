import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import style from "./SeatList.module.css";
import Poster from "img/poster_detail.png";

export default function SeatList() {
  const navigate = useNavigate();

  const data = [
    {
      seq: 0,
      title: "맘마미아!",
      location: "싸피콘서트 A홀",
      seelct_seat: "A-01",
      concert_start: "23/04/18 15시",
      poster: Poster,
      price: 20000,
    },
  ];

  const [title, setTitle] = useState();
  const [location, setlocation] = useState();
  const [seat, setSeat] = useState();
  const [date, setDate] = useState();
  const [price, setPrice] = useState();
  const [poster, setPoster] = useState();

  const [isLoading, setIsloading] = useState(true);
  const rowNo = [
    "",
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
  ];

  let rows = 20;
  let cols = 50;

  const onSelect = (e) => {
    setSeat(e);
  };

  //각 좌석의 btn 태그 생성
  const renderSeat = (row, col) => {
    const value = `${rowNo[row]}${col}`;
    return (
      <button
        key={value}
        value={value}
        className={value === seat ? style.seat_selected : style.seat}
        onClick={() => onSelect(value)}
      ></button>
    );
  };

  // 각 행의 좌석 정보 호출
  const renderRow = (row) => {
    const seats = [];
    seats.push(
      <div key={row} className={style.seat_row_name}>
        <span>{rowNo[row]}행</span>
      </div>
    );
    for (let col = 1; col <= cols; ++col) {
      seats.push(renderSeat(row, col));
    }
    return (
      <div key={row} className={style.seat_row}>
        {seats}
      </div>
    );
  };

  // 전체 좌석정보 저장하는 배열
  const rowsElements = [];
  for (let row = 1; row <= rows; ++row) {
    rowsElements.push(renderRow(row));
  }

  useEffect(() => {
    async function fetchData() {
      setTitle(data[0].title);
      setPoster(data[0].poster);
      setlocation(data[0].location);
      setDate(data[0].concert_start);
      setIsloading(false);
      setPrice(data[0].price);
    }
    fetchData();
  }, []);
  console.log(title, date, location, seat, Poster);

  return (
    <>
      {!isLoading && (
        <div className={style.container}>
          <div className={style.header}>
            <div className={style.title}>{title}</div>
            <div className={style.information}>
              <div className={style.name}>공연일</div>
              <div className={style.content}>{date}</div>
              <div className={style.name}>공연장소</div>
              <div className={style.content}>{location}</div>
              <div className={style.name}>선택 좌석</div>
              <div className={style.content}>{seat}</div>
            </div>
            <div className={style.reserve_btn}>
              <button
                onClick={() => {
                  navigate(`reservation`, {
                    state: {
                      title: title,
                      seat: seat,
                      locate: location,
                      date: date,
                      price: price,
                      poster: poster,
                    },
                  });
                }}
                disabled={!seat ? true : false}
              >
                예매하기
              </button>
            </div>
          </div>
          <div className={style.main}>
            <div className={style.blank} />
            <div className={style.stage_div}>STAGE</div>
            <div className={style.seat_total}>{rowsElements}</div>
          </div>
        </div>
      )}
    </>
  );
}
