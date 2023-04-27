import React from "react";
import ConcertList from "../components/login-main/ConcertList";
import { Routes, Route } from "react-router-dom";
import ConcertDetail from "../components/login-main/ConcertDetail";
import SeatList from "../components/login-main/SeatList";

export default function LoginMain() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<ConcertList />} />
        <Route path="/detail/:concertSeq" element={<ConcertDetail />} />
        <Route path="/detail/:concertSeq/seat" element={<SeatList />} />
      </Routes>
    </div>
  );
}
