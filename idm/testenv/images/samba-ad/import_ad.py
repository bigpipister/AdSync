#!/usr/bin/env python3
"""將 Get-ADOrganizationalUnit/Get-ADGroup/Get-ADUser 匯出的 CSV 匯入 Samba AD（直接寫 sam.ldb，samba 未啟動時執行）。

用法：
  import_ad.py --sam sam.ldb --seed DIR --src-domaindn DC=intbca,DC=gov,DC=tw --dst-domaindn DC=entbca,DC=gov,DC=tw \
               --upn-suffix entbca.gov.tw --password 'xxx'
  import_ad.py --sam sam.ldb --bind-dn CN=xx,OU=AP,DC=... --bind-pass 'xxx' --dst-domaindn DC=...
"""
import argparse
import csv
import re
import sys

import ldb
from samba.auth import system_session
from samba.param import LoadParm
from samba.samdb import SamDB

csv.field_size_limit(sys.maxsize)

SKIP_PREFIX = ("Microsoft.ActiveDirectory", "System.")
USER_ATTRS = {  # CSV 欄位(小寫) -> LDAP 屬性
    "displayname": "displayName", "sn": "sn", "givenname": "givenName", "title": "title",
    "department": "department", "employeeid": "employeeID", "mail": "mail", "pager": "pager",
    "description": "description", "company": "company", "userparameters": "userParameters",
    "homemdb": "homeMDB", "physicaldeliveryofficename": "physicalDeliveryOfficeName",
    "telephonenumber": "telephoneNumber",
}
USER_ATTRS.update({f"extensionattribute{i}": f"extensionAttribute{i}" for i in range(1, 16)})
GROUP_ATTRS = {"displayname": "displayName", "description": "description", "mail": "mail"}
GROUP_ATTRS.update({f"extensionattribute{i}": f"extensionAttribute{i}" for i in range(10, 16)})
GROUP_SCOPE = {"global": 0x2, "domainlocal": 0x4, "universal": 0x8}


def val(row, key):
    v = row.get(key)
    if v is None:
        return None
    v = v.strip()
    if not v or v.startswith(SKIP_PREFIX):
        return None
    return v


def read_csv(path):
    with open(path, encoding="utf-8-sig", newline="") as f:
        return [{k.lower(): v for k, v in r.items() if k} for r in csv.DictReader(f)]


def make_dn_mapper(src, dst):
    pat = re.compile(re.escape(src) + r"$", re.IGNORECASE)
    return lambda dn: pat.sub(dst, dn.strip())


def unicode_pwd(pw):
    return ('"%s"' % pw).encode("utf-16-le")


def exists(samdb, dn):
    try:
        return len(samdb.search(base=dn, scope=ldb.SCOPE_BASE, attrs=["dn"])) == 1
    except ldb.LdbError:
        return False


def ensure_ou_path(samdb, dn, domaindn):
    """確保 dn 的所有上層 OU 存在（容器 CN=... 不自動建）。"""
    parts = ldb.Dn(samdb, dn)
    chain = []
    cur = parts.parent()
    while cur is not None and str(cur).lower() != domaindn.lower():
        chain.append(str(cur))
        cur = cur.parent()
    for p in reversed(chain):
        if p.upper().startswith("OU=") and not exists(samdb, p):
            samdb.add({"dn": p, "objectClass": "organizationalUnit", "ou": p.split(",", 1)[0][3:]})


def add_user(samdb, dn, sam, attrs, password, uac):
    msg = {"dn": dn, "objectClass": "user", "sAMAccountName": sam}
    msg.update(attrs)
    try:
        samdb.add(dict(msg, unicodePwd=unicode_pwd(password), userAccountControl=str(uac)))
    except ldb.LdbError:
        # 部分 UAC/密碼組合 add 時不被接受：先以停用狀態建立再設密碼與 UAC
        if exists(samdb, dn):
            samdb.delete(dn)
        samdb.add(dict(msg, userAccountControl="514"))
        samdb.setpassword("(distinguishedName=%s)" % ldb.binary_encode(dn), password,
                          force_change_at_next_login=False, username=None)
        m = ldb.Message(ldb.Dn(samdb, dn))
        m["userAccountControl"] = ldb.MessageElement(str(uac), ldb.FLAG_MOD_REPLACE, "userAccountControl")
        try:
            samdb.modify(m)
        except ldb.LdbError as e:
            print(f"  ! {sam}: keep UAC 514 ({e})")


