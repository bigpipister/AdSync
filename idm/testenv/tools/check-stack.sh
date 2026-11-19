#!/bin/bash
# 整體健康檢查：DB 資料、Jenkins 啟動狀態、資源用量
cd "$(dirname "$0")/.."
psqlc() { docker exec "$1" psql -U "$2" -d idmdb -Atc "$3" 2>&1; }
echo "== containers"; docker compose --profile pwd ps --format 'table {{.Service}}\t{{.State}}\t{{.Status}}'
for z in "pg-in inidm" "pg-ce ceidm" "pg-ex exidm"; do
  set -- $z
  echo "== $1 ($2)"
  psqlc adsync-$1-1 "$2" "select 'person='||count(*)||' placeholders='||string_agg(distinct placeholder,',') from connector_space_ad_person_sync_details" 2>&1 | head -3
  psqlc adsync-$1-1 "$2" "select 'person_details='||count(*) from connector_space_ad_person_details" | head -2
  psqlc adsync-$1-1 "$2" "select 'sample dn: '||dn from connector_space_ad_person_details limit 1" | head -2
done
echo "== jenkins"
docker exec adsync-jenkins-1 java -version 2>&1 | head -2
docker logs adsync-jenkins-1 2>&1 | grep -E 'Jenkins is fully up|Failed Loading plugin|failed to load|SEVERE|Exception' | sort | uniq -c | sort -rn | head -15
docker logs adsync-jenkins-1 2>&1 | grep -E 'Jenkins \(?[0-9.]+|Started initialization|Completed initialization' | head -5
echo "== resources"; docker stats --no-stream --format 'table {{.Name}}\t{{.MemUsage}}\t{{.CPUPerc}}' $(docker compose --profile pwd ps -q); free -h | head -2
