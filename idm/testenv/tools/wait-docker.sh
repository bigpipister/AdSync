#!/bin/bash
# 等 docker daemon 起來（最多 3 分鐘），並顯示目前狀態
for i in $(seq 1 36); do
  docker info >/dev/null 2>&1 && break
  sleep 5
done
if ! docker info >/dev/null 2>&1; then
  echo "docker 仍未啟動"
  echo "systemd docker: $(systemctl is-active docker 2>&1) / enabled: $(systemctl is-enabled docker 2>&1)"
  ls -la /var/run/docker.sock 2>&1
  exit 1
fi
echo "docker OK: $(docker version --format '{{.Server.Version}}')"
cd ~/AdSync
docker compose --profile pwd ps -a --format '{{.Service}}={{.State}}' | paste -sd' '
docker images --format '{{.Repository}}:{{.Tag}} {{.ID}}' | grep -E 'adsync/jenkins|jenkins/jenkins'
free -h | sed -n 2p
