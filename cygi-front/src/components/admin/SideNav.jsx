import React from "react";
import { useNavigate } from "react-router-dom";

export default function SideNav() {
  const navigate = useNavigate();
  return (
    <div>
      <div
        onClick={() => {
          navigate("total");
        }}
      >
        DASH BOARD
      </div>
      <div
        onClick={() => {
          navigate("reservation-list");
        }}
      >
        정산 내역
      </div>
      <div
        onClick={() => {
          navigate("user-list");
        }}
      >
        사용자 목록
      </div>
      <div
        onClick={() => {
          navigate("calculation-list");
        }}
      >
        전체 예매 내역
      </div>
    </div>
  );
}
