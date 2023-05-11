import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./ConcertDetail.module.css";
import PosterBackground from "img/poster.png";
import { useQuery } from "@tanstack/react-query";
import Loading from "components/common/Loading";
import { $ } from "util/axios";
import axios from "axios";

export default function ConcertDetail() {
  const navigate = useNavigate();
  const location = useLocation();

  const [check, setCheck] = useState(false);
  const [data, setData] = useState();

  // 대기열창 모달 & 순서 계산용 변수
  const [modalOpen, setModalOpen] = useState(false);
  const [offset, setOffset] = useState();
  const [committedOffset, setCommittedOffset] = useState();
  const [endOffset, setEndOffset] = useState();
  const [onoff, setOnoff] = useState(false);

  let nowTime = new Date();

  // 대기열 체크용 함수
  let interval;

  // 공연정보 조회(트래픽 많을 경우 대기열 생성)
  const { isLoading } = useQuery([`concert_${location.state.concertId}`], () =>
    $.get(`/concert-service/api/v1/concert/${location.state.concertId}`)
      .then((res) => {
        setCheck(true);
        console.log(res.data);
        setData(res);
        setModalOpen(false);
      })
      .catch((err) => {
        setModalOpen(true);
        setOffset(err.response.data.offset);
        setCommittedOffset(err.response.data.committedOffset);
        setEndOffset(err.response.data.endOffset);

        interval = setInterval(() => {
          console.log("재요정 감지");
          $.get(`/concert-service/api/v1/concert/${location.state.concertId}`, {
            headers: {
              "KAFKA.UUID": err.response.data.uuid,
              "KAFKA.PARTITION": err.response.data.partition,
              "KAFKA.OFFSET": err.response.data.offset,
            },
          })
            .then((res) => {
              setCheck(true);
              console.log("307 에러 탈출");
              console.log(res.data);
              setData(res);
              setModalOpen(false);
            })
            .catch((err) => {
              setCommittedOffset(err.response.data.committedOffset);
              setEndOffset(err.response.data.endOffset);
              console.log(err);
            });
        }, 1000);
        console.log("인터벌 탈출");
      })
  );

  const onCheck = () => {
    $.get(`/concert-service/api/v1/seat/rest/${location.state.concertId}`)
      .then((res) => {
        setCheck(true);
        setModalOpen(false);
        if (res.data.rest === 0 || nowTime >= new Date(data.data.endDate)) {
          alert("마감되었습니다. 다른 공연을 예매해주세요.");
          navigate("/home");
        } else {
          onSelect(data.data.concertId);
        }
      })
      .catch((err) => {
        setModalOpen(true);
        setOffset(err.response.data.offset);
        setCommittedOffset(err.response.data.committedOffset);
        setEndOffset(err.response.data.endOffset);

        interval = setInterval(() => {
          $.get(
            `/concert-service/api/v1/seat/rest/${location.state.concertId}`,
            {
              headers: {
                "KAFKA.UUID": err.response.data.uuid,
                "KAFKA.PARTITION": err.response.data.partition,
                "KAFKA.OFFSET": err.response.data.offset,
              },
            }
          )
            .then((res) => {
              setCheck(true);
              setModalOpen(false);
              if (
                res.data.rest === 0 ||
                nowTime >= new Date(data.data.endDate)
              ) {
                alert("마감되었습니다. 다른 공연을 예매해주세요.");
                navigate("/home");
              } else {
                onSelect(data.data.concertId);
              }
            })
            .catch((err) => {
              setCommittedOffset(err.response.data.committedOffset);
              setEndOffset(err.response.data.endOffset);
            });
        }, 1000);
      });
  };

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
    if (!modalOpen) {
      console.log(interval);
      clearInterval(interval);
      navigate("../../");
    }
  }, [modalOpen]);

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
                    onCheck();
                  }}
                >
                  예매하기
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
      {modalOpen && (
        <Loading
          setModalOpen={setModalOpen}
          setOnoff={setOnoff}
          offset={offset}
          committedOffset={committedOffset}
          endOffset={endOffset}
          interval={interval}
        />
      )}
    </>
  );
}
