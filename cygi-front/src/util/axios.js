import axios from "axios";

export const $ = axios.create({
  baseURL: "http://allback.site:8080/",
  headers: {
    "Content-Type": "application/json",
  },
});

// 헤더에 토큰추가
$.interceptors.request.use((config) => {
  config.headers["Access-Token"] = sessionStorage.getItem("access-token");
  return config;
});
