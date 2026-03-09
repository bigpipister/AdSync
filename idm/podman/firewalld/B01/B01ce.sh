sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.151" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.92/24" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.23.102.91/24" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --reload