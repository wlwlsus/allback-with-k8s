import { Buffer } from "buffer";
import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

export default function Redirect() {
  const navigate = useNavigate();
  const [serchParams] = useSearchParams();
  const accessToken = serchParams.get("accessToken");
  const refreshToken = serchParams.get("refreshToken");
  sessionStorage.setItem("isSocial", JSON.stringify(true));
  const error = serchParams.get("error");

  useEffect(() => {
    if (error) {
      navigate("/login");
      return;
    }
    // JWT decode
    const base64Payload = accessToken.split(".")[1];
    const payload = Buffer.from(base64Payload, "base64");
    const result = JSON.parse(payload.toString());

    sessionStorage.setItem("accessToken", JSON.stringify(accessToken));
    sessionStorage.setItem("refreshToken", JSON.stringify(refreshToken));

    console.log("회원 정보");
    console.log(result);

    navigate("../../home");
  });
}
