sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.131" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.92" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.95" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.96" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.31" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.37" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.38" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="10.26.244.94" port protocol="tcp" port="5432" accept'
sudo firewall-cmd --reload