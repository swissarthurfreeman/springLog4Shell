package ch.unige.biscuits.rest;

import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.Logger;
import ch.unige.biscuits.domain.Command;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import ch.unige.biscuits.domain.repository.OrderRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static Logger logger = LogManager.getLogger();
    
    @Autowired
    OrderRepository ordRepo;

    @PostMapping("")
    @CrossOrigin
    public HttpEntity<Command> createorder(@RequestBody Command order) {
        order.id = UUID.randomUUID().toString();;
        logger.info("New biscuit order from {} at {}", order.email, order.location);
        return new ResponseEntity<Command>(ordRepo.save(order), HttpStatus.CREATED);
    }


    @GetMapping("")
    @CrossOrigin
    public HttpEntity<List<Command>> getall() {
        return new ResponseEntity<List<Command>>(this.ordRepo.findAll(), HttpStatus.OK);
    }
}
