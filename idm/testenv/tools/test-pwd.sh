#!/bin/bash
# 模擬 adpwdfilter.dll：POST {"name":sAMAccountName,"password":新密碼} 到內網 DC 上的攔截服務
#   tools/test-pwd.sh [sAMAccountName] [新密碼]
cd "$(dirname "$0")/.."
user=${1:-NB01146}; pw=${2:-'Pwd\Test#2026'}   # 預設含反斜線，與 DLL 一樣不跳脫
docker compose --profile pwd up -d interceptor-in 2>&1 | grep -E 'Started|rror'
# 等 3888 可連線（log 主要寫到 /data 下的檔案，不看 console）
for i in $(seq 1 60); do
  code=$(docker exec adsync-interceptor-in-1 curl -s -o /dev/null -w '%{http_code}' http://127.0.0.1:3888/adws/status 2>/dev/null)
  [ -n "$code" ] && [ "$code" != 000 ] && break
  [ "$(docker inspect -f '{{.State.Running}}' adsync-interceptor-in-1)" != true ] && { echo "interceptor exited"; docker logs --tail 20 adsync-interceptor-in-1; exit 1; }
  sleep 3
done
echo "interceptor http status: $code"
LOGF=$(ls -t volumes/interceptor-in/*logback* 2>/dev/null | head -1)
q() { docker exec adsync-pg-in-1 psql -U inidm -d idmdb -Atc "$1" 2>/dev/null; }
before=$(q "select coalesce(to_char(pwdlastset,'YYYY-MM-DD HH24:MI:SS'),'null') from inidm.connector_space_ad_person_details where samaccountname='$user' and placeholder='inner'")
# DLL 的請求：Content-type:application/json，body 直接字串串接（不跳脫）
body=$(printf '{"name":"%s","password":"%s"}' "$user" "$pw")
echo "POST body: $body"
# interceptor 綁 127.0.0.1（server.address），與 DLL 相同由 DC 本機送出
resp=$(docker exec adsync-interceptor-in-1 curl -s -X POST http://127.0.0.1:3888/adws/ -H 'Content-type:application/json' --data-binary "$body")
echo "response: $resp"
sleep 1
after=$(q "select coalesce(to_char(pwdlastset,'YYYY-MM-DD HH24:MI:SS'),'null')||' hash='||left(coalesce(unicodepwd_hash,''),16) from inidm.connector_space_ad_person_details where samaccountname='$user' and placeholder='inner'")
echo "pwdlastset before: $before"
echo "pwdlastset after : $after"
echo "audit: $(q "select result||' | '||activitydisplayname||' | '||left(additionaldetails,60) from inidm.audit_logs where loggedbyservice='ad-pwd-filter' order by activitydatetime desc limit 1")"
{ docker logs adsync-interceptor-in-1 2>&1; [ -n "$LOGF" ] && cat "$LOGF"; } | grep -E 'receive request|update cn|can.t find|ERROR|Exception' | grep -v LevelChange | tail -6 | cut -c1-220
