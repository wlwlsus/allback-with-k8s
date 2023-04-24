import axios from "axios";

export const $ = axios.create({
  // baseURL: "http://localhost:4433/api",
  headers: {
    "Content-Type": "application/json",
  },
});
