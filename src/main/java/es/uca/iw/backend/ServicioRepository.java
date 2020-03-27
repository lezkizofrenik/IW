package es.uca.iw.backend;

import es.uca.iw.backend.Servicio;
import es.uca.iw.backend.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByType(int type);
    Servicio findByIdAndType(int id, int type);
    Servicio findByIdServicioAndType(int id, int type);

    List<Servicio> findByTypeAndShip(int tipo, Ship ship);

    List<Servicio> findByShip(Ship ship);

}