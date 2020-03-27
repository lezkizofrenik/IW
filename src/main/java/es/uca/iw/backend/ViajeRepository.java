package es.uca.iw.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.backend.Viaje;



public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByNameStartsWithIgnoreCase(String name);

    Viaje findById(int id);
}