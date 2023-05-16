from locust import HttpUser, task, between, TaskSet, constant, SequentialTaskSet
import logging
import time, random, math

authorization = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNzY2ODgyMTczIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4NTYzNDQ4OX0.NJMI5o7XlD2LCQ7FFbkQDiDnk2FghZ05lBtO_WNCeoo'


# 티켓팅봇 : 공연정보조회 - 남은좌석조회 - 좌석상태변경 - 예약
class UserBehavior(SequentialTaskSet):
    # def recursive(self):
    #     print(f'USER {self.userCode} | recursive')
    #     self.check_seat()
    #     self.change_seat_state()
    #     self.commit_reservation()
    #     self.user.stop()
    global concertId
    concertId = 10

    @task
    def get_concert_info(self):
        self.userCode = random.randrange(100000, 999999)
        print(f"USER {self.userCode} | TASK 01")
        self.timer = 3
        # 1단계 : 공연 정보 조회
        self.res = queueGetRequest(self, '공연 정보 조회', f'/concert/{concertId}')
        # 공연이 없으면 종료
        if self.res.status_code != 200:
            print(f"USER {self.userCode} | 공연이 없음 >>> 종료")
            self.user.stop()
        # 가격 설정 -> task5에서 사용
        self.price = self.res.json().get('price')

    # 사용자 관점에서는 get_seat_info이후에 check_seat을 시행해야 하지만
    # 테스트 관점에서는 check_seat 이후에 get_seat_info를 실행한다
    @task
    def check_seat(self):
        print(f"USER {self.userCode} | TASK 02")
        # 2단계 : 남은 좌석 조회 => 남은 좌석의 수를 rest라고 하는 수로 확인
        self.res = queueGetRequest(self, '남은 좌석 조회', f'/seat/rest/{concertId}')
        # 남은 좌석의 수
        self.rest = self.res.json()['rest']
        self.all = self.res.json()['all']
        print(f'USER {self.userCode} | 전체 좌석 {self.all}개 中 남은 좌석 {self.rest}개')
        # 남은 좌석이 없으면 종료
        if self.rest <= 0:
            print(f"USER {self.userCode} | 남은 좌석이 없음 >> 종료")
            self.user.stop()

    # else:
    # 공연 정보 조회 실패시 정지
    # print(f"USER {self.userCode} | 공연 정보 없음 >>> 중지")
    # self.user.stop()

    # @task
    # def get_seat_info(self):
    #     print(f"USER {self.userCode} | TASK 03")
    #     # 2단계 : 좌석 정보 조회 => 현재 예약중이거나 예약 완료된 좌석을 반환
    #     # 남은 좌석은 알 수 없다
    #     # if self.res is not None and self.res.status_code == 200:
    #     self.res = queueGetRequest(self, '좌석 정보 조회', f'/seat/{concertId}')
    #     restSeat = self.res.json()
    #     seatList = restSeat['seatList']
    #
    #     # 남은 좌석이 없다면, 그 즉시 종료
    #     if len(seatList) == 200:
    #         print(f"USER {self.userCode} | 좌석이 없음 >>> 종료")
    #         self.user.stop()
    #
    #     # 선택한 좌석이 비어 있는지 확인
    #     seatAble = False
    #     for seat in seatList:
    #         # print(f"점유 된 좌석 정보 >>> {seat}")
    #         # if self.row == int(seat.row) and self.col == int(seat.col):
    #         if self.seatEng == seat:
    #             seatAble = True
    #             break
    #     # 좌석이 차지 되었으면 2번 태스크로 돌아감
    #     if seatAble:
    #         print(f'USER {self.userCode} | {self.seatEng}이 이미 예매 됨')
    #         self.check_seat()
    #
    #     print(f"USER {self.userCode} | 좌석 확정 : {self.seatEng}")
    #     # 선택할 좌석
    #     # random_num = random.randrange(0, len(seatList))
    #     # self.mySeat = seatList[random_num]
    #     # print('나의 좌석 :', self.mySeat)
    #     # else:
    #     # 공연 정보 조회 실패시 정지
    #     # self.user.stop()

    @task
    def change_seat_state(self):
        print(f"USER {self.userCode} | TASK 03")

        # 해당 프로세스가 필요한곳....
        # --> 최초 자리 선택 | -100 리턴시 자리선택
        # DB를 서치하여 이미 존재하지 않는 좌석에 대해서만 예매 시도 -> 이 때
        # seatInfo_response = queueGetRequest(self, '좌석 정보 조회', f'/seat/{concertId}')
        # seatList = seatInfo_response.json()['seatList']
        # if self.all <= len(seatList):
        #     print(f'user {self.userCode} | 남은 좌석이 없음 >> 종료')
        #     self.user.stop()
        # while True:
        #     # 고를 좌석을 정함
        #     self.row = random.randrange(0, 10)
        #     self.col = random.randrange(0, 20)
        #     self.seatEng = f'{chr(self.row + 65)}{self.col + 1}';
        #     if self.seatEng not in seatList:
        #         break
        #     else:
        #         print(f'USER {self.userCode} | 좌석 {self.seatEng}이 데이터 베이스에 존재함')
        # print(f"USER {self.userCode} | 선택 좌석 : {self.seatEng}")

        # 좌석 선택
        if pick_seat(self) is False:
            self.user.stop()

        # 3단계 = 좌석 점유
        # 좌석 정보를 payload에 입력
        payload = {
            'concertId': concertId,
            'seat': self.seatEng
        }
        # header 설정
        postHeader = {
            'Content-Type': 'application/json',
            'AUTHORIZATION': f'{authorization}'
        }

        # post 요청 전송
        self.res = self.client.post('/seat', headers=postHeader, json=payload)
        print(f"USER {self.userCode} | post 요청 전송 완료")
        print(f'USER {self.userCode} | 최초요청 : status=', self.res.status_code)
        print(f'USER {self.userCode} | response text: {self.res.text}')

        # 만약 -100이 반환되었다면 >>> 이미 있는 자리에 들어가려함 >> 재요청
        while self.res.text == '-100':
            print(f'USER {self.userCode} | -100 리턴됨 프로세스를 종료')
            if pick_seat(self) is False:
                self.user.stop()
                break
            payload = {
                'concertId': concertId,
                'seat': self.seatEng
            }
            self.res = self.client.post('/seat', headers=postHeader, json=payload)

        # if self.res.text == '-100':
        #     print(f'USER {self.userCode} | -100 리턴됨 프로세스를 종료')
        #     # self.user.stop()
        # elif \

        # 예상외의 일이 발생하면 중지
        if self.res.status_code != 307 and self.res.status_code != 201:
            print(f'USER {self.userCode} | 코드 이상 >>>> {self.res.status_code} : 중지')
            self.user.stop()

        # 대기열 발동
        if self.res.status_code == 307:
            data = self.res.json()
            uuid = data.get('uuid')
            partition = data.get('partition')
            offset = data.get('offset')

            print(data);

            new_headers = {
                'AUTHORIZATION': authorization,
                'KAFKA.UUID': uuid,
                'KAFKA.PARTITION': str(partition),
                'KAFKA.OFFSET': str(offset)
            }

            while True:
                time.sleep(self.timer)
                new_response = self.client.post('/seat', headers=new_headers, json=payload)

                # 요청 완료 시, 반복문 탈출
                # 성공
                if 200 <= new_response.status_code < 300:
                    print(f'USER {self.userCode} | 탈출!', new_response.status_code, offset, f'선택 좌석: {self.seatEng}')
                    # TODO : 그 사이에 해당 좌석을 누군가 예약하기 시작했을 경우 좌석을 다시 선택하게끔 해야 함
                    self.res = new_response
                    print(self.res.text)
                    break
                # bad request => 좌석이 이미 점유됨
                elif new_response.text == '-100':
                    print(f'USER {self.userCode} | 대기열 status=', self.res.status_code, '객석 점유 됨')
                    # 좌석 재선택
                    if pick_seat(self) is False:
                        self.user.stop()
                        break
                    # 페이로드 수정
                    payload = {
                        'concertId': concertId,
                        'seat': self.seatEng
                    }
                    # 타이머 재설정
                    self.timer = int(new_response.json().get('offset')) - int(
                        new_response.json().get('committedOffset'))
                    # print(f'USER {self.userCode} | 프로세스를 종료')
                    # self.user.stop()
                else:
                    # 재요청
                    self.timer = int(new_response.json().get('offset')) - int(
                        new_response.json().get('committedOffset'))
                    # if self.timer > 10:
                    #     self.timer = 10
                    # else:
                    #     self.timer = 1
                    print(f"USER {self.userCode} | 재요청 시간 : {self.timer}")

        else:
            print(f'USER {self.userCode} | status=', self.res.status_code, '최초요청 성공')

    @task
    def commit_reservation(self):
        if self.res.text == '-100':
            # pick_seat(self)
            print(f'USER {self.userCode} | 점유상태에 의해 해당 결제를 실행 할 수 없음 >> 중지')
            self.user.stop()
            return
        # 4단계 : 예약 (대기열 필요 없음)
        # 예약중 >>>> 예약완료
        print(f"USER {self.userCode} | TASK 04")
        # 예약 번호
        reservation_id = self.res.text
        # payment-host
        reservation_host = 'http://allback.site:8080/payment-service/api/v1'
        # payload
        payload = {
            # JWT 토큰 주인의 userId
            'userId': 1,
            'price': self.price
        }
        # header
        putHeader = {
            'Content-Type': 'application/json',
            'AUTHORIZATION': f'{authorization}'
        }
        # put 요청
        self.res = self.client.put(f'{reservation_host}/reservation/{reservation_id}', headers=putHeader, json=payload)

        print(self.res)
        print(self.res.text)

        if self.res.status_code != 200:
            print(f'USER {self.userCode} | 결제 오류 발생 >> process 종료')
            self.user.stop()
        else:
            print(f"USER {self.userCode} | 결제 완료")
        # 종료
        self.user.stop()


