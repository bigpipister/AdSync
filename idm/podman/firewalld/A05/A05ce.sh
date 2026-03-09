sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.11.64.151/32" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.138.242.50/32" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.138.242.22" port protocol="udp" port="161" accept'
sudo firewall-cmd --permanent --add-port=161/udp
sudo firewall-cmd --reload