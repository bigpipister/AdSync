#!/bin/bash
# 依正式環境 upstream 觸發順序跑完整 B01 同步流程（略過需舊 IDM 主機的「從舊IDM同步密碼」）
cd "$(dirname "$0")/.."
S=${SITE:-B01}
R=${S}ROOT; E=$(echo "$S" | cut -c1)ELROOT
steps=(
  "內網|擷取內網AD資料($R)"
  "內網|擷取內網AD資料($E)"
  "內網|標記內網資料庫中已不在AD中的人員"
  "中介|同步內網資料庫到中介(psql)"
  "中介|同步中介資料庫到外網(psql)"
  "外網|擷取外網AD資料($R)"
  "外網|擷取外網AD資料($E)"
  "外網|比對內外網AD資料差異並同步至外網AD"
  "外網|比對內外網AD密碼差異並同步至外網AD"
  "外網|比對資料刪除外網資料庫與AD中已不存在的人員"
  "中介|同步外網資料庫到中介(psql)"
  "中介|同步中介資料庫到內網(psql)"
)
summary=()
for s in "${steps[@]}"; do
  zone=${s%%|*}; job=${s#*|}
  out=$(tools/run-job.sh "$S" "$zone" "$job" 2>&1)
  rc=$?
  log=$(echo "$out" | grep -o 'log=.*' | cut -d= -f2)
  errs=$( [ -n "$log" ] && grep -cE ' ERROR |Exception' "$log" || echo "-")
  summary+=("$(printf '%-4s %-36s exit=%-3s errors=%s' "$zone" "$job" "$rc" "$errs")")
  echo "$out" | head -2
  echo "$out" | grep -E 'exit='
  [ -n "$log" ] && grep -E ' ERROR |Exception|Caused by' "$log" | grep -vE '^\s+at ' | cut -c1-240 | sort | uniq -c | sort -rn | head -5
  echo
done
echo "================ SUMMARY ================"
printf '%s\n' "${summary[@]}"
