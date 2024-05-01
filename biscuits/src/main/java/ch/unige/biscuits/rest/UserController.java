package ch.unige.biscuits.rest;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.unige.biscuits.domain.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.Interpolator;

@RestController
@RequestMapping("/users")
public class UserController {
    private static Logger logger = LogManager.getLogger();

    @PostMapping("")
    @CrossOrigin
    public HttpEntity<Optional<User>> get(@RequestBody User user) {
        logger.info(user.email);
        logger.info(user.password);
        
        // finally https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core/2.14.1
        logger.info(org.apache.logging.log4j.util.PropertiesUtil.class.getPackage().getImplementationVersion());
        logger.info("POST /users/{}", user);    
        return null;
    }
}
