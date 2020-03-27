package es.uca.iw;

import es.uca.iw.backend.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SpringBootApplication
public class CostavespertinoApplication {


    public static void main(String[] args) {
		SpringApplication.run(CostavespertinoApplication.class, args);
		
	}
    @EnableJpaRepositories
    public class Config {
    }
    @Bean
    public CommandLineRunner loadData(final UsuarioService usuarioService, final BankAccountService bankService, final ShipService shipService) {
        return (args) -> {
            try {
                usuarioService.loadUserByUsername("admin").getRole().equals("Admin");
            } catch (final UsernameNotFoundException e) {
                final Usuario admin = new Usuario();
                admin.setNombre("admin");
                admin.setPassword("admin");
                admin.setApellido1("a");
                admin.setApellido2("a");
                admin.setEmail("admin@admin.es");
                admin.setUsername("admin");
                admin.setRole("Admin");
                admin.setTelefono("9");
                admin.setDni("9");
                Ship ship = new Ship();
                ship.setName("Astral");
                shipService.guardarShip(ship);
                admin.setShip(ship);
                BankAccount bank = new BankAccount((int)Math.random(), (int) Math.random());
                bankService.guardarBankAccount(bank);
                admin.setBankAccount(bank);
                usuarioService.create(admin);

                final Usuario u = new Usuario();
                u.setNombre("user");
                u.setPassword("user");
                u.setApellido1("admin");
                u.setApellido2("admin");
                u.setEmail("a@admin.es");
                u.setUsername("user");
                u.setRole("User");
                u.setTelefono("92");
                u.setDni("912");
                Ship ship2 = new Ship();
                ship2.setName("Open Arms");
                shipService.guardarShip(ship2);
                u.setShip(ship2);
                BankAccount bank2 = new BankAccount();
                bankService.guardarBankAccount(bank2);
                u.setBankAccount(bank);
                usuarioService.create(u);


            }
        };
    }
}
