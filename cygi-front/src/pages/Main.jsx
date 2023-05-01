import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { user } from "../util/store";

export default function Main() {
  const userId = useRecoilValue(user);
  const navigate = useNavigate();

  // 로그인 여부에 따라 메인 페이지 다르게 표시
  // 추후 토큰 인증으로 교체

  console.log(userId);

  useEffect(() => {
    if (userId.length !== 0) navigate("/home");
    else navigate("/login");
  }, []);

  return <></>;
}
