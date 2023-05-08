import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

export const userId = atom({
  key: "userId",
  default: 0,
  effects_UNSTABLE: [persistAtom],
});

export const userRole = atom({
  key: "userRole",
  default: "",
  effects_UNSTABLE: [persistAtom],
});

export const userNick = atom({
  key: "userNick",
  default: "",
  effects_UNSTABLE: [persistAtom],
});

export const userPoint = atom({
  key: "userPoint",
  default: 0,
  effects_UNSTABLE: [persistAtom],
});

export const createdTime = atom({
  key: "createdTime",
  default: "",
  effects_UNSTABLE: [persistAtom],
});

export const reservation = atom({
  key: "reservation",
  default: { title: "", reservationId: 0, seat: "", price: 0, date: "" },
  effects_UNSTABLE: [persistAtom],
});