def queueGetRequest(self, name, url):
    headers = {
        'AUTHORIZATION': authorization
    }

    response = self.client.get(url, headers=headers)
    print(f'USER {self.userCode} |', name, '최초요청 : status=', response.status_code)

    # 대기열 발동
    if response.status_code == 307:
        data = response.json()
        uuid = data.get('uuid')
        partition = data.get('partition')
        offset = data.get('offset')

        new_headers = {
            'AUTHORIZATION': authorization,
            'KAFKA.UUID': uuid,
            'KAFKA.PARTITION': str(partition),
            'KAFKA.OFFSET': str(offset)
        }

        # print('step 1 : offset=', offset)
        while True:
            time.sleep(self.timer)
            new_response = self.client.get(url, headers=new_headers)

            # print(f'current re request time is ...... {self.timer}')
            # print('재요청 : offset=', offset, 'status:', new_response.status_code)

            # 요청 완료 시, 반복문 탈출
            if 200 <= new_response.status_code < 300:
                print('탈출!', new_response.status_code, offset)
                return new_response
            else:
                print(new_response.json())
                self.timer = int(new_response.json().get('offset')) - int(
                    new_response.json().get('committedOffset'))
                # if self.timer > 10:
                #     self.timer = 10
                # else:
                #     self.timer = 1
                print(f"USER {self.userCode} | 재요청 시간 : {self.timer}")
    else:
        print(f'USER {self.userCode} | status=', response.status_code, '최초요청 성공')
        print(response.text)
        return response


