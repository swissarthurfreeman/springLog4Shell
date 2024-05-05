package ch.unige.biscuits.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Command {
    @Id 
    @Column
    public String id;
    
    @Column
    public String email;
    
    @Column
    public String location;
    
    @ElementCollection 
    public List<String> biscuits;    
}
