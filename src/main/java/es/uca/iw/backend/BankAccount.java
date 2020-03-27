package es.uca.iw.backend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity

public class BankAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int number = 0;
    @NotNull
    private float money = 0;

    public BankAccount(){}
    
    public BankAccount(int number, float money){
        this.number = number;
        this.money = money;
    }
    
    public int getId() {
        return id;
    }

    public float getMoney() {
        return money;
    }

    public int getNumber() {
        return number;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void payment(float money){
        this.money -= money;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setRefund(float money){
        this.money += money;
    }

}


