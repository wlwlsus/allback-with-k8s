import axios from "axios";

export const $_user = axios.create({
  baseURL: "http://localhost:8000/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export const $_payment = axios.create({
  baseURL: "http://localhost:8001/api/v1/reservation",
  headers: {
    "Content-Type": "application/json",
  },
});

export const $_concert = axios.create({
  // baseURL: "http://localhost:8002/api/v1",
  baseURL: "https://k8a806.p.ssafy.io:3002/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export const $ = axios.create({
  baseURL: "http://localhost:8002/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});