def import_seed(samdb, a):
    mapdn = make_dn_mapper(a.src_domaindn, a.dst_domaindn)
    ous = read_csv(f"{a.seed}/ADOUs_Export.csv")
    groups = read_csv(f"{a.seed}/ADGroups_Export.csv")
    users = read_csv(f"{a.seed}/ADUsers_Export.csv")
    stats = {"ou": 0, "group": 0, "user": 0, "member": 0, "fail": 0}

    # 1) OU（由淺到深）
    for r in sorted(ous, key=lambda r: r["distinguishedname"].count(",")):
        dn = mapdn(r["distinguishedname"])
        if exists(samdb, dn):
            continue
        try:
            ensure_ou_path(samdb, dn, a.dst_domaindn)
            msg = {"dn": dn, "objectClass": "organizationalUnit", "ou": val(r, "name") or dn.split(",", 1)[0][3:]}
            for k, attr in (("displayname", "displayName"), ("description", "description")):
                if val(r, k):
                    msg[attr] = val(r, k)
            samdb.add(msg)
            stats["ou"] += 1
        except ldb.LdbError as e:
            stats["fail"] += 1
            print(f"  ! OU {dn}: {e}")

    # 2) 群組
    gname2dn = {}
    for r in groups:
        dn = mapdn(r["distinguishedname"])
        sam = val(r, "samaccountname") or val(r, "name")
        gname2dn[(val(r, "name") or "").lower()] = dn
        gname2dn[sam.lower()] = dn
        if exists(samdb, dn):
            continue
        scope = GROUP_SCOPE.get((val(r, "groupscope") or "global").lower(), 0x2)
        if (val(r, "groupcategory") or "Security").lower() == "security":
            scope |= 0x80000000
        gt = scope - (1 << 32) if scope >= (1 << 31) else scope
        msg = {"dn": dn, "objectClass": "group", "sAMAccountName": sam, "groupType": str(gt)}
        for k, attr in GROUP_ATTRS.items():
            if val(r, k):
                msg[attr] = val(r, k)
        try:
            ensure_ou_path(samdb, dn, a.dst_domaindn)
            samdb.add(msg)
            stats["group"] += 1
        except ldb.LdbError as e:
            stats["fail"] += 1
            print(f"  ! group {dn}: {e}")

    # 3) 使用者
    members = {}
    for i, r in enumerate(users, 1):
        dn = mapdn(r["distinguishedname"])
        sam = val(r, "samaccountname")
        if not sam:
            continue
        attrs = {attr: val(r, k) for k, attr in USER_ATTRS.items() if val(r, k)}
        if "sn" not in attrs and val(r, "surname"):
            attrs["sn"] = val(r, "surname")
        if "mail" not in attrs and val(r, "emailaddress"):
            attrs["mail"] = val(r, "emailaddress")
        attrs["userPrincipalName"] = f"{sam}@{a.upn_suffix}"
        try:
            uac = int(val(r, "useraccountcontrol") or (512 if (val(r, "enabled") or "True") == "True" else 514))
        except ValueError:
            uac = 512
        for g in (val(r, "groupnames") or "").split():
            gdn = gname2dn.get(g.lower())
            if gdn:
                members.setdefault(gdn, []).append(dn)
        if exists(samdb, dn):
            continue
        try:
            ensure_ou_path(samdb, dn, a.dst_domaindn)
            add_user(samdb, dn, sam, attrs, a.password, uac)
            stats["user"] += 1
        except ldb.LdbError as e:
            stats["fail"] += 1
            print(f"  ! user {dn}: {e}")
        if i % 500 == 0:
            print(f"  users {i}/{len(users)}")

    # 群組巢狀
    for r in groups:
        dn = mapdn(r["distinguishedname"])
        for g in (val(r, "groupnames") or "").split():
            gdn = gname2dn.get(g.lower())
            if gdn and gdn != dn:
                members.setdefault(gdn, []).append(dn)

    # 4) 成員
    for gdn, mlist in members.items():
        m = ldb.Message(ldb.Dn(samdb, gdn))
        m["member"] = ldb.MessageElement(sorted(set(mlist)), ldb.FLAG_MOD_ADD, "member")
        try:
            samdb.modify(m)
            stats["member"] += len(set(mlist))
        except ldb.LdbError:
            for one in set(mlist):  # 逐筆加，略過已存在者
                m1 = ldb.Message(ldb.Dn(samdb, gdn))
                m1["member"] = ldb.MessageElement([one], ldb.FLAG_MOD_ADD, "member")
                try:
                    samdb.modify(m1)
                    stats["member"] += 1
                except ldb.LdbError:
                    pass
    print(f"[import] {stats}")


def create_bind_user(samdb, a):
    dn = a.bind_dn
    sam = dn.split(",", 1)[0][3:]
    if not exists(samdb, dn):
        ensure_ou_path(samdb, dn, a.dst_domaindn)
        add_user(samdb, dn, sam, {"displayName": sam}, a.bind_pass, 0x10200)  # NORMAL + DONT_EXPIRE
    da = f"CN=Domain Admins,CN=Users,{a.dst_domaindn}"
    m = ldb.Message(ldb.Dn(samdb, da))
    m["member"] = ldb.MessageElement([dn], ldb.FLAG_MOD_ADD, "member")
    try:
        samdb.modify(m)
    except ldb.LdbError:
        pass
    print(f"[bind] {dn} ready")


def main():
    p = argparse.ArgumentParser()
    p.add_argument("--sam", required=True)
    p.add_argument("--seed")
    p.add_argument("--src-domaindn")
    p.add_argument("--dst-domaindn", required=True)
    p.add_argument("--upn-suffix")
    p.add_argument("--password")
    p.add_argument("--bind-dn")
    p.add_argument("--bind-pass")
    a = p.parse_args()
    lp = LoadParm()
    lp.load_default()
    samdb = SamDB(url=a.sam, session_info=system_session(), lp=lp)
    if a.bind_dn:
        create_bind_user(samdb, a)
    else:
        samdb.transaction_start()
        try:
            import_seed(samdb, a)
            samdb.transaction_commit()
        except Exception:
            samdb.transaction_cancel()
            raise


if __name__ == "__main__":
    main()
