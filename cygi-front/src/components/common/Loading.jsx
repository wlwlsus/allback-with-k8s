import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import style from "./Loading.module.css";
import Spinner from "gif/loading.gif";
import { kafka, stopRecur } from "util/store";
import { useRecoilState } from "recoil";
import { $ } from "util/axios";

export default function Loading({
  setModalOpen,
  offset,
  committedOffset,
  endOffset,
}) {
  const [onkafka, setOnKafka] = useRecoilState(kafka);

  return (
    <div className={style.container}>
      <div className={style.title}>서비스 접속 대기 중입니다.</div>
      <div className={style.loading}>
        <img src={Spinner} alt="" />
      </div>
      <div className={style.content}>
        고객님 앞에{" "}
        <span className={style.prev_cnt}>{offset - committedOffset}</span> 명,
        뒤에 <span className={style.next_cnt}>{endOffset - offset}</span>
        명의 대기자가 있습니다. <br />
        현재 접속 중인 사용자가 많아 대기 중이며, <br />
        잠시만 기다리시면 서비스로 자동 접속 됩니다. <br />
      </div>
      <span className={style.warning}>
        ※요청 중지 시, 메인페이지로 돌아갑니다.
      </span>
      <button
        className={style.btn}
        onClick={() => {
          setModalOpen(false);
          $.get(`/concert-service`, {
            headers: {
              "KAFKA.UUID": onkafka.uuid,
              "KAFKA.PARTITION": onkafka.partition,
              "KAFKA.OFFSET": onkafka.offset,
              "KAFKA.QUIT": "quit",
            },
          }).then(() => {
            // window.location.href = "http://allback.site/";
            window.location.href = "http://localhost:3000/";
          });
        }}
      >
        요청중지
      </button>
    </div>
  );
}
