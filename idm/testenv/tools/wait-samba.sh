#!/bin/bash
# 啟動/重建指定 samba 服務並等待初始化結果：tools/wait-samba.sh samba-in [--rebuild]
cd "$(dirname "$0")/.."
svc=${1:?service}; c=adsync-$svc-1
[ "${2:-}" = "--rebuild" ] && docker compose build -q "$svc"
docker compose up -d "$svc" >/dev/null 2>&1
for i in $(seq 1 120); do
  logs=$(docker logs "$c" 2>&1)
  if echo "$logs" | grep -qE 'init done|Traceback|ERR:'; then break; fi
  [ "$(docker inspect -f '{{.State.Running}}' "$c")" != true ] && break
  sleep 5
done
echo "== $svc running=$(docker inspect -f '{{.State.Running}}' "$c")"
docker logs "$c" 2>&1 | grep -E '\[adsync\]|\[import\]|\[bind\]|users [0-9]|  !|ERR|rror|Traceback|line [0-9]+' | head -80
