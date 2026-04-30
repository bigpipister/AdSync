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
  postgres:16

# Jenkins
podman run -d --name jenkins \
  --network=host \
  -e TZ=Asia/Taipei \
  # U是無根模式，它會自動將宿主機掛載目錄的擁有者，與容器內的身份做對應
  -v /apps/adws/jenkins/workspace:/var/jenkins_home:Z,U \
  jenkins:latest

# PgAdmin4
podman run -d --name pgadmin4 \
  --network=host \
  --user $(id -u admin):$(id -g root) \
  -e TZ=Asia/Taipei \
  -e PGADMIN_DEFAULT_EMAIL=admin@cht.com.tw \
  -e PGADMIN_DEFAULT_PASSWORD=1qaz@WSX \
  -e PGADMIN_LISTEN_ADDRESS=0.0.0.0 \
  -e PGADMIN_LISTEN_PORT=5050 \
  # U是無根模式，它會自動將宿主機掛載目錄的擁有者，與容器內的身份做對應
  -v /apps/adws/pgadmin/data:/var/lib/pgadmin:Z,U \
  -v /apps/adws/pgadmin/log:/var/log/pgadmin:Z,U \
  -v /apps/adws/pgadmin/config_distro.py:/pgadmin4/config_distro.py:Z \
  -v /apps/adws/pgadmin/servers.json:/pgadmin4/servers.json:ro,Z \
  pgadmin4:8.10
