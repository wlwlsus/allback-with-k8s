# open jdk java 17 버전 환경
FROM openjdk:17-jdk-slim-buster

# gradle 빌드 시 jar 파일 생성 경로
ARG JAR_FILE=./build/libs/*.jar

# JAR_FILE을 메인 디렉토리에 복사
COPY ${JAR_FILE} app.jar

# 8080 port Open
EXPOSE 8080

# 시스템 진입점
ENTRYPOINT ["java","-jar","app.jar"]
