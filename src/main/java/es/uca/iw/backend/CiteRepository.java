package es.uca.iw.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CiteRepository extends JpaRepository<Cite, Long> {
    Cite findById(int id);
    List<Cite> findByEstablishment(Establishment establishment);
    Cite findByEstablishmentAndInit(Establishment establishment, String time);
}