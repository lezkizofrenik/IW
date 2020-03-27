package es.uca.iw.backend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class City {
	@Id
	@GeneratedValue
    private int id;
	@NotNull
    private String name = "";

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public City() {
    	this.id = 0;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
    	return this.name;
    }
}
