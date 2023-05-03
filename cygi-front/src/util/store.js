import { atom, selector } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

export const userId = atom({
  key: "userId",
  default: "",
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
