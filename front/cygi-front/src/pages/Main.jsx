import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function Main() {
  const navigate = useNavigate();

  useEffect(() => {
    if (sessionStorage.getItem("accessToken") !== null) navigate("/home");
    else navigate("/login");
  }, []);

  return <></>;
}
