@echo off

cd /d %~dp0

net stop AdPwdSync
ad-pwd-sync-1.0.exe uninstall

setlocal ENABLEEXTENSIONS
set KEY_NAME="HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Lsa"
set VALUE_NAME="Notification Packages"

for /F "usebackq skip=2 tokens=1-4" %%A in (`REG QUERY %KEY_NAME% /v %VALUE_NAME% 2^>nul`) do (
    set ValueName=%%A %%B
    set ValueType=%%C
    set ValueValue=%%D
)

if defined ValueName (
	@echo get ad password filter register information!
    @echo Value Name = %ValueName%
    @echo Value Type = %ValueType%
    @echo Value Value = %ValueValue%
) else (
    @echo %KEY_NAME%\%VALUE_NAME% not found.
)

echo %ValueValue%|findstr "ADPWDSYNC" >nul
if %errorlevel% equ 0 (
	REG ADD "HKLM\SYSTEM\CurrentControlSet\Control\Lsa" /v "Notification Packages" /t REG_MULTI_SZ /d "%ValueValue:\0ADPWDSYNC=%" /f
	@echo unregistry ADPWDSYNC.dll removed!
	cd %windir%\system32\
	del /F ADPWDSYNC.dll
    @echo ADPWDSYNC.dll removed!
)

pause
