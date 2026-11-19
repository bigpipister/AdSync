#!/bin/bash
# 啟動全部服務、等 Jenkins 起來後做健康檢查
cd "$(dirname "$0")/.."
docker compose up -d 2>&1 | grep -E "Started|Running|rror"
for i in $(seq 1 90); do
  docker logs adsync-jenkins-1 2>&1 | grep -q "Jenkins is fully up" && break
  sleep 5
done
tools/check-stack.sh
