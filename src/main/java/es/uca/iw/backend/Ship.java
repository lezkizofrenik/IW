
package es.uca.iw.backend;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Ship {
	@Id
	@GeneratedValue
    private int id;
	@NotEmpty
	
	private String name = " ";
	@OneToOne
    private Viaje viaje;

	public Ship(int id, String name, Viaje viaje){
		this.id = id;
		this.name = name;
		this.viaje = viaje;
	}
	public Ship() {}

	public int getId(){
	    return this.id;
	}
	public String getName(){
	    return this.name;
	}
	public Viaje getViaje(){
	    return this.viaje;
	}

    public void setId(int id) {
    	this.id = id;
    }
    public void setName(String name) {
    	this.name = name;
	}
	public void setViaje(Viaje viaje) {
    	this.viaje = viaje;
	}

    public String toString(){
		return name;
	}

}