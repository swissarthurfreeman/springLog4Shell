package ch.unige.biscuits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class BiscuitsApplication {
	public static void main(String[] args) {
		System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
		SpringApplication.run(BiscuitsApplication.class, args);
	}

}
