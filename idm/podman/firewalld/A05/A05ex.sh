sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.138.242.22" port protocol="udp" port="161" accept'
sudo firewall-cmd --permanent --add-port=5432/tcp
sudo firewall-cmd --reload