import React, { useEffect, useState } from "react";
import style from "./CalculationList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $_admin } from "util/axios";

export default function CalculationList() {
  const [page, setPage] = useState(1);

  // 정산 내역 조회
  const {
    isLoading,
    data: balance,
    refetch,
  } = useQuery(["balance"], () =>
    $_admin.get(`/dashboard/balance?page=${page}&size=10`)
  );

  const onPrev = () => {
    setPage((cur) => cur - 1);
  };

  const onNext = () => {
    setPage((cur) => cur + 1);
  };

  useEffect(() => {
    refetch();
  }, [page]);

  return (
    <>
      {!isLoading && (
        <div className={style.container}>
          <div className={style.calculation}>
            <div className={style.title}>
              <span>정산내역</span>
              <button
                className={page === 1 ? style.btn_disabled : style.btn}
                onClick={() => {
                  onPrev();
                }}
                disabled={page === 1 ? true : false}
              >
                이전
              </button>
              <button
                className={
                  page === balance.data.totalPages
                    ? style.btn_disabled
                    : style.btn
                }
                onClick={() => {
                  onNext();
                }}
                disabled={page === balance.data.totalPages ? true : false}
              >
                다음
              </button>
            </div>
            <div className={style.calculation_header}>
              <div className={style.date}>정산 날짜</div>
              <div className={style.user_id}>주최자 ID</div>
              <div className={style.audience}>관객 수</div>
              <div className={style.final_profits}>최종 수익금</div>
            </div>
            {balance &&
              balance.data.content.map((content, index) => {
                let date =
                  content.createdDate.slice(2, 4) +
                  "." +
                  content.createdDate.slice(5, 7) +
                  "." +
                  content.createdDate.slice(8, 10) +
                  "." +
                  content.createdDate.slice(11, 13) +
                  ":" +
                  content.createdDate.slice(14, 16) +
                  "";
                return (
                  <div className={style.calculation_body} key={index}>
                    <div className={style.date}>{date}</div>
                    <div className={style.user_id}>{content.userId}</div>
                    <div className={style.audience}>{content.customer}</div>
                    <div className={style.final_profits}>{content.proceed}</div>
                  </div>
                );
              })}
          </div>
        </div>
      )}
    </>
  );
}
