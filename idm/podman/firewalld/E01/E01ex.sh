sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.210.131" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.210.101" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.210.107" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.210.108" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.214.97" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.214.86" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.14.210.92" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --reload