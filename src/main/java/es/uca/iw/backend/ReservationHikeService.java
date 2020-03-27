package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ReservationHikeService {

    private ReservationHikeRepository reporeservation;


    @Autowired
    private ReservationHikeService(ReservationHikeRepository reporeservation) {
        this.reporeservation = reporeservation;
    }
    public synchronized ReservationHike guardarReservation(ReservationHike entrada) {
        return reporeservation.save(entrada);
    }
    public Optional<ReservationHike> buscarIdReservation(long id) {
        return reporeservation.findById(id);
    }
    public ReservationHike buscarReservaPorPago(Payment pago) {
    	return reporeservation.findByPayment(pago);
    }
    public List<ReservationHike> listarReservation() {
        return reporeservation.findAll();
    }

    public Long contarReservation() {
        return reporeservation.count();
    }

    public void borrarReservation(ReservationHike entidad) {
        reporeservation.delete(entidad);
    }
    public List<ReservationHike> buscarPorUsuario(Usuario u){
        return reporeservation.findByUser(u);
    }

    public void deleteByUser(Usuario u){
        reporeservation.deleteReservationActivitiesByUser(u);
    }
}
