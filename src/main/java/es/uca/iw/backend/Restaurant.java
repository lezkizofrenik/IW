package es.uca.iw.backend;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Restaurant {

	@Id
	@GeneratedValue
    private int id;
    @NotNull
    private String name = " ";
    @NotNull
    String description = " ";

    public Restaurant(){}
    
    public Restaurant(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public Restaurant(int id,String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    
    public String getName(){return name;}
    public String getDescription(){ return description; }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String toString() {
    	return this.name;
    }
}
