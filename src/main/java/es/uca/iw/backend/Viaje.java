
package es.uca.iw.backend;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Viaje {
	@Id
	@GeneratedValue
    private int id;
	@NotEmpty
	private String name = " ";
    private String route = " ";

	public Viaje(int id, String name, String route){
		this.id = id;
		this.name = name;
		this.route = route;
	}
	public Viaje() {}

	public int getId(){
	    return this.id;
	}
	public String getName(){
	    return this.name;
	}
	public String getRoute(){
	    return this.route;
	}

    public void setId(int id) {
    	this.id = id;
    }
    public void setName(String name) {
    	this.name = name;
	}
	public void setRoute(String route) {
    	this.route = route;
	}

    public String toString(){
		return name;
	}

	public Set<String> rutaToSetString(){
		Set<String> set =  new HashSet<String>();
		if(!route.isEmpty()){
			String[] parts = route.split("/");
			for(int i = 0; i < parts.length; i++){
				set.add(parts[i]);
			}
		}
		return set;
	}

	public String setStringtoString(Set<String> set){
		String [] string = set.toArray(new String [set.size()]);
		return  String.join("/", string);
	}


}