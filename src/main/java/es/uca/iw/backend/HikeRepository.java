package es.uca.iw.backend;

import es.uca.iw.backend.Hike;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HikeRepository extends JpaRepository<Hike, Long> {
    Hike findById(int id);
    List<Hike> findByDescription(String description);
    List<Hike> findByCity(City city);
    List<Hike> findByCity_Name(String city);
    List<Hike> findByCity_NameAndCapacityGreaterThan(String city, int i);

    List<Hike> findByNameStartsWithIgnoreCase(String name);

}
