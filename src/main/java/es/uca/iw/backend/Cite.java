package es.uca.iw.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Entity
public class Cite {
	@Id
	@GeneratedValue
	private int id;
	@NotNull
    private String init; //HORA

    @ManyToOne
    private Establishment establishment;

    public Cite(){}
    public Cite(int id,String init, Establishment establishment){
        this.id = id;
        this.init = init;
        this.establishment = establishment;
    }
    public Cite(int id, LocalTime init, Establishment establishment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.id = id;
        this.establishment = establishment;
        this.init = formatter.format(init);

    }



    public String getInit() {
        return init;
    }

    public LocalTime getTimeParsed(){
        return LocalTime.parse(init);
    }

    public Establishment getEstablishment(){
        return establishment;
    }

    public int getId() {
        return id;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public void setInit(LocalTime init){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.init = formatter.format(init);
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }


}
