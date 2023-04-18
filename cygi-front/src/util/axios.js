import axios from "axios";

export const $ = axios.create({
  // baseURL: "https://j8a404.p.ssafy.io/api",
  // baseURL: "http://localhost:4433/api",
  headers: {
    "Content-Type": "application/json",
  },
});
