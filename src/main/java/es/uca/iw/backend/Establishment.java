package es.uca.iw.backend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Establishment {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String name = " ", description = " ";
    @NotNull
    private boolean type; //0: solo reservas para una persona (Médico) 1: puede múltiple(SPA)

    public Establishment(){}
    
    public Establishment(int id, String name, String description, boolean type) {
        this.id = id;
        this.name=name;
        this.description = description;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName(){return name;}
    
    public String getDescription(){ return description; }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeParsed(){
        if(!type) return "Una";
        else return "Varias";
    }

    public String toString() {
    	return this.name;
    }

}
