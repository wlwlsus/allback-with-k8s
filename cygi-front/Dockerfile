FROM node:18.13-alpine as builder

# 작업 폴더를 만들고 npm 설치
WORKDIR /usr/src/app
COPY package.json /usr/src/app/package.json
RUN npm install --force

# 소스를 작업폴더로 복사하고 빌드
COPY . /usr/src/app
RUN npm run build

FROM nginx:alpine

ADD ./build ./build

CMD ["nginx", "-g", "daemon off;"]
