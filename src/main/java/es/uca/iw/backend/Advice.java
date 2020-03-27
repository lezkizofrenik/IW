package es.uca.iw.backend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity

public class Advice {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private Ship ship;
    @NotNull
    private String description;

    public Advice(){}
    public Advice(Ship ship, String description){
        this.ship = ship;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Ship getShip() {
        return ship;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}
