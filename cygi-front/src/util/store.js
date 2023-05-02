import { atom, selector } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

export const user = atom({
  key: "userId",
  default: "",
  effects_UNSTABLE: [persistAtom],
});

// 공연 목록 저장하는 상태 변수
export const concertState = atom({
  key: "concertState",
  default: [],
});

export const concertLastIdxState = selector({
  key: "concertLastIdxState",
  get: ({ get }) => {
    const concerts = get(concertState);
    if (concerts.length === 0) return -1;
    return concerts[concerts.length - 1].idx;
  },
  set: ({ set }) => {
    set(concertState, []);
  },
});
