#!/usr/bin/env python3
"""將攤平匯出的 job（jobs/<站台>/<區域>/<job>/config.xml）轉成 Jenkins 資料夾結構匯入 jenkins_home。

  import-jobs.py <匯出的 jobs 目錄> <jenkins_home> [站台 ...]   （預設 B01）

- 站台、區域建立 cloudbees-folder 的 Folder
- 測試環境封閉：含 email-ext 寄信 publisher 的 job 一律停用（避免寄出通知信）
"""
import os
import re
import shutil
import sys

FOLDER_XML = """<?xml version='1.1' encoding='UTF-8'?>
<com.cloudbees.hudson.plugins.folder.Folder plugin="cloudbees-folder">
  <description>{desc}</description>
  <properties/>
  <folderViews class="com.cloudbees.hudson.plugins.folder.views.DefaultFolderViewHolder">
    <views>
      <hudson.model.AllView>
        <owner class="com.cloudbees.hudson.plugins.folder.Folder" reference="../../../.."/>
        <name>All</name>
        <filterExecutors>false</filterExecutors>
        <filterQueue>false</filterQueue>
        <properties class="hudson.model.View$PropertyImpl"/>
      </hudson.model.AllView>
    </views>
    <tabBar class="hudson.views.DefaultViewsTabBar"/>
  </folderViews>
  <healthMetrics/>
  <icon class="com.cloudbees.hudson.plugins.folder.icons.StockFolderIcon"/>
</com.cloudbees.hudson.plugins.folder.Folder>
"""


def folder(path, desc=""):
    os.makedirs(os.path.join(path, "jobs"), exist_ok=True)
    cfg = os.path.join(path, "config.xml")
    if not os.path.exists(cfg):
        with open(cfg, "w", encoding="utf-8") as f:
            f.write(FOLDER_XML.format(desc=desc))


def main():
    src, home = sys.argv[1], sys.argv[2]
    sites = sys.argv[3:] or ["B01"]
    total = disabled_mail = 0
    for site in sites:
        site_src = os.path.join(src, site)
        site_dst = os.path.join(home, "jobs", site)
        folder(site_dst, f"{site}（AdSync 測試環境匯入）")
        for zone in sorted(os.listdir(site_src)):
            zsrc = os.path.join(site_src, zone)
            if not os.path.isdir(zsrc):
                continue
            zdst = os.path.join(site_dst, "jobs", zone)
            folder(zdst)
            for job in sorted(os.listdir(zsrc)):
                cfg = os.path.join(zsrc, job, "config.xml")
                if not os.path.isfile(cfg):
                    continue
                jdst = os.path.join(zdst, "jobs", job)
                os.makedirs(jdst, exist_ok=True)
                xml = open(cfg, encoding="utf-8").read()
                if "ExtendedEmailPublisher" in xml or "hudson.tasks.Mailer" in xml:
                    xml = re.sub(r"<disabled>\w+</disabled>", "<disabled>true</disabled>", xml, count=1)
                    disabled_mail += 1
                    print(f"  disabled (sends mail): {site}/{zone}/{job}")
                with open(os.path.join(jdst, "config.xml"), "w", encoding="utf-8") as f:
                    f.write(xml)
                nb = os.path.join(zsrc, job, "nextBuildNumber")
                if os.path.isfile(nb):
                    shutil.copy(nb, jdst)
                total += 1
    print(f"[jobs] imported {total} jobs from {sites}, mail jobs disabled: {disabled_mail}")


if __name__ == "__main__":
    main()
