import { Buffer } from "buffer";
import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { userId, userAuth, userNick, createdTime } from "../../util/store";

export default function Redirect() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const accessToken = searchParams.get("accessToken");
  const refreshToken = searchParams.get("refreshToken");
  sessionStorage.setItem("isSocial", JSON.stringify(true));
  const error = searchParams.get("error");

  const [id, setId] = useRecoilState(userId);
  const [auth, setAuth] = useRecoilState(userAuth);
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

    setId(result.userId);
    setAuth(result.auth);
    setNickName(result.nickname);
    setCreateTime(result.createdTime);

    navigate("../../");
  });
}
