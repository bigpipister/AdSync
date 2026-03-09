sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.151" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.27.102.91" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.94" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --reload