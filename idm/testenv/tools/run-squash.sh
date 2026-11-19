#!/bin/bash
# 執行 build-squash-oci.sh，完整輸出存 logs/，畫面只顯示重點
cd "$(dirname "$0")/.."
mkdir -p logs
log=logs/build-squash-$(date +%Y%m%d-%H%M%S).log
echo "-- 執行前狀態"
docker images --format '{{.Repository}}:{{.Tag}} {{.ID}}' | grep -E 'jenkins'
ls -la export 2>/dev/null
docker ps -a --filter ancestor=quay.io/buildah/stable:latest --format '{{.ID}} {{.Status}}'
echo "-- 執行 (log: $log)"
tools/build-squash-oci.sh > "$log" 2>&1
rc=$?
grep -vE 'Copying blob|Copying config|Writing manifest|Storing signatures|Getting image source|^Trying|^Pulling|^[0-9a-f]{64}$' "$log"
echo "exit=$rc"
exit $rc
