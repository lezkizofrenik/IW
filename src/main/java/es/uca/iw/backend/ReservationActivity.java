package es.uca.iw.backend;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ReservationActivity {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String dateReservation, dateWanted;
    @OneToOne
    private Payment payment; //PUEDE SER 0
    @OneToOne
    @NotNull
    private Activity activity;
    @NotNull
    @OneToOne
    private Usuario user;
    @NotNull
    private int nPeople;

    public ReservationActivity(){}
    
    public ReservationActivity(Activity activity, Usuario user, Date dateReservation, LocalDate dateWanted, Payment payment, int nPeople){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        this.dateWanted = formatter2.format(dateWanted);
        this.dateReservation = formatter.format(dateReservation);
        this.activity = activity;
        this.user = user;
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

    public String getDateReservation() {
        return dateReservation;
    }
    public String getDateWanted() {
        return dateWanted;
    }

    public Activity getActivity() {
        return activity;
    }

    public Payment getPayment() {
        return payment;
    }

    public int getnPeople() {
        return nPeople;
    }

    public void setnPeople(int nPeople) {
        this.nPeople = nPeople;
    }

    //Necesario para el grid
    public String getActivityName() {
        return activity.getName();
    }

    //Setters
    public void setDate(String dateReservation) {
        this.dateReservation = dateReservation;
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public void setDate(Date dateReservation) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.dateReservation = formatter.format(dateReservation); ;
    }

}
