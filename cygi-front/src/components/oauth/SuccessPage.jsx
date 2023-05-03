import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { userId, userNick, reservation } from "util/store";
import { $_payment } from "util/axios";
import axios from "axios";
import { useRecoilValue } from "recoil";
import { useMutation } from "@tanstack/react-query";

function SuccessPage() {
  const navigate = useNavigate();
  const location = useLocation();

  const id = useRecoilValue(userId);
  const nickName = useRecoilValue(userNick);
  const reservationInfo = useRecoilValue(reservation);

  // API_PUT 함수
  const res_put = () => {
    return $_payment.put(`/reservation/${reservationInfo.reservationId}`);
  };

  const { mutate: onReserve } = useMutation(res_put, {
    onSuccess: () => {
      navigate("../complete", {
        state: {
          title: reservationInfo.title,
          reservationId: reservationInfo.reservationId,
          seat: reservationInfo.seat,
          price: reservationInfo.price,
          date: reservationInfo.date,
        },
      });
    },
  });

  useEffect(() => {
    console.log(id);
    // URL 파라미터에서 pg_token 값을 추출합니다.
    const searchParams = new URLSearchParams(location.search);
    const pg_token = searchParams.get("pg_token");

    // localStorage에서 tid 값을 가져옵니다.
    const tid = localStorage.getItem("tid");

    // 결제 승인 API를 호출합니다.
    const data = {
      cid: "TC0ONETIME",
      tid: tid,
      partner_order_id: reservationInfo.reservationId,
      partner_user_id: nickName,
      pg_token: pg_token,
    };

    // 결제 승인 API를 호출합니다.
    axios
      .post(`http://localhost:8001/api/v1/reservation/approve/${id}`, data)
      .then((response) => {
        if (response.status === 200) {
          // 결제 승인 성공 처리
          alert("결제가 완료되었습니다!");
          onReserve();
        } else {
          // 결제 승인 실패 처리
          alert("결제를 실패하였습니다.");
        }
      })
      .catch((error) => {
        alert("문제가 발생하였습니다.");
        console.log(error);
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
