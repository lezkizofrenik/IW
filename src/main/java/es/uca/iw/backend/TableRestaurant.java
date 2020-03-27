package es.uca.iw.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TableRestaurant { 
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private int capacity;


    @ManyToOne
    @NotNull
    private Restaurant restaurant;

    public TableRestaurant(){}
    public TableRestaurant(int id, int capacity, Restaurant restaurant){
        this.id=id;
        this.capacity= capacity;
        this.restaurant = restaurant;
    }


    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }


}
