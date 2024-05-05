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
        Biscuit b1 = new Biscuit();
        b1.name = "Kambly Classic";
        b1.description = "A Swiss favorite, Kambly classic are known for their sweet wholesome taste.";
        b1.image = "./kambly.png";

        Biscuit b2 = new Biscuit();
        b2.name = "Basler Lekerli";
        b2.description = "A Swiss German classic, Lekerli are known for being an acquired taste that stays.";
        b2.image = "./lakerli.png";
        
        Biscuit b3 = new Biscuit();
        b3.name = "Basler Brunsli";
        b3.description = "A Christmas standard, these biscuits will satisfy the sweetest-toothed amongst us!";
        b3.image = "./brunsli.png";

        List<Biscuit> biscs = new ArrayList<Biscuit>();
        biscs.add(b1);
        biscs.add(b2);
        biscs.add(b3);

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
