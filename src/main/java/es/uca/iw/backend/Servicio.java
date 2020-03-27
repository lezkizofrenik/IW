package es.uca.iw.backend;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Entity
@Table (name="servicio")
public class Servicio implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private int id;
    @NotNull(message = "Campos obligatorios")
    @Min(value = 1)
    @Max(value = 4)
    private int type; //0: excursion 1:instalacion 2:restaurante 3:actividad
    @NotNull(message = "Campos obligatorios")
    private int idServicio;
    @OneToOne
    @NotNull
    private Ship ship;

    public Servicio(int id,int idServicio, int type, Ship ship){
        this.id = id;
        this.idServicio = idServicio;
        this.type = type;
        this.ship = ship;

    }
    public Servicio(int idServicio, int type, Ship ship){
        this.idServicio = idServicio;
        this.type = type;
        this.ship = ship;

    }

    public Servicio(){}

    public Ship getShip() {
        return ship;
    }

    public int getId() {
        return id;
    }
    public int getIdServicio(){
        return idServicio;
    }
    public int getType() {
        return type;
    }

    public void setIdServicio(int type){ this.idServicio = type;}
    public void setType(Integer type) {
        this.type = type;
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


}