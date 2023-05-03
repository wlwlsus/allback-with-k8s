import React, { useState, useEffect, useRef, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import style from "./ConcertList.module.css";
import { $_concert } from "util/axios";

export default function ConcertList() {
  const navigate = useNavigate();
  let nowTime = new Date();

  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [concertList, setConcertList] = useState([]);
  const [prevScrollY, setPrevScrollY] = useState(0);

  const observer = useRef();
  const lastIdx = useCallback(
    (node) => {
      if (loading) return;
      if (observer.current) observer.current.disconnect();
      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting) {
          console.log("visible");
          setPage((prev) => prev + 1);
        }
      });
      if (node) observer.current.observe(node);
    },
    [loading]
  );

  useEffect(() => {
    // 이전 스크롤 위치를 기억하고, 새로운 공연 목록이 렌더링될 때 이전 스크롤 위치로 스크롤
    setPrevScrollY(window.scrollY);
  }, [concertList]);

  useEffect(() => {
    window.scrollTo(0, prevScrollY);
  }, [prevScrollY]);

  useEffect(() => {
    setLoading(true);
    $_concert.get(`concert?page=${page}`).then((res) => {
      setConcertList((prevConcerts) => {
        return [...new Set([...prevConcerts, res.data])];
      });
      setLoading(false);
    });
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
        결제는 CAN YOU GET IT 포인트 또는 카카오 페이를 통해 이루어집니다.
      </div>
      <div className={style.container}>
        {!loading &&
          concertList.map((contents, index1) => {
            return contents.map((content, index2) => {
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
                  ref={
                    concertList.length * 10 === index1 * 10 + index2 + 1
                      ? lastIdx
                      : null
                  }
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
    </div>
  );
}
