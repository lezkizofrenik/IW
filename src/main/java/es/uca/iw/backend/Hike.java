package es.uca.iw.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
@Entity
public class Hike {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    private City city;
    @NotNull
    private String init, time; //HORA DE INICIO, DURACION
    @NotNull
    private int capacity, price;
    @NotNull
    private String description, name;

    public Hike(){}

    public Hike(int id, City city, LocalTime init, LocalTime time, int capacity, int price, String description, String name){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.id = id;
        this.init = formatter.format(init);
        this.city = city;
        this.time = formatter.format(time);
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.name = name;
    }

    public Hike(int id, City city, String init, String time, int capacity, int price, String description, String name){
        this.id = id;
        this.init = init;
        this.city = city;
        this.time = time;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.name = name;
    }

    public Hike(City city, String init, String time, int capacity, int price, String description, String name){
        this.init = init;
        this.city = city;
        this.time = time;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.name = name;
    }



    public Hike(City city, LocalTime init, LocalTime time, int capacity, int price, String description, String name){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.init = formatter.format(init);
        this.city = city;
        this.time = formatter.format(time);
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.name = name;
    }

    public City getCity() {
        return city;
    }
    public String getName() {
        return name;
    }
    public String getTime() {
        return time;
    }
    public String getInit() {
        return init;
    }

    public int getId(){ return id;}
    public int getCapacity(){ return capacity;}
    public int getPrice(){ return price;}
    public String getDescription(){ return description;}

    public void decrementCapacity(int capacity){ this.capacity-=capacity;}

    public void incrementCapacity(int capacity){
        this.capacity+=capacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setInit(LocalTime init) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.init = formatter.format(init);
    }

    public void setInit(String init) {
        this.init = init;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.time = formatter.format(time);
    }
    public void setName(String name){
        this.name = name;
    }

    public LocalTime initParsed(){
        return LocalTime.parse(init);
    }

    public LocalTime timeParsed(){
        return LocalTime.parse(time);
    }
}
