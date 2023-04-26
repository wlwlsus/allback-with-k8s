import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Poster from "img/poster_detail.png";
import style from "./ConcertList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $ } from "util/axios";

export default function ConcertList() {
  const navigate = useNavigate();
  const { isLoading, isError, data } = useQuery(["concert"], async () => {
    // async 키워드 추가
    const response = await $.get(`/concert?page=10`); // await 키워드 추가
    return response.data; // 실제 데이터가 들어있는 response.data를 반환하도록 수정
  });

  const datas = [
    {
      seq: 0,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 1,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 2,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 3,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 4,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 5,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 6,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 7,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 8,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
    {
      seq: 9,
      title: "맘마미아!",
      poster_url: Poster,
      total_seat: 100,
      remain_seat: 80,
      ticket_start: "23/04/18 15시",
    },
  ];

  console.log(data);

  return (
    <div className={style.total}>
      <div className={style.header}>공연 목록</div>
      <div className={style.descript}>
        원하시는 공연을 선택하고 좌석을 예매해보세요. 모든 좌석이 매진되기 전에
        서두르세요! <br />
        결제는 CAN YOU GET IT 포인트 또는 카카오 페이를 통해 이루어집니다.
      </div>
      <div className={style.container}>
        {!isLoading &&
          data.data.map((content) => {
            return (
              <div
                key={content.seq}
                className={style.card}
                onClick={() => {
                  navigate(`detail/${1}`);
                }}
              >
                <div className={style.poster_url}>
                  <img src={content.poster_url} alt="" />
                </div>
                <div className={style.content_title}>{content.title}</div>
                <div className={style.contents}>
                  <div className={style.name}>
                    <div className={style.title}>잔여좌석</div>
                    <div className={style.content}>
                      {content.remain_seat} / {content.total_seat}
                    </div>
                  </div>
                  <div className={style.seat}>
                    <div className={style.title}>예매시작일</div>
                    <div className={style.content}>{content.ticket_start}</div>
                  </div>
                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
}