def pick_seat(self):
    # 해당 프로세스가 필요한곳....
    # --> 최초 자리 선택 | `-100` 리턴시 자리선택
    # DB를 서치하여 이미 존재하지 않는 좌석에 대해서만 예매 시도 -> 이 때
    seatInfo_response = queueGetRequest(self, '좌석 정보 조회', f'/seat/{concertId}')
    seatList = seatInfo_response.json()['seatList']
    if self.all <= len(seatList):
        print(f'user {self.userCode} | 남은 좌석이 없음 >> 종료')
        # self.user.stop()
        return False
    while True:
        # 고를 좌석을 정함
        self.row = random.randrange(0, 10)
        self.col = random.randrange(0, 20)
        self.seatEng = f'{chr(self.row + 65)}{self.col + 1}';
        if self.seatEng not in seatList:
            break
        else:
            print(f'USER {self.userCode} | 좌석 {self.seatEng}이 데이터 베이스에 존재함')
    print(f"USER {self.userCode} | 선택 좌석 : {self.seatEng}")
    return True


class LocustUser(HttpUser):
    host = "http://allback.site:8080/concert-service/api/v1"
    # host = "http://localhost:8080/concert-service/api/v1"
    tasks = [UserBehavior]
    wait_time = between(1, 1)
    # wait_time = constant(10)    # 5초마다 작업을 수행한다.
