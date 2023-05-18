import axios from "axios";

export const $ = axios.create({
  baseURL: "https://allback.site:8080/",
  // baseURL: "http://localhost:8080/",
  headers: {
    "Content-Type": "application/json; charset=UTF-8",
  },
});

export const $_admin = axios.create({
  baseURL: "https://k8a806.p.ssafy.io:8090/api/v1",
  headers: {
    "Content-Type": "application/json; charset=UTF-8",
  },
});

$.interceptors.request.use((config) => {
  config.headers["Authorization"] = `Bearer ${JSON.parse(
    sessionStorage.getItem("accessToken")
  )}`;
  return config;
});

$_admin.interceptors.request.use((config) => {
  config.headers["Authorization"] = `Bearer ${JSON.parse(
    sessionStorage.getItem("accessToken")
  )}`;
  return config;
});
