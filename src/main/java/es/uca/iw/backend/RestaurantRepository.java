package es.uca.iw.backend;

import es.uca.iw.backend.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByDescription(String description);
    Restaurant findById(int id);
    List<Restaurant> findByNameStartsWithIgnoreCase(String name);

}
