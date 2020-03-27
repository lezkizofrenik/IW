package es.uca.iw.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class ReservationRestaurant {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String dateWanted, dateReservation;
    @OneToOne
    @NotNull
    private Restaurant restaurant;
    @NotNull
    @OneToOne
    private Usuario user;
    @NotNull
    @OneToOne
    private TableRestaurant tableRestaurant;
    @NotNull
    boolean turn; // false: almuerzo true: cena

    public ReservationRestaurant(){}

    public ReservationRestaurant(LocalDate dateWanted, Date dateReservation, Restaurant restaurant, Usuario user, boolean turn, TableRestaurant tableRestaurant){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        this.dateWanted = formatter2.format(dateWanted);
        this.dateReservation = formatter.format(dateReservation);
        this.restaurant = restaurant;
        this.user = user;
        this.turn = turn;
        this.tableRestaurant = tableRestaurant;
    }
    public ReservationRestaurant(String dateWanted, String dateReservation, Restaurant restaurant, Usuario user, boolean turn, TableRestaurant tableRestaurant){
        this.dateWanted = dateWanted;
        this.dateReservation = dateReservation;
        this.restaurant = restaurant;
        this.user = user;
        this.turn = turn;
        this.tableRestaurant = tableRestaurant;
    }

    public String getDateWanted() {
        return dateWanted;
    }
    public String getDateReservation() {
        return dateReservation;
    }

    public int getId() {
        return id;
    }

    public Usuario getUser() {
        return user;
    }

    public boolean getTurn() {
        return turn;
    }
    public String getTurnParsed(){
        if(!turn) return "Almuerzo";
        else return "Cena";
    }
    public void setDate(Date dateWanted) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.dateWanted = formatter.format(dateWanted); ;
    }
    public void setDateReservation(Date dateReservation) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.dateReservation = formatter.format(dateReservation); ;
    }

    public int getPeopleReservation(){
        return tableRestaurant.getCapacity();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public TableRestaurant getTableRestaurant() {
        return tableRestaurant;
    }

    public void setDate(String dateWanted) {
        this.dateWanted = dateWanted;
    }
    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }


    public void setTableRestaurant(TableRestaurant tableRestaurant) {
        this.tableRestaurant = tableRestaurant;
    }

    //Necesario para el grid
    public String getRestaurantName(){
        return restaurant.getName();
    }
}
