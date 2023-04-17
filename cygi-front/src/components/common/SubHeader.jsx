import React from "react";
import style from "./SubHeader.module.css";
import { useNavigate } from "react-router-dom";

export default function SubHeader() {
  const navigate = useNavigate();
  return (
    <div className={style.header}>
      <div
        onClick={() => {
          navigate("../login");
        }}
      >
        비로그인 메인페이지
      </div>
      <div
        onClick={() => {
          navigate("../home");
        }}
      >
        로그인 메인페이지
      </div>
      <div
        onClick={() => {
          navigate("../mypage");
        }}
      >
        유저 마이페이지
      </div>
      <div
        onClick={() => {
          navigate("../admin/total");
        }}
      >
        관리자페이지
      </div>
    </div>
  );
}
