package es.uca.iw.backend;

import es.uca.iw.backend.BankAccount;
import es.uca.iw.backend.Ship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Usuario implements UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id // esto sirve para decir cual es el id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // esto para que se genere aleatorio
    private Long id;
    @NotEmpty(message = "Este campo es obligatorio")
    private String dni = "", nombre = "", apellido1 = "", apellido2 = "", telefono = "", password = "";
    @NotEmpty(message = "Este campo es obligatorio")
    @Column(unique = true)
    private String username = "";
    @NotEmpty(message = "Este campo es obligatorio")
    @Column(unique = true)
    private String email = "";
    private String role = "";
    @OneToOne
    private Ship ship;
    @OneToOne
    @NotNull
    private BankAccount bankAccount;

    // Getters
    public Long getId() {
        return id;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public String getRole() {
        return role;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    // Setters
    public void setId(Long Id) {
        this.id = Id;
    }

    public void setApellido1(String Apellido1) {
        this.apellido1 = Apellido1;
    }

    public void setApellido2(String Apellido2) {
        this.apellido2 = Apellido2;
    }

    public void setDni(String Dni) {
        this.dni = Dni;
    }

    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }

    public void setTelefono(String Telefono) {
        this.telefono = Telefono;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public void setPassword(String Contrasena) {
        this.password = Contrasena;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    
    public void setUsername(String nombreUsuario) {
        this.username = nombreUsuario;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(role));
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellido1() + " " + getApellido2();
    }
}