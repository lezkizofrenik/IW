package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ReservationEstablishmentService {

    private ReservationEstablishmentRepository reporeservation;


    @Autowired
    private ReservationEstablishmentService(ReservationEstablishmentRepository reporeservation) {
        this.reporeservation = reporeservation;
    }
    public synchronized ReservationEstablishment guardarReservation(ReservationEstablishment entrada) {
        return reporeservation.save(entrada);
    }
    public Optional<ReservationEstablishment> buscarIdReservation(long id) {
        return reporeservation.findById(id);
    }
    
    public ReservationEstablishment listReservationByCite(Cite c){
    	return reporeservation.findByCite(c);
    }
    public List<ReservationEstablishment> listarReservation() {
        return reporeservation.findAll();
    }

    public Long contarReservation() {
        return reporeservation.count();
    }

    public void borrarReservation(ReservationEstablishment entidad) {
        reporeservation.delete(entidad);
    }

    public List<ReservationEstablishment> buscarPorFecha(String d){
        return reporeservation.findByDateWanted(d);
    }
    public List<ReservationEstablishment> buscarPorUsuario(Usuario u) {
        return reporeservation.findByUser(u);
    }

    public List<ReservationEstablishment> buscarPorEstablecimiento(Establishment e){
        return reporeservation.findByCite_Establishment(e);
    }

    public int nReservasPorEstablecimiento(Establishment e){
        return buscarPorEstablecimiento(e).size();
    }

    public List<ReservationEstablishment> buscarPorEstablecimientoYFecha(Establishment e, String d){
        return reporeservation.findByCite_EstablishmentAndDateWanted(e, d);
    }

    public List<ReservationEstablishment> buscarPorEstablecimientoYFechaYTurno(Establishment e, String d, String t){
        return reporeservation.findByCite_EstablishmentAndDateWantedAndCite_Init(e, d, t);
    }

    public int nReservasPorEstablecimientoYFecha(Establishment e, String d){
        return buscarPorEstablecimientoYFecha(e, d).size();
    }
    public int nReservasPorEstablecimientoYFechaYTurno(Establishment e, String d, String turno){
        return buscarPorEstablecimientoYFechaYTurno(e, d, turno).size();
    }

    public void deleteByUser(Usuario u){
        reporeservation.deleteReservationActivitiesByUser(u);
    }
}
