import React from "react";
import { useNavigate } from "react-router-dom";

export default function SeatList() {
  const navigate = useNavigate();
  return (
    <div>
      <div> 좌석 확인</div>
      <button
        onClick={() => {
          navigate(`reservation`);
        }}
      >
        해당 좌석 예매
      </button>
    </div>
  );
}
