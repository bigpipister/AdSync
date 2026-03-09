AD 密碼變更時會觸發註冊在 HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Lsa 的 "Notification Packages" 這個機碼值內的 所有 DLL

它會固定呼叫 DLL 內的 InitializeChangeNotify、PasswordChangeNotify、PasswordFilter 這三個 method

由於初始並沒有特別做什麼，所以 InitializeChangeNotify method 直接 return True

也沒有要做 passwordfilter 所以 PasswordFilter method 直接 return 1

僅在 PasswordChangeNotify method 實做接收到密碼變更時要做的動作

PasswordChangeNotify 流程:

1. 將攔截到的帳號密碼以明碼儲存在 C:\AdPwdSync\temp.db

2. 之後將帳號密碼資訊以 {"name":xxx, "password":xxx} 的格式 post 到 http://localhost/adws/ (之後由 java 程式接手)

開發工具版本: Microsoft Visual Studio Community 2019