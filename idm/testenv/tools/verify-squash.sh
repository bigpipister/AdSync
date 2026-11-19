#!/bin/bash
# 驗證 squash 後的 image：設定保留、GCB、單層內容，並以它啟動測試環境跑 job
cd "$(dirname "$0")/.."
NEW=docker.io/jenkins/jenkins:latest; OLD=adsync/jenkins:layered
echo "== image 設定比對（舊多層 vs 新單層）"
for k in '{{.Config.User}}' '{{json .Config.Entrypoint}}' '{{json .Config.Env}}' '{{json .Config.ExposedPorts}}' '{{json .Config.Volumes}}' '{{.Config.WorkingDir}}'; do
  a=$(docker image inspect -f "$k" $OLD); b=$(docker image inspect -f "$k" $NEW)
  [ "$a" = "$b" ] && echo "  same  $k" || { echo "  DIFF  $k"; echo "    old: $a"; echo "    new: $b"; }
done
echo "  layers: old=$(docker image inspect -f '{{len .RootFS.Layers}}' $OLD) new=$(docker image inspect -f '{{len .RootFS.Layers}}' $NEW)"

echo "== 單層內容檢查（image 內實際存放的檔案）"
tmp=$(mktemp -d); docker save $NEW -o $tmp/i.tar; mkdir $tmp/x; tar -xf $tmp/i.tar -C $tmp/x
for b in $tmp/x/blobs/sha256/*; do
  tar -tvf "$b" 2>/dev/null | grep -qE ' etc/shadow$' || continue
  echo "  layer $(basename $b | cut -c1-12):"
  tar -tvf "$b" | grep -E ' (etc/shadow|etc/gshadow|usr/bin/chage|usr/bin/expiry|var/log/lastlog|var/log/wtmp|var/log/btmp)$' | awk '{print "    "$1, $2, $NF}'
  echo "    lastlog/wtmp/btmp 筆數: $(tar -tf "$b" | grep -cE '^var/log/(lastlog|wtmp|btmp)$')"
done
rm -rf $tmp

echo "== 最終檔案系統 GCB"
tools/check-gcb.sh $NEW 2>/dev/null | sed -n '2,15p'

echo "== 以單層 image 啟動測試環境"
docker compose up -d >/dev/null 2>&1
for i in $(seq 1 90); do docker logs adsync-jenkins-1 2>&1 | grep -q 'Jenkins is fully up' && break; sleep 5; done
echo "  jenkins image : $(docker inspect -f '{{.Image}}' adsync-jenkins-1 | cut -c1-19) (new=$(docker image inspect -f '{{.Id}}' $NEW | cut -c1-19))"
echo "  java          : $(docker exec adsync-jenkins-1 java -version 2>&1 | sed -n 2p)"
echo "  plugin errors : $(docker logs adsync-jenkins-1 2>&1 | grep -cE 'SEVERE|Failed Loading plugin')"
tools/pg-refresh-collation.sh --if-needed | grep -v OK
tools/run-job.sh B01 內網 '擷取內網AD資料(B01ROOT)' | grep exit=
tools/run-job.sh B01 外網 '擷取外網AD資料(B01ROOT)' | grep exit=
tools/run-job.sh B01 中介 '同步內網資料庫到中介(psql)' | grep exit=
