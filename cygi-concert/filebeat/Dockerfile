FROM docker.elastic.co/beats/filebeat:7.12.0

COPY filebeat.yml /usr/share/filebeat/filebeat.yml
USER root

RUN mkdir /var/logs

RUN chown -R root /usr/share/filebeat