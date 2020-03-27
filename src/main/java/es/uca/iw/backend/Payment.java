package es.uca.iw.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private float total;
    @OneToOne
    private Usuario user;
    @NotNull
    private int idServicio;
    @NotNull
    private int type;
    @NotNull
    private String date;

    public Payment(){};
    public Payment(int id, float total, Usuario user, int idServicio, int type, Date date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = formatter.format(date);
        this.id = id;
        this.total = total;
        this.user = user;
        this.idServicio = idServicio;
        this.type = type;
    }

    public Payment(float total, Usuario user, int idServicio, int type, Date date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = formatter.format(date);
        this.total = total;
        this.user = user;
        this.idServicio = idServicio;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public float getTotal() {
        return total;
    }

    public Usuario getUsuario(){
        return user;
    }


    public void setTotal(float total) {
        this.total = total;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public int getType() {
        return type;
    }

    public String getTypeParsed(){
        String typeParsed = "";
        switch(type){
            case 1:
                typeParsed = "Excursión";
                break;
            case 2:
                typeParsed ="Actividad";
                break;
            case 3:
                typeParsed ="Establecimiento";
                break;
            case 4:
                typeParsed ="Restaurante";
                break;
        }
        return typeParsed;
    }

    //Necesario para grid

    public Long getUsuarioId(){
        return user.getId();
    }
}
