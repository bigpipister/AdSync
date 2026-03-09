#!/bin/bash

# 移除舊容器（如果存在）
podman rm -f postgres 2>/dev/null || true
podman rm -f jenkins 2>/dev/null || true
podman rm -f pgadmin4 2>/dev/null || true

# PostgreSQL
podman run -d --name postgres \
  --network=host \
  -e POSTGRES_DB=idmdb \
  -e POSTGRES_USER=idmdb \
  -e POSTGRES_PASSWORD='idmdb' \
  -v /apps/adws/idmdb:/var/lib/postgresql/data:Z \
  postgres:latest

# Jenkins
podman run -d --name jenkins \
  --network=host \
  -e TZ=Asia/Taipei \
  -v /apps/adws/jenkins/workspace:/var/jenkins_home:Z,U \
  jenkins-lts-jdk17-psql:latest

# PgAdmin4
podman run -d --name pgadmin4 \
  --network=host \
  -e TZ=Asia/Taipei \
  -e PGADMIN_DEFAULT_EMAIL=admin@cht.com.tw \
  -e PGADMIN_DEFAULT_PASSWORD=1qaz@WSX \
  -e PGADMIN_LISTEN_ADDRESS=0.0.0.0 \
  -e PGADMIN_LISTEN_PORT=5050 \
  -e PGADMIN_CONFIG_LOG_FILE=/dev/stdout \
  -e PGADMIN_CONFIG_CONSOLE_LOG_LEVEL=20 \
  -v /home/admin/apps/adws/pgadmin/data:/var/lib/pgadmin:Z \
  -v /apps/adws/pgadmin/servers.json:/pgadmin4/servers.json:Z \
  pgadmin4-clean-squashed:8.10
