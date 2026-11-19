# AdSync 封閉測試環境（docker compose）

模擬 B01 正式環境：內網 AD / 外網 AD / 內・中介・外三台 PostgreSQL / Jenkins / 密碼攔截服務。
容器 IP 沿用正式環境，Jenkins job 與 application-*.yml 中的 IP 不需修改。

| 服務 | 角色 | 網路 / IP |
|---|---|---|
| samba-in | 內網 AD（intbca.gov.tw，Samba 4 模擬） | net-in 10.23.88.105 |
| samba-ex | 外網 AD（entbca.gov.tw） | net-ex 10.22.242.39 |
| pg-in / pg-ex | 內網 / 外網 IDM DB | 10.23.88.98 / 10.22.242.98 |
| pg-ce | 中介 DB（與 jenkins 共用網路 → job 裡的 localhost:5432） | — |
| jenkins | Jenkins LTS + JDK 21（Corretto 21.0.13.1.2，修補 CVE-2026-114938） | 兩網 .10，UI 綁 127.0.0.1:18080 |
| interceptor-in | 密碼攔截服務（正式環境裝在 DC 上），profile `pwd` | 與 samba-in 共用網路，只聽 127.0.0.1:3888 |

net-in / net-ex 為 internal 網路：互不相通也不能連外。

## 部署到新主機（seed / app 不進 git）

188 的 `~/AdSync` 已備妥。要在別台重建時，從本機把下列檔案放到對應位置後再 `./adsync.sh init`：

| 放到 | 來源（本機 AdSync 資料夾） |
|---|---|
| `seed/ad/ADOUs_Export.csv`、`ADGroups_Export.csv`、`ADUsers_Export.csv` | `idm-doc/IDM exported data/` |
| `seed/db/inner/idmdb.tar`、`seed/db/inter/idmdb.tar`、`seed/db/outer/idmdb.tar` | `idm/idmdb/{inner,inter,outer}/idmdb.tar` |
| `seed/jenkins/workspace.tar` | `idm/jenkins/workspace.tar` |
| `seed/jenkins/jobs.tar` | `idm/jenkins/jobs` 打包：`tar -cf jobs.tar -C idm/jenkins jobs` |
| `app/adws.jceks`、`app/application-B01in.yml`、`app/application-B01ex.yml`、`app/logback-spring.xml` | `idm/jenkins/activedirectory_sync/` |
| `app/cht-activedirectory-sync-connector-1.0.jar` | `cht-directory-connector` 以 JDK 21 `mvn -Pprod package` |
| `app/cht-activedirectory-intercept-service-1.0.jar` | 同上 |

## 使用

```bash
./adsync.sh init          # 第一次：還原 seed（Jenkins home、三個 DB、B01 jobs）、build image
./adsync.sh up            # 啟動（自動處理 PG collation）；./adsync.sh up --pwd 一併起密碼攔截服務
./adsync.sh stop          # 全部停止（資料保留）
./adsync.sh pg-reset      # 只把三個 DB 重置回 seed
./adsync.sh reset         # 全部清掉（含 AD），之後再 init
./adsync.sh deploy-jar /path/to/cht-activedirectory-sync-connector-1.0.jar
```

Jenkins UI：在自己電腦 `ssh -p 11322 -L 18080:127.0.0.1:18080 tsunghsien@220.135.214.188`，瀏覽 http://localhost:18080
（帳號 admin，密碼同正式環境 `idm/jenkins/password.txt`）。B01 job 匯入時維持原本的停用狀態；「系統檢測」因會寄信，強制停用。

## 測試工具

```bash
tools/run-flow.sh                                   # 依 upstream 順序跑完整 B01 流程（以 job 原始指令在 jenkins 容器內執行）
tools/run-job.sh B01 內網 '擷取內網AD資料(B01ROOT)'  # 單一 job
tools/verify.sh                                     # 三個 DB 各表筆數、psql 中介 job 搬移筆數
tools/check-ad.sh in|ex                             # 以 sync-connector bind 帳號走 LDAPS 查 AD
tools/test-pwd.sh [帳號] [新密碼]                    # 模擬 adpwdfilter.dll POST 到攔截服務並檢查 DB
tools/run-squash.sh                                 # 把測過的 Jenkins image 用 buildah 壓成單層，匯出 export/jenkins.oci
                                                    #   tag 為 docker.io/jenkins/jenkins:latest（給正式環境 podman load），並換給測試環境使用
tools/verify-squash.sh                              # 驗證單層 image：設定與多層版一致、GCB、啟動後跑 job
tools/check-gcb.sh [image]                          # 檢查 GCB 修正（lastlog/wtmp/btmp、shadow 權限、gid=42）
```

job 執行 log 在 `logs/`。

## 與正式環境的差異（測試限制）

- **AD 為 Samba 4**：schema 以私有 OID 擴充 `extensionAttribute1-15` / `homeMDB`（homeMDB 為字串語法）。
- **Samba 不允許透過 LDAP 寫 `userParameters`**（Samba bug 8077）：ADWS202X「比對內外網AD資料差異並同步至外網AD」
  修改人員時一定帶 userParameters，在此環境會全部 constraint violation；此路徑需在真實 AD 驗證。
- bind 帳號：內網正式為 `CN=B01IDMAdmin2,OU=AP,DC=ntbt,...`（跨網域），測試改建在 intbca 網域。
- 「從舊IDM同步密碼到新IDM」需要舊 IDM 主機，未模擬。
- AD 資料由 `seed/ad/*.csv`（Get-AD* 匯出）匯入，內外網同一份（外網換 domain）；使用者密碼 `!QAZxsw2#EDC`。

## 升級正式環境時要注意（此環境驗證到的）

1. **新 jar 為 Java 21（class 65）**：正式 Jenkins 必須先換成 JDK 21 image，才能部署新 jar。
2. **Jenkins 2.479 → 2.580 外掛**：舊 jenkins_home 外掛會載入失敗；image 內預載外掛並以 `*.jpi.override` 覆蓋
   （官方 PLUGINS_FORCE_UPGRADE 會因版本比較 bug 漏升 eddsa-api，連帶 git/ssh 類外掛失敗）。
3. **postgres:16.12 改為 Debian 13（glibc 2.41）**：舊資料目錄（glibc 2.36）會出現 collation version mismatch，
   換 image 後需以 superuser 對每個 DB 執行 `REINDEX DATABASE` + `ALTER DATABASE ... REFRESH COLLATION VERSION`
   （見 `tools/pg-refresh-collation.sh`）。
