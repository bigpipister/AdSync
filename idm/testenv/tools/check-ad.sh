#!/bin/bash
# 以 sync-connector 的 bind 帳號走 LDAPS 驗證 AD：tools/check-ad.sh in|ex
cd "$(dirname "$0")/.."
case "${1:-in}" in
  in) c=adsync-samba-in-1; h=10.23.88.105; d='DC=intbca,DC=gov,DC=tw'; u='CN=B01IDMAdmin2,OU=AP,DC=intbca,DC=gov,DC=tw'; p='1qaz@WSX' ;;
  ex) c=adsync-samba-ex-1; h=10.22.242.39; d='DC=entbca,DC=gov,DC=tw'; u='CN=CHTIDMAdmin,OU=ap,DC=entbca,DC=gov,DC=tw'; p='1qaz@WSX3edc' ;;
esac
q() { docker exec -e LDAPTLS_REQCERT=never "$c" ldapsearch -LLL -o ldif-wrap=no -H "ldaps://$h:636" -D "$u" -w "$p" "$@"; }
echo "== $1 ($h) LDAPS bind as $u"
echo "users under MOF : $(q -b "OU=MOF,$d" '(objectClass=user)' dn | grep -c '^dn:')"
echo "groups under MOF: $(q -b "OU=MOF,$d" '(objectClass=group)' dn | grep -c '^dn:')"
echo "OUs under MOF   : $(q -b "OU=MOF,$d" '(objectClass=organizationalUnit)' dn | grep -c '^dn:')"
echo "-- sample user"
q -b "OU=MOF,$d" '(sAMAccountName=NB01146)' sAMAccountName userPrincipalName displayName department extensionAttribute1 extensionAttribute10 homeMDB userAccountControl memberOf objectGUID whenChanged
