import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { userId, userNick, reservation, userPoint } from "util/store";
import { $ } from "util/axios";
import axios from "axios";
import { useRecoilState, useRecoilValue } from "recoil";
import { useMutation, useQuery } from "@tanstack/react-query";

function SuccessPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const id = useRecoilValue(userId);
  const nickName = useRecoilValue(userNick);
  const reservationInfo = useRecoilValue(reservation);
  const [point, setPoint] = useRecoilState(userPoint);

  // 포인트 갱신용
  const { isLoading, data: pointData } = useQuery(["getPoint"], () =>
    $.get(`/user-service/api/v1/user/point?id=${id}`)
  );

  useEffect(() => {
    console.log(id, reservationInfo.price);
    // URL 파라미터에서 pg_token 값을 추출합니다.
    const searchParams = new URLSearchParams(location.search);
    const pg_token = searchParams.get("pg_token");

    // localStorage에서 tid 값을 가져옵니다.
    const tid = localStorage.getItem("tid");

    // 결제 승인 API를 호출합니다.
    const data = {
      cid: "TC0ONETIME",
      tid: tid,
      partner_order_id: "1000",
      partner_id: nickName,
      pg_token: pg_token,
    };

    // 결제 승인 API를 호출합니다.
    $.post(`/payment-service/api/v1/reservation/approve/${id}`, data)
      .then((response) => {
        if (response.status === 200) {
          // 결제 승인 성공 처리
          alert("충전이 완료되었습니다!");
          $.get(`/user-service/api/v1/user/point?id=${id}`).then((res) => {
            setPoint(res.data);
            window.location.href = "http://allback.site/mypage";
          });
        } else {
          // 결제 승인 실패 처리
          alert("충전을 실패하였습니다.");
          window.location.href = "http://allback.site/mypage";
        }
      })
      .catch((error) => {
        alert("문제가 발생하였습니다.");
        console.log(error);
        window.location.href = "http://allback.site/mypage";
      });
  }, [location.search]);

  return (
    <div
      style={{
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
      }}
    >
      <h3>결제 승인 처리 중입니다...</h3>
      {/* 결제 성공 페이지에 필요한 컴포넌트들을 추가합니다. */}
    </div>
  );
}

export default SuccessPage;
