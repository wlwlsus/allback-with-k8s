import axios from "axios";

export const $ = axios.create({
  baseURL: "http://api.allback.site/",
  // baseURL: "http://localhost:8080/",
  headers: {
    "Content-Type": "application/json; charset=UTF-8",
  },
});

export const $_admin = axios.create({
  baseURL: "http://api.allback.site/admin-service",
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
