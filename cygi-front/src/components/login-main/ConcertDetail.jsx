import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./ConcertDetail.module.css";
import PosterBackground from "img/poster.png";
import Loading from "components/common/Loading";
import { $ } from "util/axios";
import { kafka, stopRecur } from "util/store";
import { useRecoilState, useSetRecoilState } from "recoil";
import { isModalOpen } from "util/store";

export default function ConcertDetail() {
  const navigate = useNavigate();
  const location = useLocation();

  const [check, setCheck] = useState(false);
  const [data, setData] = useState();
  const [isLoading, setIsLoading] = useState();
  const setKafka = useSetRecoilState(kafka);
  const [onModal, setOnModal] = useRecoilState(isModalOpen);

  // 대기열창 모달 & 순서 계산용 변수
  const [modalOpen, setModalOpen] = useState(false);
  const [offset, setOffset] = useState();
  const [committedOffset, setCommittedOffset] = useState();
  const [endOffset, setEndOffset] = useState();

  let nowTime = new Date();

  // 공연정보 조회
  const getConcert = (val) => {
    setIsLoading(true);
    $.get(`/concert-service/api/v1/concert/${location.state.concertId}`, {
      headers: {
        "KAFKA.PASS": "pass",
      },
    }).then((res) => {
      setIsLoading(false);
      setData(res);
    });
  };

  const onCheck = () => {
    $.get(`/concert-service/api/v1/seat/rest/${location.state.concertId}`)
      .then((res) => {
        if (res.data.rest === 0 || nowTime >= new Date(data.data.endDate)) {
          alert("마감되었습니다. 다른 공연을 예매해주세요.");
          navigate("/home");
        } else {
          onSelect(data.data.concertId);
        }
      })
      .catch((err) => {
        setModalOpen(true);
        setOnModal(true);
        setOffset(err.response.data.offset);
        setCommittedOffset(err.response.data.committedOffset);
        setEndOffset(err.response.data.endOffset);
        setKafka({
          uuid: err.response.data.uuid,
          partition: err.response.data.partition,
          offset: err.response.data.offset,
        });

        setTimeout(() => {
          onCheck2(err);
        }, 1000);
      });
  };

  const onCheck2 = (val) => {
    $.get(`/concert-service/api/v1/seat/rest/${location.state.concertId}`, {
      headers: {
        "KAFKA.UUID": val.response.data.uuid,
        "KAFKA.PARTITION": val.response.data.partition,
        "KAFKA.OFFSET": val.response.data.offset,
      },
    })
      .then((res) => {
        setCheck(true);
        setModalOpen(false);
        setOnModal(false);
        if (res.data.rest === 0 || nowTime >= new Date(data.data.endDate)) {
          alert("마감되었습니다. 다른 공연을 예매해주세요.");
          navigate("/home");
        } else {
          onSelect(data.data.concertId);
        }
      })
      .catch((err) => {
        setOffset(err.response.data.offset);
        setCommittedOffset(err.response.data.committedOffset);
        setEndOffset(err.response.data.endOffset);
        console.log("재요청중");
        console.log(kafka.quit);

        setTimeout(() => {
          onCheck2(err);
        }, [1000]);
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
    getConcert();
  }, []);

  return (
    <>
      {!isLoading && data && (
        <div className={onModal ? style.disbled : style.container}>
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
                  <div className={style.title}>포인트</div>
                  <div className={style.content}>{data.data.price}P</div>
                </div>
                <div className={style.seat}>
                  <div className={style.title}>공연장</div>
                  <div className={style.content}>{data.data.location}</div>
                </div>
                <div className={style.location}>
                  <div className={style.title}>예매 시작일</div>
                  <div className={style.content}>
                    {data.data.startDate.slice(2, 4)}년&nbsp;
                    {data.data.startDate.slice(5, 7)}월&nbsp;
                    {data.data.startDate.slice(8, 10)}일&nbsp;
                    {data.data.startDate.slice(11, 13)}시&nbsp;
                  </div>
                </div>
                <div className={style.date}>
                  <div className={style.title}>공연 시작일</div>
                  <div className={style.content}>
                    {data.data.endDate.slice(2, 4)}년&nbsp;
                    {data.data.endDate.slice(5, 7)}월&nbsp;
                    {data.data.endDate.slice(8, 10)}일&nbsp;
                    {data.data.endDate.slice(11, 13)}시&nbsp;
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
                  disabled={onModal}
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
          offset={offset}
          committedOffset={committedOffset}
          endOffset={endOffset}
        />
      )}
    </>
  );
}
