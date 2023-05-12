from hmac import new

from locust import HttpUser, task, between, TaskSet, constant, SequentialTaskSet
import logging
import time, random

authorization = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNzY2ODgyMTczIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4NTYzNDQ4OX0.NJMI5o7XlD2LCQ7FFbkQDiDnk2FghZ05lBtO_WNCeoo'

# 티켓팅봇 : 공연정보조회 - 남은좌석조회 - 좌석상태변경 - 예약
class UserBehavior(SequentialTaskSet):

    global concertId
    concertId = 1
    # res = None

    @task
    def get_concert_info(self):
        print("::::::::::::::::::::::::::::::::::::::::::::::::::::: TASK 01")
        # 1단계 : 공연 정보 조회
        self.res = queueGetRequest(self, '공연 정보 조회', f'/concert/{concertId}')
        # 공연이 없으면 종료
        if self.res.status_code != 200:
            print("공연이 없음 >>> 종료")
            self.user.stop()

    # 사용자 관점에서는 get_seat_info이후에 check_seat을 시행해야 하지만
    # 테스트 관점에서는 check_seat 이후에 get_seat_info를 실행한다
    @task
    def check_seat(self):
        print("::::::::::::::::::::::::::::::::::::::::::::::::::::: TASK 02")
        if self.res is not None and self.res.status_code == 200:
            # 3단계 : 남은 좌석 조회 => 남은 좌석의 수를 rest라고 하는 수로 확인
            self.res = queueGetRequest(self, '남은 좌석 조회', f'/seat/rest/{concertId}')
            # 남은 좌석이 없으면 종료
            if self.res.json()['rest'] == 0:
                self.user.stop()

            # 고를 좌석을 정함
            self.row = random.randrange(0, 10)
            self.col = random.randrange(0, 20)
            self.seatEng = f'{chr(self.row + 65)}{self.col + 1}';
            print(f"현재 선택 좌석 >>> {self.seatEng}")
        else:
            # 공연 정보 조회 실패시 정지
            print("공연 정보 없음 >>> 중지")
            self.user.stop()

    @task
    def get_seat_info(self):
        print("::::::::::::::::::::::::::::::::::::::::::::::::::::: TASK 03")
        # 2단계 : 좌석 정보 조회 => 현재 예약중이거나 예약 완료된 좌석을 반환
        # 남은 좌석은 알 수 없다
        # if self.res is not None and self.res.status_code == 200:
        self.res = queueGetRequest(self, '좌석 정보 조회', f'/seat/{concertId}')
        restSeat = self.res.json()
        print(f'restSeat >>>> {restSeat}')
        seatList = restSeat['seatList']

        # 남은 좌석이 없다면, 그 즉시 종료
        if len(seatList) == 200:
            print("좌석이 없음 >>> 종료")
            self.user.stop()

        # 선택한 좌석이 비어 있는지 확인
        seatAble = False
        for seat in seatList:
            # print(f"점유 된 좌석 정보 >>> {seat}")
            # if self.row == int(seat.row) and self.col == int(seat.col):
            if self.seatEng == seat:
                seatAble = True
                break
        # 좌석이 차지 되었으면 2번 태스크로 돌아감
        if seatAble:
            self.check_seat()

        print(f"좌석 선택 가능 >>> {self.seatEng}")
            # 선택할 좌석
            # random_num = random.randrange(0, len(seatList))
            # self.mySeat = seatList[random_num]
            # print('나의 좌석 :', self.mySeat)
        # else:
            # 공연 정보 조회 실패시 정지
            # self.user.stop()



    @task
    def chage_seat_state(self):
        print("::::::::::::::::::::::::::::::::::::::::::::::::::::: TASK 04")
        # 4단계 = 좌석 점유
        # 좌석 정보를 payload에 입력
        payload = {
            'concertId': concertId,
            # 'concertId': concertId,
            'seat': self.seatEng
            # 'seat': self.seatEng
            # 'seat': f'{chr(self.row + 65)}-{self.col + 1}'
        }
        # header 설정
        postHeader = {
            'Content-Type': 'application/json',
            'AUTHORIZATION': f'{authorization}'
        }
        # post 요청 전송

        print("헤더")
        print(postHeader)
        print("페이로드")
        print(payload)

        self.res = self.client.post('/seat', headers=postHeader, json=payload)
        print("post 요청 전송 완료")

        print(":::::: response text ::::::")
        print(self.res.text)

        print('최초요청 : status=', self.res.status_code)


        if self.res.status_code != 307 or self.res.status_code != 201:
            print(f'코드 이상 >>>> {self.res.status_code}')
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
                time.sleep(3)
                new_response = self.client.post('/seat', headers=new_headers, json=payload)

                # 요청 완료 시, 반복문 탈출
                if 200 <= new_response.status_code < 300:
                    print('탈출!', new_response.status_code, offset)
                    # TODO : 그 사이에 해당 좌석을 누군가 예약하기 시작했을 경우 좌석을 다시 선택하게끔 해야 함
                    print(self.res.text)
                    break
                # bad request => 좌성이 이미 점유됨
                elif new_response.status_code == 400:
                    print('status=', self.res.status_code, '객석 점유 됨')
                    self.check_seat()
        # 최초 요청시 자리가 점유 되면 되돌아감
        # elif self.res.status_code == 400:
        #     print('status=', self.res.status_code, '객석 점유 됨')
        #     self.check_seat()
        else:
            print('status=', self.res.status_code, '최초요청 성공')
            print(self.res.text)




        # 5단계 : 예약 (대기열 필요 없음)
        # 종료
        self.user.stop()













def queueGetRequest(self, name, url):

    headers = {
        'AUTHORIZATION': authorization
    }

    response = self.client.get(url, headers=headers)
    print('QGR ::: ', name, '최초요청 : status=', response.status_code)

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
            time.sleep(3)
            new_response = self.client.get(url, headers=new_headers)

            # print('재요청 : offset=', offset, 'status:', new_response.status_code)

            # 요청 완료 시, 반복문 탈출
            if 200 <= new_response.status_code < 300:
                print('탈출!', new_response.status_code, offset)
                return response
    else:
        print('status=', response.status_code, '최초요청 성공')
        print(response.text)
        return response








class LocustUser(HttpUser):
    host = "http://allback.site:8080/concert-service/api/v1"
    # host = "http://localhost:8080/concert-service/api/v1"
    tasks = [UserBehavior]
    wait_time = between(10, 10)   # 1~4초 사이 간격으로 랜덤하게 작업을 수행한다.
    # wait_time = constant(10)    # 5초마다 작업을 수행한다.