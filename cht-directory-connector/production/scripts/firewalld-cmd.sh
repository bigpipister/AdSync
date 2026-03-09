查看服務
systemctl | grep postgres

重啟服務
sudo systemctl restart postgresql-12

查看防火牆設定
sudo firewall-cmd --zone=public --list-all

新增規財
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="192.168.12.9" port port="5432" protocol="tcp" accept'

更改target
firewall-cmd --permanent --zone=docker --set-target=DROP

重啟防火牆
sudo firewall-cmd --reload