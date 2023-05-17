# 🎟️ Can you get it - 터지지 않는 티켓팅 플랫폼

![로고 이미지](/document/logo.png)

1. [서비스 소개](#서비스-소개)
2. [서비스 아키텍처](#서비스-아키텍처)
3. [기능 소개](#기능-소개)
4. [기술 스택](#기술-스택)
5. [프로젝트 산출물](#프로젝트-산출물)
6. [팀원 소개](#팀원-소개)
7. 깃 브랜치 전략?협업환경?

<br>

---
## ✨ 서비스 소개

- 프로젝트 진행 기간  
2023.04.10(월) ~ 2023.05.19(금) (6주간 진행)  
- 소개 영상 : [유튜브 링크](TODO)
- 사이트 링크 : http://allback.site

티켓팅 사이트 `Can You Get It`
티켓팅의 특징은 특정한 시각에 수많은 요청이 한꺼번에 몰린다는 점
이러한 상황에서도 서버가 터지지 않는 안정적인 아키텍처를 고민해보았습니다.

<br>

---
## 서비스 아키텍처
![서비스 아키텍처]()

<br>

---
## 🎯 기능 소개
<details>
<summary>MSA - spring 서버 및 DB를 기능별로 분리</summary>

접은 내용
</details>

<details>
<summary>Spring gateway 및 eureka</summary>

접은 내용
</details>


<details>
<summary>Spring Batch를 이용한 정산 시스템</summary>
<img src="/document/gif/정산.gif" title="정산" width="30%" height="30%"/> <br>
매일 일정한 시각마다 자동으로 공연 주최자들에게 돈을 정산
</details>


<details>
<summary>ELK Stack + kafka를 이용한 로그 수집</summary>
접은 내용
</details>

<details>
<summary>kafka를 이용한 대기열 시스템</summary>
<img src="/document/gif/정산.gif" title="정산" width="30%" height="30%"/> <br>
접은 내용
</details>

<details>
<summary>locust를 이용한 티켓팅 봇 및 성능테스트</summary>
접은 내용
</details>

<details>
<summary>jenkins - 헬름차트 - argo CICD</summary>
접은 내용
</details>

<details>
<summary>k8s를 이용한 도커 컨테이너 관리</summary>
접은 내용
</details>

<details>
<summary>오토 스케일링</summary>
접은 내용
</details>

<details>
<summary>롤링업데이트를 통한 무중단배포</summary>
접은 내용
</details>

<details>
<summary>헬름차트를 이용한 쿠버네티스 리소스 관리</summary>

접은 내용
</details>

<details>
<summary>그라파나랑 프로메테우스를 이용한 자원 관리</summary>

접은 내용
</details>

<details>
<summary>집킨을 이용한 분산추적시스템</summary>

접은 내용
</details>

<details>
<summary>Feign Client를 이용한 서버 간 통신</summary>

접은 내용
</details>

<details>
<summary>jacoco를 이용한 코드 커버리지 측정</summary>

접은 내용
</details>

<details>
<summary>카카오페이를 이용한 결제 시스템</summary>

접은 내용
</details>

<details>
<summary>Observer를 이용한 무한 스크롤 구현</summary>

접은 내용
</details>

<details>
<summary>카카오페이 연동을 통한 포인트 충전 시스템</summary>

접은 내용
</details>

<details>
<summary>Recoil을 활용한 로그인 정보 유자</summary>

접은 내용
</details>

<br>

---
## 🔧 기술 스택
`Backend`
- intelliJ IDE
- springboot 3.0.6
- spring cloud netflix eureka
- spring cloud gateway
- spring cloud zipkin
- spring cloud sleuth
- spring cloud openfeign 4.0.2
- spring Data JPA
- spring batch
- java 17
- jacoco
- mockito
- jwt
- junit
- mapstruct
- gradle
- swagger

`Data`
- mySQL
- redis
- AWS S3

`Frontend`
- visual Studio Code IDE
- reactJS 18.2.0
- javaScript
- recoil 0.7.7
- nodeJS
- axios 1.3.5
- react-query

`Infra`
- mobaxterm
- AWS EC2
- GKE
- kubernetes
- nginx
- jenkins
- docker
- docker-compose


`etc`
- elasticsearch
- logstash
- kibana
- filebeat
- kafka
- locust
- python
- gitlab
- notion
- jira
- mattermost

<br>

---
## 프로젝트 산출물


<br>

---
## 👨‍💻 팀원 소개

팀명 : All Back

|이름|역할|github|
|---|---|---|
|성원준(팀장)|Payment 서버 개발, Gateway 및 Eureka 서버 개발, 쿠버네티스 적용|https://github.com/wlwlsus|
|김동연|User 서버 개발 및 배포, 테스트 봇 개발, locust 성능 테스트|https://github.com/EastFlovv|
|김정수|프론트 react 개발, Concert 서버 개발|https://github.com/kjskjs356|
|윤호산|Concert 서버 개발, Spring Batch  정산 시스템 개발|https://github.com/hosanyoon|
|최준아|CI/CD, ELK Stack 로그 수집 시스템 구축, 대기열 시스템 개발, 코드 커버리지|https://github.com/wnsdk|
