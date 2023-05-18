#!/bin/sh

# Nginx를 먼저 시작합니다.
nginx

# 이제 설정 파일을 복사합니다.
cp /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.bak
cp /etc/nginx/conf.d/my.conf /etc/nginx/conf.d/default.conf

# 변경된 설정을 적용하기 위해 Nginx를 재시작합니다.
nginx -s reload
