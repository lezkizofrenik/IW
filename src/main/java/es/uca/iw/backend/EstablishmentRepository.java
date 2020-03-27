package es.uca.iw.backend;

import es.uca.iw.backend.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    List<Establishment> findByNameStartsWithIgnoreCase(String description);
    Establishment findById(int id);
}
