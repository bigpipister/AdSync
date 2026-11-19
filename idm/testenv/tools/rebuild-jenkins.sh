#!/bin/bash
# 重 build Jenkins image 並重啟，檢查外掛載入結果
cd "$(dirname "$0")/.."
docker compose build jenkins 2>&1 | grep -E 'naming to|ERROR|rror:' | tail -5
docker compose up -d jenkins pg-ce 2>&1 | grep -E 'Started|rror'
for i in $(seq 1 120); do
  docker logs adsync-jenkins-1 2>&1 | grep -q 'Jenkins is fully up' && break
  sleep 5
done
echo "jenkins core: $(docker exec adsync-jenkins-1 sh -c 'unzip -p /usr/share/jenkins/jenkins.war META-INF/MANIFEST.MF | grep -i ^Jenkins-Version' | tr -d '\r')"
echo "java: $(docker exec adsync-jenkins-1 java -version 2>&1 | sed -n 2p)"
echo "SEVERE/Failed count: $(docker logs adsync-jenkins-1 2>&1 | grep -cE 'SEVERE|Failed to load')"
docker logs adsync-jenkins-1 2>&1 | grep -E 'SEVERE|Failed to load|fully up' | cut -c1-220 | head -15
