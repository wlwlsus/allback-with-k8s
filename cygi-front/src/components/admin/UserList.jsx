import React from "react";
import style from "./UserList.module.css";
import { useQuery } from "@tanstack/react-query";
import { $_admin } from "util/axios";

export default function UserList() {
  // 사용자 목록 조회
  const { data: user } = useQuery(["user"], () =>
    $_admin.get(`/dashboard/user?page=${1}`)
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
          <div className={style.role}>권한</div>
        </div>
        {user &&
          user.data.content.map((content) => {
            let date =
              content.createDate.slice(2, 4) +
              "." +
              content.createDate.slice(5, 7) +
              "." +
              content.createDate.slice(8, 10) +
              "." +
              content.createDate.slice(11, 13) +
              ":" +
              content.createDate.slice(14, 16) +
              "";
            return (
              <div className={style.user_body} key={content.uuid}>
                <div className={style.user_id}>{content.email}</div>
                <div className={style.nickname}>{content.nickname}</div>
                <div className={style.signup_date}>{date}</div>
                <div className={style.role}>
                  {content.role === "ROLE_USER" ? "일반" : "관리자"}
                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
}
