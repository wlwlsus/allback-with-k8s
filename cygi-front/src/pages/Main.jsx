import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Main() {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(false);

  // 로그인 여부에 따라 메인 페이지 다르게 표시
  // 추후 토큰 인증으로 교체
  useEffect(() => {
    if (isLogin) navigate("/home");
    else navigate("/login");
  }, []);

  return <></>;
}
