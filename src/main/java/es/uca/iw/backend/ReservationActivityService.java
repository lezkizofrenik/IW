package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ReservationActivityService {

    private ReservationActivityRepository reporeservation;

    @Autowired
    private ReservationActivityService(ReservationActivityRepository reporeservation) {
        this.reporeservation = reporeservation;
    }
    public synchronized ReservationActivity guardarReservation(ReservationActivity entrada) {
        return reporeservation.save(entrada);
    }
    public Optional<ReservationActivity> buscarIdReservation(long id) {
        return reporeservation.findById(id);
    }
    public List<ReservationActivity> listarReservation() {
        return reporeservation.findAll();
    }
    public ReservationActivity buscarReservaPorPago(Payment pago) {
    	return reporeservation.findByPayment(pago);
    }
    public Long contarReservation() {
        return reporeservation.count();
    }

    public void borrarReservation(ReservationActivity entidad) {
        reporeservation.delete(entidad);
    }
    public List<ReservationActivity> buscarPorUsuario(Usuario u){
        return reporeservation.findByUser(u);
    }
    public int actividadesReservadas(Activity activity, String fecha){
        int cont = 0;
        List<ReservationActivity> list=  reporeservation.findByActivityAndDateWanted(activity, fecha);
        for(int i = 0; i < list.size(); i++){
            cont+= list.get(i).getnPeople();
        }

        return cont;

    }

    public void deleteByUser(Usuario u){
        reporeservation.deleteReservationActivitiesByUser(u);
    }

}
