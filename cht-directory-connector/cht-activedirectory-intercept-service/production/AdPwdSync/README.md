安裝機制由 WINSW 與 windows bat 組成

更新步驟:

1. 先確定 C:\AdPwdSync 資料夾是否已存在，
   若存在，將安裝包裡的 cht-ad-pwd-filter-service-1.0.0.jar 直接覆蓋原檔，
   若遇到無法覆蓋的情形，打開工作管理員找到 java.exe 程序直接結束工作，然後後再覆蓋一次

2. 確認 ad-pwd-sync-1.0.xml 內 arguments 標籤裡的{db_address} {db_account} {db_password}
   參數是否正確

3. windows service 找到 AdPwdSync 服務看是否狀態為執行中，若不是請手動啟動，
   若啟動失敗請連絡開發人員。

4. 若啟動成功則在 ad 執行密碼變更測試，並確認是否有同步到外網

安裝步驟

1. 先確定 C:\AdPwdSync 資料夾不存在

2. 將 ad-pwd-sync-1.0.xml 內 arguments 標籤裡的參數改成所安裝區局對應的正確參數
   需修改三個參數 {db_address} {db_account} {db_password}

3. 將整個AdPwdSync資料夾複製到 C:\ 下 ， 並執行 install.bat

4. 打開 windows service 查看是否有出現 AdPwdSync 服務

5. 確認 AdPwdSync 服務是否狀態為執行中，若不是請手動啟動，若啟動失敗請連絡開發人員

6. 將server開新啟動(安裝的DLL才會生效)

7. 在 ad 執行密碼變更測試，並確認是否有同步到外網

install.bat 所做的事(這裡看看就好):

1. 執行 ad-pwd-sync-1.0.exe install , ad-pwd-sync-1.0.exe 會讀取 ad-pwd-sync-1.0.xml 並依據裡面的設定將執行程式包裝成 windows service

2. 將 windows service "AdPwdSync" 啟動

3. 讀取 HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Lsa 下的 "Notification Packages" 機碼值

4. 判斷原機碼值是否已註冊 ADPWDSYNC，若無則將 "ADPWDSYNC" append 在原機碼值後並寫回

5. 將 ADPWDSYNC.dll 複製到 C:\Windows\System32 裡