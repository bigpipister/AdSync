# 查看nat table
iptables -t nat -L -n

# 然後放行你要的 IP
iptables -I DOCKER-USER -s 1.1.1.1 -p tcp --dport 5432 -j ACCEPT
iptables -I DOCKER-USER -s 2.2.2.2 -p tcp --dport 5432 -j ACCEPT

# 要先加完再擋
# 先查 if
ip a
# 再擋
# 先拒絕所有8080(進跟出)
iptables -I DOCKER-USER -p tcp --dport 8080 -j DROP
# 限制由某個if進來的5432
iptables -A DOCKER-USER -p tcp --dport 5432 -i eth0 -j DROP

# 下完要用這個查
iptables -t filter -L DOCKER-USER -n --line-numbers

# 擋 22 port (input chain)
# 允許 10.22.61.0/24 這個網段連入 SSH
iptables -I INPUT -p tcp -s 10.22.61.0/24 --dport 22 -j ACCEPT

# 拒絕其他 IP 連入 SSH
iptables -A INPUT -p tcp --dport 22 -j DROP

# 下完要用這個查
iptables -t filter -L INPUT -n --line-numbers

# 儲存 iptables 設定
iptables-save > /etc/iptables/rules.v4
# 建立一個 systemd service 檔案
vi /etc/systemd/system/iptables-restore.service
# 重新載入 systemd 並啟用這個服務
systemctl daemon-reload
systemctl enable iptables-restore.service
systemctl start iptables-restore.service

# 驗證服務是否正確執行
systemctl status iptables-restore.service

# 重開機後確認iptables有倒回
iptables -L -n -v