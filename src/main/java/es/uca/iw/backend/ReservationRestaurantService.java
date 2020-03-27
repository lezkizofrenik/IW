package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ReservationRestaurantService {

    private ReservationRestaurantRepository reporeservation;


    @Autowired
    private ReservationRestaurantService(ReservationRestaurantRepository reporeservation) {
        this.reporeservation = reporeservation;
    }
    public synchronized ReservationRestaurant guardarReservation(ReservationRestaurant entrada) {
        return reporeservation.save(entrada);
    }
    public Optional<ReservationRestaurant> buscarIdReservation(long id) {
        return reporeservation.findById(id);
    }
    public List<ReservationRestaurant> listarReservation() {
        return reporeservation.findAll();
    }

    public Long contarReservation() {
        return reporeservation.count();
    }

    public void borrarReservation(ReservationRestaurant entidad) {
        reporeservation.delete(entidad);
    }

    public List<ReservationRestaurant> buscarPorUsuario(Usuario u) {
        return reporeservation.findByUser(u);
    }

    public List<ReservationRestaurant> buscarPorFechaRestauranteYturno(Restaurant restaurant, String date, boolean turn){
        return reporeservation.findByRestaurantAndDateWantedAndTurn(restaurant, date, turn );
    }

    public List<ReservationRestaurant> buscarPorFechaRestauranteYturnoYmesa(Restaurant restaurant, String date, boolean turn, TableRestaurant tableRestaurant){
        return reporeservation.findByRestaurantAndDateWantedAndTurnAndTableRestaurant(restaurant, date, turn, tableRestaurant);
    }

    public void deleteByUser(Usuario u){
        reporeservation.deleteReservationActivitiesByUser(u);
    }
}
