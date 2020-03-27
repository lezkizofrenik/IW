package es.uca.iw.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.backend.Ship;



public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Ship> findByNameStartsWithIgnoreCase(String name);
    
    Optional<Ship> findById(int id);
}