package es.uca.iw.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<TableRestaurant, Long> {
    List<TableRestaurant> findByRestaurant(Restaurant restaurant);
    List<TableRestaurant> findByRestaurantAndCapacity(Restaurant restaurant, int capacity);

}
