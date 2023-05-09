import React from "react";
import style from "./UserList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $ } from "util/axios";

export default function UserList() {
  // 사용자 목록 조회
  const { isLoading, data } = useQuery(["user"], () =>
    $.get(`/dashboard/user`)
  );

  return (
    <div className={style.container}>
      <div className={style.user}>
        <div className={style.title}>
          <span>사용자 목록</span>
        </div>
        <div className={style.user_header}>
          <div className={style.user_id}>아이디</div>
          <div className={style.nickname}>닉네임</div>
          <div className={style.signup_date}>가입일</div>
          <div className={style.role}>역할</div>
        </div>
      </div>
    </div>
  );
}
