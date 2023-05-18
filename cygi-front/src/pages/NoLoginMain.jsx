import React from "react";
import style from "./NoLoginMain.module.css";
import Kakao from "img/kakao_login_medium_narrow.png";

export default function NoLoginMain() {
  const redirectUri = "http://allback.site/oauth/redirect";
  // const redirectUri = "http://localhost:3000/oauth/redirect";

  const goSocialLogin = (socialType) => {
    const url = `http://allback.site:8000/api/v1/oauth2/authorization/${socialType}?redirect_uri=${redirectUri}`;
    window.location.href = url;
  };

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
          goSocialLogin("kakao");
          // navigate("../home");
        }}
      />
    </div>
  );
}
