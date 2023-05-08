import React from "react";
import Header from "../../components/common/Header";
import { Outlet } from "react-router-dom";

export default function HeaderPage() {
  return (
    <div>
      <Header />
      <Outlet />
    </div>
  );
}
