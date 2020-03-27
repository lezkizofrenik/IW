package es.uca.iw.backend;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ReservationHike {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String dateReservation;
    @OneToOne
    private Payment payment; //PUEDE SER 0
    @OneToOne
    @NotNull
    private Hike hike;
    @NotNull
    @OneToOne
    private Usuario user;
    @NotNull
    private int nPeople;

    //EL TIPO ESTA EN SERVICIO
    public ReservationHike(){}
    
    public ReservationHike(Hike hike, Usuario user, Date dateReservation, Payment payment, int nPeople){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        this.hike = hike;
        this.user = user;
        this.dateReservation = formatter.format(dateReservation);
        this.payment = payment;
        this.nPeople = nPeople;
    }

    //Getters
    public int getId() {
        return id;
    }

    public Usuario getUser() {
        return user;
    }

    public String getDate() {
        return dateReservation;
    }

    public Hike getHike() {
        return hike;
    }

    //Setters
    public void setDate(String date) {
        this.dateReservation = date;
    }

    public Payment getPayment() {
        return payment;
    }

    public int getnPeople(){ return nPeople;}

    public void setDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.dateReservation = formatter.format(date); ;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public void setHike(Hike hike) {
        this.hike = hike;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    //Necesario para el grid
    public String getNameHike(){
        return hike.getName();
    }
    public String getCityHike(){
        return hike.getCity().getName();
    }
}
