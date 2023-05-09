import axios from "axios";

export const $ = axios.create({
  baseURL: "http://allback.site:8080/",
  headers: {
    "Content-Type": "application/json",
  },
});

$.interceptors.request.use((config) => {
  config.headers["Authorization"] = `Bearer ${JSON.parse(
    sessionStorage.getItem("accessToken")
  )}`;
  return config;
});
