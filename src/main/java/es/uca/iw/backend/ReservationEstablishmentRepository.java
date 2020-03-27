package es.uca.iw.backend;

import es.uca.iw.backend.ReservationEstablishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationEstablishmentRepository extends JpaRepository<ReservationEstablishment, Long> {
    List<ReservationEstablishment> findByUser(Usuario u);
    ReservationEstablishment findByCite(Cite c);
    List<ReservationEstablishment> findByCite_Establishment(Establishment establishment);
    List<ReservationEstablishment> findByDateWanted(String date);
    List<ReservationEstablishment> findByCite_EstablishmentAndDateWanted(Establishment establishment, String date);
    List<ReservationEstablishment> findByCite_EstablishmentAndDateWantedAndCite_Init(Establishment establishment, String date, String init);
    void deleteReservationActivitiesByUser(Usuario u);



}
