import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./SeatList.module.css";
import Poster from "img/poster_detail.png";
import { useQuery, useMutation } from "@tanstack/react-query";
import { $_concert } from "util/axios";

export default function SeatList() {
  const navigate = useNavigate();
  const location = useLocation();

  const [seat, setSeat] = useState();
  const [cols, setCols] = useState();
  const [rows, setRows] = useState();

  // 해당 공연장의 좌석 조회
  const { isLoading, data } = useQuery(
    [`seat-list_${location.state.concertId}`],
    () => $_concert.get(`/seat/${location.state.concertId}`)
  );

  const newData = {
    concertId: location.state.concertId,
    seat: seat,
  };

  //API_POST 함수
  const res_post = () => {
    return $_concert.post(`/seat`, newData);
  };

  // 예매하기 클릭 시 이벤트 발생
  const { mutate: onSelect } = useMutation(res_post, {
    onSuccess: (res) => {
      navigate(`reservation`, {
        state: {
          reservationId: res.data,
          title: location.state.title,
          seat: seat,
          location: location.state.location,
          endDate: location.state.endDate,
          price: location.state.price,
          image: location.state.image,
        },
      });
    },
  });

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

  const onClicked = (e) => {
    if (e === seat) setSeat();
    else setSeat(e);
  };

  //각 좌석의 btn 태그 생성
  const renderSeat = (row, col) => {
    const value = `${rowNo[row]}${col}`;

    return (
      <button
        key={value}
        value={value}
        className={
          value.indexOf(data.data.seatList) !== -1
            ? style.seat_reserved
            : value === seat
            ? style.seat_selected
            : style.seat
        }
        onClick={() => onClicked(value)}
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

  const onCheck = () => {
    if (seat === undefined || seat === null) {
      alert("좌석을 선택하여주세요.");
      return;
    }
    onSelect();
  };

  useEffect(() => {
    if (!isLoading) {
      setCols(data.data.col);
      setRows(data.data.row);
      console.log(data.data.seatList);
    }
  }, [isLoading]);

  return (
    <>
      {!isLoading && data && (
        <div className={style.container}>
          <div className={style.header}>
            <div className={style.title}>{location.state.title}</div>
            <div className={style.information}>
              <div className={style.name}>공연일</div>
              <div className={style.content}>
                {location.state.endDate.slice(0, 4)}년&nbsp;
                {location.state.endDate.slice(5, 7)}월&nbsp;
                {location.state.endDate.slice(8, 10)}일&nbsp;
                {location.state.endDate.slice(11, 13)}시&nbsp;
                {location.state.endDate.slice(14, 16)}분&nbsp;
              </div>
              <div className={style.name}>공연장소</div>
              <div className={style.content}>{location.state.location}</div>
              <div className={style.name}>선택 좌석</div>
              <div className={style.content}>{seat}</div>
            </div>
            <div className={style.reserve_btn}>
              <button
                onClick={() => {
                  onCheck();
                }}
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
