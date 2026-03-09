# 參數匯整
scan
```
(1) command
- basedn (service params)
- modifytimestamp (service params)
- doscan (service params)
(2) profile
- placeholder (service params)
- domaindn
- exdomaindn
```
sync
```
(1) command
- scope (service params)(1: OrganizationUnit, 2: OrganizationPerson, 3: Group)
- basedn (service params)(default:mofdn)
- dryrun (service params)
(2) profile
- mofdn
- placeholder
- domaindn
```
clean
```
(1) command
- adbasedn (service params)(default:mofdn)
- removeadbasedn (service params)(default:mofdn)
- removeaduser (service params)(default:removeuser.enabled)
- dryrun (service params)
(2) profile
- mofdn
- placeholder (service params)
- removeAduserMaxlimit
- removeuser.enabled
```
pwd
```
(1) command
- basedn (service params)(default:mofdn)
- dryrun (service params)
(2) profile
- mofdn
- placeholder (service params)
```
# audit log 情境
```
(1) 內網 db 將 person 標記為 delete
(2) 外網 db 將 person 刪除
(3) 外網 ad 將 person 刪除
(4) 外網 ad 刪除的 persion 數大於 max_limit

(5) add member to group
(6) remove member from group

(7) 物件新增、修改、刪除與其例外操作
```
# jenkins job (admin / 1qaz@WSX)
內網
```
擷取內網AD資料(B01ROOT) --> 每五分鐘 --> 標記內網資料庫中已不在AD中的人員

擷取內網AD資料(BELROOT) --> 每五分鐘
```
外網
```
擷取外網AD資料(B01ROOT) --> 每五分鐘 --> 比對內外網AD資料差異並同步至外網AD 
--> 比對內外網AD密碼差異並同步至外網AD --> 比對資料刪除外網資料庫與AD中已不存在的人員

擷取外網AD資料(BELROOT) --> 每五分鐘
```
中介
```
同步內網資料庫到中介 --> 每五分鐘 --> 同步中介資料庫到外網

同步外網資料庫到中介 --> 每五分鐘 --> 同步中介資料庫到內網
```
