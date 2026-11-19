# 官方 Jenkins LTS + JDK 21（JDK 17 版已於 2.541.3 後停止更新）
FROM docker.io/jenkins/jenkins:lts-jdk21

USER root

# CVE-2026-114938 (Java4Shell)：將內建 Temurin 換成已修正的 Amazon Corretto
# 沿用 /opt/java/openjdk 路徑，JAVA_HOME / PATH 不需變動
ARG CORRETTO_VER=21.0.13.1.2
RUN set -eux; \
    curl -fsSL -o /tmp/jdk.tar.gz \
      "https://corretto.aws/downloads/resources/${CORRETTO_VER}/amazon-corretto-${CORRETTO_VER}-linux-x64.tar.gz"; \
    rm -rf /opt/java/openjdk; \
    mkdir -p /opt/java/openjdk; \
    tar -xzf /tmp/jdk.tar.gz -C /opt/java/openjdk --strip-components=1; \
    rm -f /tmp/jdk.tar.gz; \
    java -version

# 安裝 PostgreSQL client（中介 psql 同步 job 使用）
RUN apt-get update && \
    apt-get install -y postgresql-client && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 外掛：正式環境 jenkins_home 現有的全部外掛（含 mailer / email-ext），預載與此版 core 相容的最新版
# 正式環境離線載入 image，舊 jenkins_home 的外掛無法線上更新；
# 外掛版本以 image 為準：全部改名為 *.jpi.override，每次啟動都覆蓋 jenkins_home/plugins 的同名外掛
# （不用 PLUGINS_FORCE_UPGRADE：jenkins-support 的 versionLT 會把 0.3.0.1-29 判成比 0.3.0-4 舊，
#   導致 eddsa-api 不升級，連帶 trilead/mina-sshd/git 等外掛載入失敗）
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt && \
    for f in /usr/share/jenkins/ref/plugins/*.jpi; do mv "$f" "$f.override"; done

RUN set -eux; \
    # 帳務檔，容器內不需要
    for f in /var/log/lastlog /var/log/wtmp /var/log/btmp; do \
      [ -e "$f" ] && rm -f "$f" || true; \
    done; \
    # shadow 相關檔改為 root 群組並收緊權限（避免 gid=42）
    for f in /etc/shadow /etc/shadow- /etc/gshadow /etc/gshadow- \
             /usr/sbin/unix_chkpwd /sbin/unix_chkpwd \
             /usr/bin/chage /usr/bin/expiry; do \
      [ -e "$f" ] && chgrp 0 "$f" && chmod 640 "$f" || true; \
    done; \
    # 清各發行版快取
    rm -rf /var/cache/apt/archives /var/cache/apt/lists \
           /var/cache/dnf /var/cache/yum \
           /var/cache/apk /lib/apk/db/* || true

USER jenkins
