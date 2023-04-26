import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Poster from "img/poster_detail.png";
import style from "./ConcertList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $_concert } from "util/axios";

export default function ConcertList() {
  const navigate = useNavigate();

  const [page, setPage] = useState(1);

  const { isLoading, data } = useQuery(["concert"], () =>
    $_concert.get(`/concert?page=${page}`)
  );

  const onSelect = (id) => {
    navigate(`detail/${id}`, {
      state: {
        concertId: id,
      },
    });
  };

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
          data &&
          data.data.map((content) => {
            let date =
              content.endDate.slice(0, 4) +
              "/" +
              content.endDate.slice(5, 7) +
              "/" +
              content.endDate.slice(8, 10) +
              " " +
              content.endDate.slice(11, 13) +
              "시" +
              content.endDate.slice(14, 16) +
              "분";
            return (
              <div
                key={content.concertId}
                className={style.card}
                onClick={() => {
                  onSelect(content.concertId);
                }}
              >
                <div className={style.poster_url}>
                  <img src={content.image} alt="" />
                </div>
                <div className={style.content_title}>{content.title}</div>
                <div className={style.contents}>
                  <div className={style.name}>
                    <div className={style.title}>잔여좌석</div>
                    <div className={style.content}>
                      {content.rest} / {content.all}
                    </div>
                  </div>
                  <div className={style.seat}>
                    <div className={style.title}>예매마감일</div>
                    <div className={style.content}>{date}</div>
                  </div>
                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
}
