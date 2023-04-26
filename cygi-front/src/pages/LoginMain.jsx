import React from "react";
import ConcertList from "../components/login-main/ConcertList";
import { Routes, Route } from "react-router-dom";
import ConcertDetail from "../components/login-main/ConcertDetail";
import SeatList from "../components/login-main/SeatList";
import Reservation from "../components/login-main/Reservation";

export default function LoginMain() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<ConcertList />} />
        <Route path="/detail/:concertSeq" element={<ConcertDetail />} />
        <Route path="/detail/:concertSeq/seat" element={<SeatList />} />
        <Route
          path="/detail/:concertSeq/seat/reservation"
          element={<Reservation />}
        />
      </Routes>
    </div>
  );
}
