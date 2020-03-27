package es.uca.iw.backend;

import org.apache.tomcat.jni.Local;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity {
    @Id
    @GeneratedValue
    int id;
    @NotNull
    String name = " ", description = " ", init, time, dias;
    @NotNull
    float price = 0;
    @NotNull
    int capacity = 0;


    public Activity(){}

    public Activity(String name, String description, String init, String time, float price, int capacity, String dias){
        this.dias = dias;
        this.init = init;
        this.name = name;
        this.description = description;
        this.price = price;
        this.capacity = capacity;
        this.time = time;
    }

    public Activity(String name, String description, String init, String time, float price, int capacity, Set<String> dias){
        this.dias = setStringtoString(dias);
        this.init = init;
        this.name = name;
        this.description = description;
        this.price = price;
        this.capacity = capacity;
        this.time = time;
    }

    public Activity(int id, String name, String description, LocalTime init, LocalTime time, float price, int capacity, String dias){
        this.dias = dias;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.id = id;
        this.time = formatter.format(time);
        this.init = formatter.format(init);
        this.name = name;
        this.description = description;
        this.price = price;
        this.capacity = capacity;
    }

    public Activity(int id, String name, String description, LocalTime init, LocalTime time, float price, int capacity, Set<String> dias){
        this.dias = setStringtoString(dias);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.id = id;
        this.time = formatter.format(time);
        this.init = formatter.format(init);
        this.name = name;
        this.description = description;
        this.price = price;
        this.capacity = capacity;

    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getInit(){
        return init;
    }

    public String getTime() {
        return time;
    }

    public float getPrice(){ return price;}
    public int getCapacity(){ return capacity;}
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setInit(String init) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        this.init = formatter.format(init);
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getInitParsed(){
        return LocalTime.parse(init);
    }

    public LocalTime getTimeParsed(){
        return LocalTime.parse(time);
    }

    public String toString(){
        return getName();
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public Set<Integer> diasToSetInt(){
        Set<Integer> set =  new HashSet<Integer>();
        if(!dias.isEmpty()){
            String[] parts = dias.split("/");
            for(int i = 0; i < parts.length; i++){
                set.add(Integer.parseInt(parts[i]));
            }
        }
        return set;
    }

    public Set<String> diasToSetString(){
        Set<String> set =  new HashSet<String>();
        if(!dias.isEmpty()){
            String[] parts = dias.split("/");
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


    public int parseDayStringToInt(String s){
        int day = 0;
        if(!s.isEmpty()){
            switch (s){
                case "Lunes":
                    day = 1;
                    break;
                case "Martes":
                    day = 2;
                    break;
                case "Miércoles":
                    day = 3;
                    break;
                case "Jueves":
                    day = 4;
                    break;
                case "Viernes":
                    day = 5;
                    break;
                case "Sábado":
                    day = 6;
                    break;
                case "Domingo":
                    day = 7;
                    break;
            }

        }
        return day;
    }

}