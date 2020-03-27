package es.uca.iw.backend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class ReservationEstablishment {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @OneToOne
    private Cite cite;
    @NotNull
    @OneToOne
    private Usuario user;
    @NotNull
    String dateReservation, dateWanted;
    @NotNull
    int nPeople;

    public ReservationEstablishment(){}
    public ReservationEstablishment(Usuario user, Cite cites, Date dateReservation, LocalDate dateWanted, int nPeople){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateReservation = formatter.format(dateReservation);
        this.dateWanted = formatter2.format(dateWanted);
        this.cite = cites;
        this.user = user;
        this.nPeople = nPeople;
    }

    public int getId() {
        return id;
    }

    public int getnPeople() {
        return nPeople;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public String getDateWanted() {
        return dateWanted;
    }

    public Usuario getUser() {
        return user;
    }
    public Cite getCite(){ return cite;}

    public String getDate(){
        return dateReservation;
    }
    public void setUser(Usuario user) {
        this.user = user;
    }

    public void setDate(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setDateWanted(String dateWanted) {
        this.dateWanted = dateWanted;
    }

    public void setnPeople(int nPeople) {
        this.nPeople = nPeople;
    }

    public void setCite(Cite cite) {
        this.cite = cite;
    }
    //Necesario para el grid
    public String getEstablishmentName(){
        return cite.getEstablishment().getName();
    }

    public String getTurnCite(){
        return cite.getInit();
    }



}
