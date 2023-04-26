import React from "react";
import style from "./NoLoginMain.module.css";
import Kakao from "img/kakao_login_medium_narrow.png";
import { useNavigate } from "react-router-dom";

export default function NoLoginMain() {
  const navigate = useNavigate();
  return (
    <div className={style.background}>
      <div className={style.title}>
        <span>멈추지 않는 티켓팅 사이트</span>
      </div>
      <div className={style.content}>
        <span>
          MSA, 오토스케일링 등의 기술들을 적용하여
          <br />
          많은 트래픽이 몰리더라도 다운되지 않는 사이트를 구축하였습니다.
        </span>
      </div>
      <img
        className={style.login_btn}
        src={Kakao}
        alt=""
        onClick={() => {
          navigate("../home");
        }}
      />
    </div>
  );
}
