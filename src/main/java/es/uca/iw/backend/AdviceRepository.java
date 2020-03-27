package es.uca.iw.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdviceRepository extends JpaRepository<Advice, Long> {
    List<Advice> findByShip(Ship ship);
    Advice findById(int id);

}
