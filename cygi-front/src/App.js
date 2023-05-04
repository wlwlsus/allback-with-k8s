import { Routes, Route, useNavigate } from "react-router-dom";
import "./App.css";
import HeaderPage from "./pages/router-page/HeaderPage";
import LoginMain from "./pages/LoginMain";
import NoLoginMain from "./pages/NoLoginMain";
import Main from "./pages/Main";
import ReserveComplete from "./pages/ReserveComplete";
import MyPage from "./pages/MyPage";
import Admin from "./pages/Admin";
import Loading from "components/common/Loading";
import Redirect from "components/oauth/Redirect";
import SuccessPage from "components/oauth/SuccessPage";
import { useEffect } from "react";
import { history } from "components/login-main/history";

function App() {
  const navigate = useNavigate();
  // 뒤로가기 이벤트 감지
  useEffect(() => {
    const listenBackEvent = () => {
      window.location.href = "http://localhost:3000/";
    };
    const historyEvent = history.listen(({ action }) => {
      if (action === "POP") {
        listenBackEvent();
      }
    });
    return historyEvent;
  }, []);
  return (
    <>
      <Routes>
        <Route element={<HeaderPage />}>
          <Route path="/home/*" element={<LoginMain />} />
          <Route path="/admin/*" element={<Admin />} />
          <Route path="/loading" element={<Loading />} />
        </Route>
        <Route path="/login" element={<NoLoginMain />} />
        <Route path="/" element={<Main />} />
        <Route path="/complete" element={<ReserveComplete />} />
        <Route path="/mypage/*" element={<MyPage />} />
        <Route path="/oauth/redirect" element={<Redirect />} />
        <Route path="/success" element={<SuccessPage />} />
      </Routes>
    </>
  );
}

export default App;
