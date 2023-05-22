import React from "react";
import SideNav from "../components/admin/SideNav";
import { Routes, Route } from "react-router-dom";
import Total from "../components/admin/Total";
import UserList from "../components/admin/UserList";
import ReservationList from "../components/admin/ReservationList";
import CalculationList from "../components/admin/CalculationList";
import style from "./Admin.module.css";

export default function Admin() {
  return (
    <div className={style.container}>
      <SideNav />
      <div className={style.right_div}>
        <Routes>
          <Route path="total" element={<Total />} />
          <Route path="user-list" element={<UserList />} />
          <Route path="reservation-list" element={<ReservationList />} />
          <Route path="calculation-list" element={<CalculationList />} />
        </Routes>
      </div>
    </div>
  );
}
