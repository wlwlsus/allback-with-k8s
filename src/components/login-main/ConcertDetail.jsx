import React from "react";
import { useNavigate } from "react-router-dom";

export default function ConcertDetail() {
  const navigate = useNavigate();
  return (
    <div>
      <div> 콘서트 사진 설명</div>
      <div> 콘서트 내용 설명</div>
      <button
        onClick={() => {
          navigate(`seat`);
        }}
      >
        예약 버튼
      </button>
    </div>
  );
}
