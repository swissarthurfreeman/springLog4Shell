package ch.unige.ldap;

import java.net.InetAddress;
import java.net.URL;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Entry;

/**
 * Hello world !
 * mvn package + java -cp target/my-app-1.0-SNAPSHOT.jar ch.unige.ldap.App
 */
public class App 
{
    // this is the server distinguished name, dc means domain component, dn means distinguished name
    // see https://stackoverflow.com/questions/18756688/what-are-cn-ou-dc-in-an-ldap-search
    // worth a read https://www.zytrax.com/books/ldap/ch2/index.html#history
    private static final String LDAP_BASE = "dc=unige,dc=ch";
    private static final int PORT = 1389;

    public static void main( String[] args )
    {
        System.out.println("Launching LDAP Server...");
        try { 
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);    

            // a config has a certain number of listeners, each listens on a port and a certain address.
            config.setListenerConfigs(
                new InMemoryListenerConfig(
                    "listen", 
                    InetAddress.getByName("0.0.0.0"),   // address on the which ldap server listens
                    PORT,                                   
                    ServerSocketFactory.getDefault(),        // config to create sockets with bogus SSL
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()
                )
            );
            
            // what does this line do ? 
            // config.addAdditionalBindCredentials("cn=admin,dc=biscuits,dc=unige,dc=ch", "password");
        
            // config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(args[ 0 ])));
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            ds.add(
               "dn: dc=unige,dc=ch",
                            "objectclass: top",
                            "objectclass: domain",
                            "dc: unige"                   
            );

            ds.add(
               "dn: ou=biscuits,dc=unige,dc=ch",        // create a organizational unit
                            "objectClass: organizationalUnit",
                            "ou: biscuits"                   
            );

            
            ds.add(
               "dn: cn=tom,ou=biscuits,dc=unige,dc=ch",        // object class defines the type of the LDAP entry, the set of attributes that it MUST have as well as the optional ones.
                            "objectClass: inetOrgPerson",                   // see https://datatracker.ietf.org/doc/html/rfc2798#page-2
                            "cn: tom",
                            "sn: Tom",
                            "mail: tom@warner.com",
                            "userPassword: tom"
            );
            // for querying stuff see https://access.redhat.com/documentation/en-us/red_hat_directory_server/12/html/searching_entries_and_tuning_searches/ref_ldap-search-examples_searching-entries-and-tuning-searches
            // ldapsearch -H ldap://0.0.0.0:1389 -b "dc=unige,dc=ch" -s sub -x "(objectclass=*)"
            
            ds.add(
               "dn: cn=jerry,ou=biscuits,dc=unige,dc=ch",
                            "objectClass: inetOrgPerson",       
                            "cn: jerry",
                            "sn: Jerry",
                            "mail: jerry@warner.com",
                            "userPassword: jerry"
            );
            
            
            System.out.println("Listening on 0.0.0.0:" + Integer.toString(PORT)); //$NON-NLS-1$
            ds.startListening();

            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
