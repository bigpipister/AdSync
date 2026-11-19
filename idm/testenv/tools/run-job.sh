#!/bin/bash
# 以 Jenkins job 的原始指令（從 config.xml 取出，不修改）在 jenkins 容器內執行
#   tools/run-job.sh B01 內網 '擷取內網AD資料(B01ROOT)'
#   tools/run-job.sh --list B01
cd "$(dirname "$0")/.."
JH=volumes/jenkins_home/jobs
if [ "$1" = "--list" ]; then
  find "$JH/$2" -name config.xml -path '*/jobs/*/jobs/*' | sed "s#$JH/$2/jobs/##; s#/config.xml##; s#/jobs/# / #"
  exit 0
fi
site=$1; zone=$2; job=$3
cfg="$JH/$site/jobs/$zone/jobs/$job/config.xml"
[ -f "$cfg" ] || { echo "not found: $cfg"; exit 2; }
cmd=$(python3 - "$cfg" <<'PY'
import sys, xml.etree.ElementTree as ET
r = ET.parse(sys.argv[1]).getroot()
print("\n".join(e.text or "" for e in r.iter("command")))
PY
)
log="logs/$(date +%Y%m%d-%H%M%S)_${site}_${zone}_${job//[^A-Za-z0-9_]/_}.log"
mkdir -p logs
echo "### [$site/$zone] $job"
echo "$cmd" | head -3 | cut -c1-250
start=$(date +%s)
# 與 Jenkins 執行 shell step 相同：在 workspace 目錄、以 jenkins 使用者、bash -xe 之外直接執行
docker exec -i -u jenkins -w /var/jenkins_home adsync-jenkins-1 bash -s > "$log" 2>&1 <<EOF
$cmd
EOF
rc=$?
echo "exit=$rc  time=$(( $(date +%s) - start ))s  log=$log"
grep -E ' (ERROR|WARN) |Exception|Caused by|completed|finish|total|count|成功|失敗|完成|處理資料表|UPSERT|INSERT|COPY [0-9]' "$log" | grep -vE '^\s+at ' | cut -c1-230 | tail -25
exit $rc
