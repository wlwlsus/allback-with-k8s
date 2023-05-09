import React, { useState, useEffect, useRef, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import style from "./ConcertList.module.css";
import { $ } from "util/axios";
import ListLoading from "gif/list_loading.gif";
import { reservation } from "util/store";
import { useRecoilState, useRecoilValue } from "recoil";

export default function ConcertList() {
  const navigate = useNavigate();
  let nowTime = new Date();

  const [concertList, setConcertList] = useState([]);

  const [page, setPage] = useState(0); //현재 페이지
  const [load, setLoad] = useState(true); //로딩 스피너

  const obsRef = useRef(null); //observer Element
  const preventRef = useRef(true); //옵저버 중복 실행 방지

  const reservationInfo = useRecoilValue(reservation);

  useEffect(() => {
    //옵저버 생성
    const observer = new IntersectionObserver(obsHandler, { threshold: 0.5 });
    if (obsRef.current) observer.observe(obsRef.current);
    return () => {
      observer.disconnect();
    };
  }, []);

  // 옵저버 생성
  const obsHandler = (entries) => {
    //옵저버 콜백함수
    const target = entries[0];
    //옵저버 중복 실행 방지
    if (target.isIntersecting && preventRef.current) {
      preventRef.current = false; //옵저버 중복 실행 방지
      setPage((prev) => prev + 1); //페이지 값 증가
    }
  };

  const getConcert = useCallback(async () => {
    setLoad(true);
    await $.get(`/concert-service/api/v1/concert?page=${page}`).then((res) => {
      setConcertList((prev) => [...prev, res.data]);
      preventRef.current = true;
      setLoad(false);
    });
  }, [page]);

  useEffect(() => {
    if (page > 0) getConcert();
  }, [page]);

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
        결제는 CAN YOU GET IT 포인트로 이루어지며, <br />
        카카오페이를 통해 포인트를 충전할 수 있습니다.
      </div>
      <div className={style.container}>
        {concertList &&
          concertList.map((contents) => {
            return contents.map((content) => {
              let date =
                content.endDate.slice(2, 4) +
                "/" +
                content.endDate.slice(5, 7) +
                "/" +
                content.endDate.slice(8, 10) +
                " " +
                content.endDate.slice(11, 13) +
                "시";
              return (
                <div
                  key={content.concertId}
                  className={
                    content.rest === 0 || nowTime >= new Date(content.endDate)
                      ? style.card_disable
                      : style.card
                  }
                  onClick={() => {
                    if (
                      content.rest !== 0 &&
                      nowTime < new Date(content.endDate)
                    )
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
                        {content.rest === 0
                          ? "마감"
                          : content.rest + "/" + content.all}
                      </div>
                    </div>
                    <div className={style.seat}>
                      <div className={style.title}>예매마감일</div>
                      <div className={style.content}>
                        {nowTime >= new Date(content.endDate) ? "마감" : date}
                      </div>
                    </div>
                  </div>
                </div>
              );
            });
          })}
      </div>
      {load && (
        <div>
          <img className={style.loading} src={ListLoading} alt="" />
        </div>
      )}
      <div ref={obsRef}>&nbsp;</div>
    </div>
  );
}
