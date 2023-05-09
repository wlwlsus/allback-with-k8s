import axios from "axios";

export const $ = axios.create({
  baseURL: "http://allback.site:8080/",
  headers: {
    "Content-Type": "application/json",
    Authorization: sessionStorage.getItem("accessToken"),
  },
});
