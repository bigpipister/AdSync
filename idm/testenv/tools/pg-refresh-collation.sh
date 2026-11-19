#!/bin/bash
# postgres:16.12 改為 Debian 13 (glibc 2.41)，舊資料目錄建於 glibc 2.36 → 需重建索引並更新 collation 版本
# 正式環境換新 image 後也要做一次（每個 DB：idmdb / postgres / template1），需以 superuser 執行
#   --if-needed : 只處理仍有 mismatch 警告的 DB
cd "$(dirname "$0")/.."
MODE="${1:-}"
for z in "pg-in inidm" "pg-ce ceidm" "pg-ex exidm"; do
  set -- $z
  svc=$1; c=adsync-$svc-1
  # 以業務帳號查出 superuser（容器內 local socket 為 trust）
  for i in $(seq 1 30); do docker exec "$c" pg_isready -q 2>/dev/null && break; sleep 2; done
  if [ "$MODE" = "--if-needed" ] && \
     [ "$(docker exec "$c" psql -U "$2" -d idmdb -Atc 'select 1' 2>&1 | grep -c 'collation version mismatch')" = 0 ]; then
    echo "== $svc collation OK"; continue
  fi
  su=$(docker exec "$c" psql -U "$2" -d idmdb -Atc "select rolname from pg_roles where rolsuper order by rolname limit 1" 2>/dev/null)
  echo "== $svc (superuser: $su)"
  for db in $(docker exec "$c" psql -U "$su" -d postgres -Atc "select datname from pg_database where datallowconn" 2>/dev/null); do
    docker exec "$c" psql -U "$su" -d "$db" -qc "REINDEX DATABASE \"$db\"" 2>&1 | grep -vE 'collation version|DETAIL|HINT' | grep . || true
    echo "  $db: $(docker exec "$c" psql -U "$su" -d "$db" -Atc "ALTER DATABASE \"$db\" REFRESH COLLATION VERSION" 2>&1 | grep -vE 'collation version|DETAIL|HINT' | tr '\n' ' ')"
  done
  echo "  warning left: $(docker exec "$c" psql -U "$su" -d idmdb -Atc 'select 1' 2>&1 | grep -c 'collation version mismatch')"
done
