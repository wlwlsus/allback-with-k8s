import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./SeatList.module.css";
import { useQuery, useMutation } from "@tanstack/react-query";
import { history } from "./history";
import { $ } from "util/axios";
import { userNick, reservation, userId, userPoint } from "util/store";
import { useRecoilValue, useRecoilState } from "recoil";
import Loading from "components/common/Loading";

export default function SeatList() {
  const navigate = useNavigate();
  const location = useLocation();
  let nowTime = new Date();

  const [seat, setSeat] = useState();
  const [cols, setCols] = useState();
  const [rows, setRows] = useState();

  const [isPointBtn, setIsPointBtn] = useState(false);
  const [isKakaoBtn, setIsKakaoBtn] = useState(false);

  const nickName = useRecoilValue(userNick);
  const id = useRecoilValue(userId);
  const [reservationInfo, setReservationInfo] = useRecoilState(reservation);
  const [point, setPoint] = useRecoilState(userPoint);

  const [check, setCheck] = useState(false);
  const [data, setData] = useState();

  // 대기열창 모달 & 순서 계산용 변수
  const [modalOpen, setModalOpen] = useState(false);
  const [offset, setOffset] = useState();
  const [committedOffset, setCommittedOffset] = useState();
  const [endOffset, setEndOffset] = useState();

  // 좌석선택 페이지 or 예매화면 페이지 변환용 변수
  const [isreserve, setIsreserve] = useState(false);

  // 대기열 체크용 함수
  let interval;

  // API_PUT 함수
  const res_put = () => {
    return $.put(
      `/payment-service/api/v1/reservation/${reservationInfo.reservationId}`,
      {
        userId: id,
        price: location.state.price,
      }
    );
  };

  const { mutate: onReserve } = useMutation(res_put, {
    onSuccess: () => {
      navigate("../../../complete", {
        state: {
          title: location.state.title,
          reservationId: reservationInfo.reservationId,
          seat: seat,
          price: location.state.price,
          endDate: location.state.endDate,
        },
      });
    },
  });

  const onPayCheck = () => {
    // 포인트 충분하면 결제
    if (point >= location.state.price) {
      onReserve();
    } else {
      if (
        window.confirm(
          "포인트가 부족합니다. 마이페이지에서 포인트를 충전하시겠습니까?"
        )
      ) {
        onDelete();
        navigate("../../../mypage");
      }
    }
  };

  // 해당 공연장의 좌석 조회(트래픽 많을 경우 대기열 생성)
  const { isLoading, refetch } = useQuery(
    [`seat-list_${location.state.concertId}`],
    () =>
      $.get(`/concert-service/api/v1/seat/${location.state.concertId}`)
        .then((res) => {
          setCheck(true);
          setData(res);
          setModalOpen(false);
        })
        .catch((err) => {
          setModalOpen(true);
          setOffset(err.response.data.offset);
          setCommittedOffset(err.response.data.committedOffset);
          setEndOffset(err.response.data.endOffset);

          interval = setInterval(() => {
            $.get(`/concert-service/api/v1/seat/${location.state.concertId}`, {
              headers: {
                "KAFKA.UUID": err.response.data.uuid,
                "KAFKA.PARTITION": err.response.data.partition,
                "KAFKA.OFFSET": err.response.data.offset,
              },
            })
              .then((res) => {
                setCheck(true);
                setData(res);
                setModalOpen(false);
              })
              .catch((err) => {
                setCommittedOffset(err.response.data.committedOffset);
                setEndOffset(err.response.data.endOffset);
                console.log(err);
              });
          }, 1000);
        })
  );

  const newData = {
    concertId: location.state.concertId,
    seat: seat,
  };

  // API_DELETE 함수
  const onDelete = () => {
    return $.delete(
      `/concert-service/api/v1/seat/delete/${reservationInfo.reservationId}`,
      reservationInfo.reservationId
    )
      .then(() => {
        setCheck(true);
        setModalOpen(false);
      })
      .catch((err) => {
        setModalOpen(true);
        setOffset(err.response.data.offset);
        setCommittedOffset(err.response.data.committedOffset);
        setEndOffset(err.response.data.endOffset);

        interval = setInterval(() => {
          $.get(
            `/concert-service/api/v1/seat/delete/${reservationInfo.reservationId}`,
            {
              headers: {
                "KAFKA.UUID": err.response.data.uuid,
                "KAFKA.PARTITION": err.response.data.partition,
                "KAFKA.OFFSET": err.response.data.offset,
              },
            }
          )
            .then(() => {
              setCheck(true);
              setModalOpen(false);
            })
            .catch((err) => {
              setCommittedOffset(err.response.data.committedOffset);
              setEndOffset(err.response.data.endOffset);
            });
        }, 1000);
      });
  };

  const handleBeforeUnload = (e) => {
    e.preventDefault();
    e.returnValue = "";
    onDelete();
  };

  // 뒤로가기 이벤트 감지
  useEffect(() => {
    const listenBackEvent = () => {
      onDelete();
      navigate("/");
    };
    const historyEvent = history.listen(({ action }) => {
      if (action === "POP") {
        listenBackEvent();
      }
    });
    return historyEvent;
  }, []);

  useEffect(() => {
    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  //API_POST 함수
  const onSelect = () => {
    return $.post(`/concert-service/api/v1/seat`, newData)
      .then((res) => {
        setCheck(true);
        setModalOpen(false);
        setIsreserve(true);
        setReservationInfo({
          title: location.state.title,
          reservationId: res.data,
          seat: seat,
          price: location.state.price,
          date: location.state.endDate,
        });
      })
      .catch((err) => {
        setModalOpen(true);
        setOffset(err.response.data.offset);
        setCommittedOffset(err.response.data.committedOffset);
        setEndOffset(err.response.data.endOffset);

        let interval = setInterval(() => {
          $.get(`/concert-service/api/v1/seat`, {
            headers: {
              "KAFKA.UUID": err.response.data.uuid,
              "KAFKA.PARTITION": err.response.data.partition,
              "KAFKA.OFFSET": err.response.data.offset,
            },
          })
            .then(() => {
              setCheck(true);
              setModalOpen(false);
              clearInterval(interval);
            })
            .catch((err) => {
              setCommittedOffset(err.response.data.committedOffset);
              setEndOffset(err.response.data.endOffset);
            });
        }, 1000);
      });
  };

  // 예매하기 클릭 시 이벤트 발생
  // const { mutate } = useMutation(res_post, {
  //   onSuccess: (res) => {
  //     setIsreserve(true);
  //     setReservationInfo({
  //       title: location.state.title,
  //       reservationId: res.data,
  //       seat: seat,
  //       price: location.state.price,
  //       date: location.state.endDate,
  //     });
  //   },
  // });

  const rowNo = [
    "",
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
  ];

  const onClicked = (e) => {
    if (e === seat) setSeat();
    else setSeat(e);
  };
  const getKeyByValue = (obj, value) => {
    return Object.keys(obj).find((key) => obj[key] === value);
  };

  //각 좌석의 btn 태그 생성
  const renderSeat = (row, col) => {
    const value = `${rowNo[row]}${col}`;
    const key = getKeyByValue(data.data.seatList, value);
    return (
      <button
        key={value}
        value={value}
        className={
          key !== undefined
            ? style.seat_reserved
            : value === seat
            ? style.seat_selected
            : style.seat
        }
        onClick={() => onClicked(value)}
        disabled={key !== undefined ? true : false}
      ></button>
    );
  };

  // 각 행의 좌석 정보 호출
  const renderRow = (row) => {
    const seats = [];
    seats.push(
      <div key={row} className={style.seat_row_name}>
        <span>{rowNo[row]}행</span>
      </div>
    );
    for (let col = 1; col <= cols; ++col) {
      seats.push(renderSeat(row, col));
    }
    return (
      <div key={row} className={style.seat_row}>
        {seats}
      </div>
    );
  };

  // 전체 좌석정보 저장하는 배열
  const rowsElements = [];
  for (let row = 1; row <= rows; ++row) {
    rowsElements.push(renderRow(row));
  }

  const onCheck = () => {
    if (seat === undefined || seat === null) {
      alert("좌석을 선택하여주세요.");
      return;
    }
    if (nowTime >= new Date(data.data.endDate)) {
      alert("마감된 공연입니다.");
      navigate("/home");
    }
    // 좌석 한번 더 갱신
    refetch();
    const key = getKeyByValue(data.data.seatList, seat);
    if (key === undefined) {
      onSelect();
    } else {
      alert("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
      setSeat();
    }
  };

  useEffect(() => {
    if (!isLoading) {
      setCols(data.data.col);
      setRows(data.data.row);
    }
  }, [isLoading]);

  useEffect(() => {
    if (!modalOpen) {
      clearInterval(interval);
    }
  }, [modalOpen]);

  return (
    <>
      {!isLoading && data && !isreserve && (
        <div className={style.container}>
          <div className={style.header}>
            <div className={style.title}>{location.state.title}</div>
            <div className={style.information}>
              <div className={style.name}>공연일</div>
              <div className={style.content}>
                {location.state.endDate.slice(0, 4)}년&nbsp;
                {location.state.endDate.slice(5, 7)}월&nbsp;
                {location.state.endDate.slice(8, 10)}일&nbsp;
                {location.state.endDate.slice(11, 13)}시&nbsp;
                {location.state.endDate.slice(14, 16)}분&nbsp;
              </div>
              <div className={style.name}>공연장소</div>
              <div className={style.content}>{location.state.location}</div>
              <div className={style.name}>선택 좌석</div>
              <div className={style.content}>{seat}</div>
            </div>
            <div className={style.btn_list}>
              <div className={style.reserve_btn}>
                <button
                  onClick={() => {
                    onCheck();
                  }}
                >
                  예매하기
                </button>
              </div>
              <div className={style.cancel_btn}>
                <button
                  onClick={() => {
                    navigate("/home");
                  }}
                >
                  메인으로
                </button>
              </div>
            </div>
          </div>
          <div className={style.main}>
            <div className={style.blank} />
            <div className={style.stage_div}>STAGE</div>
            <div className={style.seat_total}>{rowsElements}</div>
          </div>
        </div>
      )}
      {!isLoading && data && isreserve && (
        <div className={style.total}>
          <div className={style.left_div}>
            <div>
              <img
                className={style.poster_img}
                src={location.state.image}
                alt=""
              />
            </div>
            <div>
              <button
                className={style.btn}
                onClick={() => {
                  onPayCheck();
                }}
              >
                결제하기
              </button>
              <button
                className={style.btn_cancel}
                onClick={() => {
                  onDelete();
                  navigate("/../../../");
                }}
              >
                예매취소
              </button>
            </div>
          </div>
          <div className={style.right_div}>
            <div className={style.pay_type}>잔여 포인트</div>
            <div className={style.btn_type}>
              <div>{point}원</div>
            </div>
            <div className={style.contents}>
              <div className={style.name2}>
                <div className={style.title2}>공연명</div>
                <div className={style.content2}>{location.state.title}</div>
              </div>
              <div className={style.seat2}>
                <div className={style.title2}>좌석 번호</div>
                <div className={style.content2}>
                  {seat[0]}-{seat.slice(1)}
                </div>
              </div>
              <div className={style.location2}>
                <div className={style.title2}>공연장</div>
                <div className={style.content2}>{location.state.location}</div>
              </div>
              <div className={style.date2}>
                <div className={style.title2}>공연일</div>
                <div className={style.content2}>
                  {location.state.endDate.slice(2, 4)}년&nbsp;
                  {location.state.endDate.slice(5, 7)}월&nbsp;
                  {location.state.endDate.slice(8, 10)}일&nbsp;
                  {location.state.endDate.slice(11, 13)}시&nbsp;
                </div>
              </div>
            </div>
            <div className={style.pay}>
              <div className={style.title2}>총 결제 금액</div>
              <div className={style.content2}>{location.state.price}원</div>
            </div>
          </div>
        </div>
      )}
      {modalOpen && (
        <Loading
          setModalOpen={setModalOpen}
          offset={offset}
          committedOffset={committedOffset}
          endOffset={endOffset}
        />
      )}
    </>
  );
}
