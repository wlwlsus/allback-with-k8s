import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./ConcertDetail.module.css";
import PosterBackground from "img/poster.png";
import { useQuery } from "@tanstack/react-query";
import { $_concert } from "util/axios";

export default function ConcertDetail() {
  const navigate = useNavigate();
  const location = useLocation();

  const { isLoading, data } = useQuery(
    [`concert_${location.state.concertId}`],
    () => $_concert.get(`/concert/${location.state.concertId}`)
  );

  const onSelect = (id) => {
    navigate(`seat`, {
      state: {
        concertId: location.state.concertId,
        title: data.data.title,
        location: data.data.location,
        endDate: data.data.endDate,
        image: data.data.image,
        price: data.data.price,
      },
    });
  };
  useEffect(() => {
    if (!isLoading) console.log(data);
  }, [isLoading]);

  return (
    <>
      {!isLoading && data && (
        <div className={style.container}>
          <div className={style.concert_background}>
            <img src={PosterBackground} alt="" />
          </div>
          <div className={style.details}>
            <div className={style.top_details}>
              <div className={style.top_detail_img}>
                <img
                  className={style.poster_detail}
                  src={data.data.image}
                  alt=""
                />
              </div>
              <div className={style.contents}>
                <div className={style.concert_title}>
                  <span>{data.data.title}</span>
                </div>
                <div className={style.name}>
                  <div className={style.title}>가격</div>
                  <div className={style.content}>{data.data.price}원</div>
                </div>
                <div className={style.seat}>
                  <div className={style.title}>공연장</div>
                  <div className={style.content}>{data.data.location}</div>
                </div>
                <div className={style.location}>
                  <div className={style.title}>예매 시작일</div>
                  <div className={style.content}>
                    {data.data.startDate.slice(0, 4)}년&nbsp;
                    {data.data.startDate.slice(5, 7)}월&nbsp;
                    {data.data.startDate.slice(8, 10)}일&nbsp;
                    {data.data.startDate.slice(11, 13)}시&nbsp;
                    {data.data.startDate.slice(14, 16)}분&nbsp;
                  </div>
                </div>
                <div className={style.date}>
                  <div className={style.title}>공연 시작일</div>
                  <div className={style.content}>
                    {data.data.endDate.slice(0, 4)}년&nbsp;
                    {data.data.endDate.slice(5, 7)}월&nbsp;
                    {data.data.endDate.slice(8, 10)}일&nbsp;
                    {data.data.endDate.slice(11, 13)}시&nbsp;
                    {data.data.endDate.slice(14, 16)}분&nbsp;
                  </div>
                </div>
              </div>
            </div>
            <div className={style.bottom_details}>
              <div className={style.detail_story}>{data.data.content}</div>
              <div className={style.reserve_btn}>
                <button
                  onClick={() => {
                    onSelect(data.data.concertId);
                  }}
                >
                  예매하기
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
