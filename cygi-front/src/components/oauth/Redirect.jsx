import { Buffer } from "buffer";
import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { userId, userRole, userNick, createdTime } from "../../util/store";

export default function Redirect() {
  const navigate = useNavigate();
  const [serchParams] = useSearchParams();
  const accessToken = serchParams.get("accessToken");
  const refreshToken = serchParams.get("refreshToken");
  sessionStorage.setItem("isSocial", JSON.stringify(true));
  const error = serchParams.get("error");

  const [id, setId] = useRecoilState(userId);
  const [role, setRole] = useRecoilState(userRole);
  const [nickName, setNickName] = useRecoilState(userNick);
  const [createTime, setCreateTime] = useRecoilState(createdTime);

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
    setId(result.userId);
    setRole(result.auth);
    setNickName(result.nickname);
    setCreateTime(result.createdTime);

    navigate("../../");
  });
}
