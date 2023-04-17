import React from "react";
import { useNavigate } from "react-router-dom";

export default function ConcertList() {
  const navigate = useNavigate();
  return (
    <div>
      <button
        onClick={() => {
          navigate(`detail/${1}`);
        }}
      >
        예매 신청
      </button>
    </div>
  );
}
