package es.uca.iw.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import es.uca.iw.backend.City;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByNameStartsWithIgnoreCase(String name);
    List<City> findByName(String name);
}