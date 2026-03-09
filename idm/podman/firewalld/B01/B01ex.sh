sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.33" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.39" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.40" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.92" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.95" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.96" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.97" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.22.242.132" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --reload