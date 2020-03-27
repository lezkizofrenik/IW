package es.uca.iw.backend;

import es.uca.iw.backend.ReservationHike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationHikeRepository extends JpaRepository<ReservationHike, Long> {
    List<ReservationHike> findByUser(Usuario u);
    void deleteReservationActivitiesByUser(Usuario u);
	ReservationHike findByPayment(Payment pago);
}
