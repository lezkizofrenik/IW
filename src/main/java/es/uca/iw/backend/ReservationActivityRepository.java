package es.uca.iw.backend;

import es.uca.iw.backend.ReservationActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReservationActivityRepository extends JpaRepository<ReservationActivity, Long> {
    List<ReservationActivity> findByUser(Usuario usuario);

    List<ReservationActivity> findByActivityAndDateWanted(Activity activity, String date);
	ReservationActivity findByPayment(Payment pago);
	void deleteReservationActivitiesByUser(Usuario u);
}