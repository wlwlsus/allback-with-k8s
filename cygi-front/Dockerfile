FROM nginx

WORKDIR /app

RUN mkdir ./build

# 빌드된 React 애플리케이션 파일을 작업 폴더에 복사
ADD ./build ./build

# Nginx 서버를 실행
CMD ["nginx", "-g", "daemon off;"]
