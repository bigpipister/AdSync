# 測試用 AD DC（Samba 4），模擬內/外網 Windows AD
FROM docker.io/library/ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
      samba samba-ad-dc samba-ad-provision samba-dsdb-modules samba-vfs-modules \
      winbind python3-samba ldb-tools ldap-utils acl attr krb5-config krb5-user \
      ca-certificates tzdata && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY schema-ext.ldif.tmpl import_ad.py entrypoint.sh /opt/adsync/
RUN chmod +x /opt/adsync/entrypoint.sh

EXPOSE 389 636
ENTRYPOINT ["/opt/adsync/entrypoint.sh"]
