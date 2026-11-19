#!/bin/bash
# 首次啟動：provision 網域 -> 擴充 schema(extensionAttribute*/homeMDB) -> 匯入 CSV -> 建立 bind 帳號
# 之後啟動：直接以 volume 內既有資料啟動 samba
set -euo pipefail

: "${REALM:?}"            # 例 INTBCA.GOV.TW
: "${NETBIOS:?}"          # 例 INTBCA
: "${ADMIN_PASS:?}"
DOMAINDN="DC=$(echo "${REALM,,}" | sed 's/\./,DC=/g')"
PRIV=/var/lib/samba/private
PERSIST_CONF=/var/lib/samba/smb.conf.persist

MARKER=/var/lib/samba/.adsync-initialized
if [ ! -f "$MARKER" ]; then
  echo "[adsync] provisioning $REALM ($DOMAINDN)"
  # 上次初始化未完成則清掉重來
  find /var/lib/samba -mindepth 1 -delete 2>/dev/null || true
  rm -f /etc/samba/smb.conf
  samba-tool domain provision \
    --realm="$REALM" --domain="$NETBIOS" --server-role=dc \
    --dns-backend=SAMBA_INTERNAL --adminpass="$ADMIN_PASS" \
    --host-name="$(hostname -s)" --use-rfc2307 \
    --option="dsdb:schema update allowed = true"
  cp /etc/samba/smb.conf "$PERSIST_CONF"

  # 測試方便：密碼可立即再變更、不過期（複雜度維持開啟，與 AD 一致）
  samba-tool domain passwordsettings set --min-pwd-age=0 --max-pwd-age=0 --history-length=0

  echo "[adsync] extending schema"
  # 分三段（屬性 -> 輔助類別 -> 掛到 user/group），每段獨立程序執行，才會重新載入 schema
  sed "s/@DOMAINDN@/$DOMAINDN/g" /opt/adsync/schema-ext.ldif.tmpl \
    | awk 'BEGIN{n=1} /^#--SPLIT--/{n++; next} {print > ("/tmp/schema-" n ".ldif")}'
  for s in /tmp/schema-1.ldif /tmp/schema-2.ldif /tmp/schema-3.ldif; do
    ldbmodify -H "$PRIV/sam.ldb" --option="dsdb:schema update allowed = true" "$s"
  done

  if [ -n "${SEED_DIR:-}" ] && [ -f "$SEED_DIR/ADUsers_Export.csv" ]; then
    echo "[adsync] importing AD objects from $SEED_DIR"
    python3 /opt/adsync/import_ad.py \
      --sam "$PRIV/sam.ldb" --seed "$SEED_DIR" \
      --src-domaindn "${SEED_SRC_DOMAINDN:-$DOMAINDN}" --dst-domaindn "$DOMAINDN" \
      --upn-suffix "${REALM,,}" --password "${SEED_USER_PASS:-!QAZxsw2#EDC}"
  fi

  # sync-connector 連線用的 bind 帳號（對應 application-*.yml 的 spring.ldap.username）
  if [ -n "${BIND_USER_DN:-}" ]; then
    echo "[adsync] creating bind user $BIND_USER_DN"
    python3 /opt/adsync/import_ad.py --sam "$PRIV/sam.ldb" \
      --bind-dn "$BIND_USER_DN" --bind-pass "${BIND_USER_PASS:?}" --dst-domaindn "$DOMAINDN"
  fi
  touch "$MARKER"
  echo "[adsync] init done"
else
  [ -f "$PERSIST_CONF" ] && cp "$PERSIST_CONF" /etc/samba/smb.conf
fi

exec samba --foreground --no-process-group --debug-stdout
