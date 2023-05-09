import axios from "axios";

export const $_user = axios.create({
  baseURL: "http://allback.site:8080/user-service/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export const $_payment = axios.create({
  baseURL: "http://allback.site:8080/payment-service/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export const $_concert = axios.create({
  baseURL: "http://allback.site:8080/concert-service/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export const $_admin = axios.create({
  baseURL: "http://allback.site:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});
