package ch.unige.biscuits.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.unige.biscuits.domain.Biscuit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/biscuits")
public class BiscuitController {
    private static Logger logger = LogManager.getLogger();

    @GetMapping("")
    @CrossOrigin
    public HttpEntity<List<Biscuit>> list() {
        var b1 = new Biscuit();
        b1.name = "Kambly Chocosquares";
        b1.description = "Delicious full grain and dark chocolate biscuits.";
        b1.stock = 10;

        var b2 = new Biscuit();
        b2.name = "Oreos";
        b2.description = "Classic oreos.";
        b2.stock = 24;

        List<Biscuit> biscs = new ArrayList<Biscuit>();
        biscs.add(b1);
        biscs.add(b2);

        logger.info("GET /biscuits");    
        return new HttpEntity<List<Biscuit>>(biscs);
    }

    @GetMapping("{id}")
    @CrossOrigin
    public HttpEntity<Optional<Biscuit>> get(@PathVariable String id) {
        logger.info("GET /biscuits/{}", id);    
        return null;
    }
}
