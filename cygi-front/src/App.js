import { Routes, Route } from "react-router-dom";
import "./App.css";
import HeaderPage from "./pages/router-page/HeaderPage";
import LoginMain from "./pages/LoginMain";
import NoLoginMain from "./pages/NoLoginMain";
import Main from "./pages/Main";
import ReserveComplete from "./pages/ReserveComplete";
import MyPage from "./pages/MyPage";
import Admin from "./pages/Admin";
import Loading from "components/common/Loading";
import SuccessPage from "components/kakao/SuccessPage";

function App() {
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
        <Route path="/success" element={<SuccessPage />} />
      </Routes>
    </>
  );
}

export default App;
