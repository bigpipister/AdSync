#!/bin/bash
# 驗證同步結果：各 DB 筆數、psql 中介 job 實際搬移筆數
cd "$(dirname "$0")/.."
q() { docker exec "adsync-$1-1" psql -U "$2" -d idmdb -Atc "$3" 2>/dev/null; }
printf '%-55s %8s %8s %8s\n' table pg-in pg-ce pg-ex
for t in connector_space_ad_person_details connector_space_ad_group_details connector_space_ad_organizationalunit_details \
         connector_space_ad_person_sync_details connector_space_ad_group_sync_details connector_space_ad_organizationalunit_sync_details \
         connector_space_ad_person_external_details status_logs event_logs audit_logs; do
  a=$(q pg-in inidm "select count(*) from inidm.$t" || echo -); b=$(q pg-ce ceidm "select count(*) from ceidm.$t" || echo -); c=$(q pg-ex exidm "select count(*) from exidm.$t" || echo -)
  printf '%-55s %8s %8s %8s\n' "$t" "${a:--}" "${b:--}" "${c:--}"
done
echo "-- status_logs (pg-ex, latest)"
q pg-ex exidm "select region||' | '||jobname||' | '||coalesce(health,'')||' | '||coalesce(to_char(startdatetime,'HH24:MI:SS'),'') from exidm.status_logs order by startdatetime desc limit 6" || \
q pg-ex exidm "select * from exidm.status_logs limit 3"
echo "-- psql job logs (rows moved)"
for f in $(ls -t logs/*psql*.log | head -4); do
  echo "## $f"; grep -E '^(COPY|INSERT|DELETE) [0-9]|ERROR|處理資料表' "$f" | tr '\n' ' ' | cut -c1-400; echo
done
