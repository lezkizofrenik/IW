package es.uca.iw.backend;

import es.uca.iw.backend.ReservationRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRestaurantRepository extends JpaRepository<ReservationRestaurant, Long> {
    List<ReservationRestaurant> findByUser(Usuario u);
    List<ReservationRestaurant> findByRestaurantAndDateWantedAndTurn(Restaurant restaurant, String dateWanted, boolean turn);
    List<ReservationRestaurant> findByRestaurantAndDateWantedAndTurnAndTableRestaurant(Restaurant restaurant, String dateWanted, boolean turn, TableRestaurant tableRestaurant);
    void deleteReservationActivitiesByUser(Usuario u);

}
