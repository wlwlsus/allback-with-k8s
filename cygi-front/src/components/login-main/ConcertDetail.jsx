import React from "react";
import { useNavigate } from "react-router-dom";
import style from "./ConcertDetail.module.css";
import PosterBackground from "img/poster.png";
import PosterDatail from "img/poster_detail.png";

export default function ConcertDetail() {
  const navigate = useNavigate();
  let story_text = `무대는 그리스 지중해의 외딴섬... 젊은 날 한때 꿈 많던 아마추어 그룹 리드 싱어였으나 지금은 작은 모텔의 여주인이 된
  도나(Donna)와 그녀의 스무 살 난 딸 소피(Sophie)가 주인공이다. 도나의 보살핌 아래 홀로 성장해온 소피는 약혼자
  스카이(Sky)와의 결혼을 앞두고 아빠를 찾고 싶어 하던 중 엄마가 처녀시절 쓴 일기장을 몰래 훔쳐보게 된다.
  그리고 그 안에서 찾은, 자신의 아버지일 가능성이 있는 세 명의 남자, 샘(Sam Carnichael), 빌(Bill Austin),
  해리(Harry Bright)에게 어머니의 이름으로 초청장을 보내는데...`;
  return (
    // container div에 공연 이미지 삽입
    // inner_container => 영화 상세정보 들어갈 div
    <div className={style.container}>
      <div className={style.concert_background}>
        <img src={PosterBackground} alt="" />
      </div>
      <div className={style.details}>
        <div className={style.top_details}>
          <div className={style.top_detail_img}>
            <img className={style.poster_detail} src={PosterDatail} alt="" />
          </div>
          <div className={style.contents}>
            <div className={style.concert_title}>
              <span>맘마미아!</span>
            </div>
            <div className={style.name}>
              <div className={style.title}>가격</div>
              <div className={style.content}>20,000원</div>
            </div>
            <div className={style.seat}>
              <div className={style.title}>공연장</div>
              <div className={style.content}>싸피콘서트 A홀</div>
            </div>
            <div className={style.location}>
              <div className={style.title}>예매 시작일</div>
              <div className={style.content}>2023년 04월 20일 17시 30분</div>
            </div>
            <div className={style.date}>
              <div className={style.title}>공연 시작일</div>
              <div className={style.content}>2023년 04월 20일 17시 30분</div>
            </div>
          </div>
        </div>
        <div className={style.bottom_details}>
          <div className={style.detail_story}>{story_text}</div>
          <div className={style.reserve_btn}>
            <button
              onClick={() => {
                navigate(`seat`);
              }}
            >
              예매하기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
