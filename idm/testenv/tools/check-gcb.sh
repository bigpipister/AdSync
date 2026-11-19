#!/bin/bash
# 檢查 Jenkins image 的 GCB 修正是否生效（最終檔案系統 + 各 layer 是否仍殘留）
IMG=${1:-docker.io/jenkins/jenkins:latest}
echo "== 最終檔案系統 ($IMG)"
docker run --rm --user 0 --entrypoint sh "$IMG" -c '
for f in /var/log/lastlog /var/log/wtmp /var/log/btmp; do [ -e $f ] && echo "  存在(應刪除) $f" || echo "  OK 已刪除 $f"; done
for f in /etc/shadow /etc/shadow- /etc/gshadow /etc/gshadow- /usr/sbin/unix_chkpwd /sbin/unix_chkpwd /usr/bin/chage /usr/bin/expiry; do
  [ -e $f ] && echo "  $(stat -c "%A %U:%G(gid=%g)" $f) $f" || echo "  (不存在) $f"; done
echo "  apt cache: $(ls /var/cache/apt/archives 2>/dev/null | wc -l) 個, lists: $(ls /var/lib/apt/lists 2>/dev/null | wc -l) 個"
echo "  gid=42 的檔案: $(find / -xdev -group 42 2>/dev/null | wc -l)"
'
echo "== image layer 數: $(docker image inspect -f '{{len .RootFS.Layers}}' "$IMG")"
echo "== 下層 layer 是否仍含被刪檔案（未 squash 時會殘留）"
tmp=$(mktemp -d); docker save "$IMG" -o "$tmp/i.tar"; mkdir "$tmp/x"; tar -xf "$tmp/i.tar" -C "$tmp/x"
n=0; for b in "$tmp"/x/blobs/sha256/*; do
  if tar -tf "$b" 2>/dev/null | grep -qE '^(etc/shadow|var/log/lastlog|var/log/wtmp|var/log/btmp)$'; then n=$((n+1)); fi
done
echo "  含 shadow/lastlog/wtmp/btmp 的 layer 數: $n"
rm -rf "$tmp"
