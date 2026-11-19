#!/bin/bash
# AdSync 測試環境管理
#   ./adsync.sh init      從 seed 還原 Jenkins home / 三個 DB、準備 app 設定、build image（只需一次，或 reset 後）
#   ./adsync.sh up        啟動（加 --pwd 一併啟動密碼攔截服務）
#   ./adsync.sh stop      全部停止（資料保留）
#   ./adsync.sh status    狀態
#   ./adsync.sh reset     刪除 volumes（回到 seed 初始狀態，需再 init）
#   ./adsync.sh pg-reset  只把三個 DB 重置回 seed 狀態
#   ./adsync.sh deploy-jar <sync-connector.jar>  換 jar
set -euo pipefail
cd "$(dirname "$0")"
DC="docker compose"
# 以 root 容器處理檔案擁有者（本機無 sudo 也可）
asroot() { docker run --rm -v "$PWD:/w" -w /w docker.io/library/alpine:3.20 sh -c "$*"; }

restore_pg() {  # $1=seed 目錄名 $2=volume 名
  # 資料目錄為 999:700，主機使用者看不到內容，必須以 root 容器判斷是否已還原
  if asroot "test -f volumes/$2/PG_VERSION"; then echo "  $2 已存在，略過"; return; fi
  if [ -n "$($DC ps -q "$2" 2>/dev/null)" ]; then echo "  $2 執行中，請先 stop"; return 1; fi
  echo "  還原 seed/db/$1 -> volumes/$2"
  asroot "mkdir -p volumes/$2 && tar -xf seed/db/$1/idmdb.tar -C volumes/$2 --strip-components=1 && chown -R 999:999 volumes/$2 && chmod 700 volumes/$2"
}

prepare_app() {
  local d=app/activedirectory_sync
  mkdir -p "$d" app/interceptor
  cp -f app/cht-activedirectory-sync-connector-1.0.jar app/adws.jceks app/logback-spring.xml "$d/"
  # 測試用 yml：只改「本機 DB」與「跨網域 bind DN」，其餘與正式 B01 相同
  sed -e 's#jdbc:postgresql://localhost:5432#jdbc:postgresql://10.23.88.98:5432#' \
      -e 's#CN=B01IDMAdmin2,OU=AP,DC=ntbt,DC=gov,DC=tw#CN=B01IDMAdmin2,OU=AP,DC=intbca,DC=gov,DC=tw#' \
      app/application-B01in.yml > "$d/application-B01in.yml"
  sed -e 's#jdbc:postgresql://localhost:5432#jdbc:postgresql://10.22.242.98:5432#' \
      app/application-B01ex.yml > "$d/application-B01ex.yml"
  cp -f app/cht-activedirectory-intercept-service-1.0.jar app/adws.jceks app/interceptor/
}

case "${1:-}" in
  init)
    mkdir -p volumes/samba-in volumes/samba-ex volumes/interceptor-in
    echo "[1/4] 還原 DB"
    restore_pg inner pg-in
    restore_pg inter pg-ce
    restore_pg outer pg-ex
    echo "[2/4] 還原 Jenkins home"
    if [ ! -f volumes/jenkins_home/config.xml ]; then
      asroot "mkdir -p volumes/jenkins_home && tar -xf seed/jenkins/workspace.tar -C volumes/jenkins_home --strip-components=1 && chown -R 1000:1000 volumes/jenkins_home"
    else echo "  jenkins_home 已存在，略過"; fi
    if [ ! -d volumes/jenkins_home/jobs/${SITE:-B01} ] && [ -f seed/jenkins/jobs.tar ]; then
      echo "  匯入 ${SITE:-B01} jobs"
      rm -rf /tmp/adsync-jobs && mkdir -p /tmp/adsync-jobs
      tar --warning=no-unknown-keyword -xf seed/jenkins/jobs.tar -C /tmp/adsync-jobs
      asroot "chmod 777 volumes/jenkins_home/jobs"
      python3 tools/import-jobs.py /tmp/adsync-jobs/jobs volumes/jenkins_home ${SITE:-B01}
      asroot "chown -R 1000:1000 volumes/jenkins_home/jobs && chmod 755 volumes/jenkins_home/jobs"
    fi
    echo "[3/4] 準備 app 設定"
    prepare_app
    asroot "chown -R 1000:1000 volumes/interceptor-in"
    echo "[4/4] build images"
    $DC --profile pwd build
    ;;
  up)
    if [ "${2:-}" = "--pwd" ]; then $DC --profile pwd up -d; else $DC up -d; fi
    # 舊資料目錄(glibc 2.36)在新 image(glibc 2.41)上需重建索引；僅在偵測到不一致時執行
    sleep 5; tools/pg-refresh-collation.sh --if-needed
    $DC ps
    ;;
  pg-reset)  # 只重置三個 DB 回 seed 狀態
    $DC stop pg-in pg-ce pg-ex
    $DC rm -f pg-in pg-ce pg-ex
    asroot "rm -rf volumes/pg-in volumes/pg-ce volumes/pg-ex"
    restore_pg inner pg-in; restore_pg inter pg-ce; restore_pg outer pg-ex
    $DC up -d pg-in pg-ex pg-ce
    sleep 5; tools/pg-refresh-collation.sh --if-needed
    ;;
  stop)   $DC --profile pwd stop ;;
  status) $DC --profile pwd ps -a; docker stats --no-stream $($DC --profile pwd ps -q) 2>/dev/null || true ;;
  reset)
    $DC --profile pwd down
    asroot "rm -rf volumes"
    echo "volumes 已清除，請重新 ./adsync.sh init"
    ;;
  deploy-jar)
    cp -f "${2:?jar path}" app/cht-activedirectory-sync-connector-1.0.jar
    cp -f app/cht-activedirectory-sync-connector-1.0.jar app/activedirectory_sync/
    echo "已更新 app/activedirectory_sync/cht-activedirectory-sync-connector-1.0.jar"
    ;;
  *) sed -n '2,9p' "$0"; exit 1 ;;
esac
