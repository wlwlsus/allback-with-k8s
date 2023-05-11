import React from "react";
import style from "./CalculationList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $_admin } from "util/axios";

export default function CalculationList() {
  // 정산 내역 조회
  const { data: balance } = useQuery(["balance"], () =>
    $_admin.get(`/dashboard/balance`)
  );

  return (
    <div className={style.container}>
      <div className={style.calculation}>
        <div className={style.title}>
          <span>정산내역</span>
        </div>
        <div className={style.calculation_header}>
          <div className={style.date}>정산 날짜</div>
          <div className={style.user_id}>주최자 ID</div>
          <div className={style.account}>계좌번호</div>
          <div className={style.bank_name}>은행명</div>
          <div className={style.audience}>관객 수</div>
          <div className={style.profits}>수익금</div>
          <div className={style.fee}>수수료</div>
          <div className={style.final_profits}>최종 수익금</div>
        </div>
      </div>
    </div>
  );
}
