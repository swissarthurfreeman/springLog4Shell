package ch.unige.biscuits;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class BiscuitsApplication {
	public static void main(String[] args) {
		// set this to "false" and rebuild image to test basic mitigation
		System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
		SpringApplication.run(BiscuitsApplication.class, args);
	}

}
