import React from "react";
import style from "./Header.module.css";
import { useNavigate } from "react-router-dom";
import logo from "img/logo.png";

export default function Header() {
  const navigate = useNavigate();
  return (
    <div className={style.header}>
      <div>
        <div
          onClick={() => {
            navigate("home");
          }}
          className={style.header_left}
        >
          <img className={style.logo} src={logo} alt="" />{" "}
          <p className={style.logo_name}>CAN YOU GET IT</p>
        </div>
      </div>
      <div className={style.header_right}>
        <div
          className={style.nologin}
          onClick={() => {
            navigate("login");
          }}
        >
          비로그인 메인페이지
        </div>
        <div
          className={style.login}
          onClick={() => {
            navigate("home");
          }}
        >
          로그인 메인페이지
        </div>
        <div
          className={style.mypage}
          onClick={() => {
            navigate("mypage");
          }}
        >
          유저 마이페이지
        </div>
        <div
          className={style.admin}
          onClick={() => {
            navigate("admin/total");
          }}
        >
          관리자페이지
        </div>
        <div
          className={style.loading}
          onClick={() => {
            navigate("loading");
          }}
        >
          로딩스피너 확인용
        </div>
      </div>
    </div>
  );
}
