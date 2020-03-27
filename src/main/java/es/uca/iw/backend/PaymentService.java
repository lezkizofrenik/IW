package es.uca.iw.backend;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    PaymentRepository repoPayment;

    private PaymentService(PaymentRepository repoPayment) {
        this.repoPayment = repoPayment;
    }

    public synchronized Payment guardarPayment(Payment entrada) {
        return repoPayment.save(entrada);
    }

    public Optional<Payment> buscarIdPayment(Long id) {
        return repoPayment.findById(id);
    }

    public List<Payment> listarPayment() {
        return repoPayment.findAll();
    }

    public Long contarPayment() {
        return repoPayment.count();
    }

    public void borrarPayment(Payment entidad) {
        repoPayment.delete(entidad);
    }

    public List<Payment> buscarActividadesPorUsuario(Usuario u){
        return repoPayment.findByUserAndType(u, 2);
    }

    public List<Payment> buscarHikesPorUsuario(Usuario u){
        return repoPayment.findByUserAndType(u, 1);
    }

    public List<Payment> buscarPorUsuario(Usuario u){
        return repoPayment.findByUser(u);
    }
    public List<Payment> buscarPorUsuarioYFecha(Usuario u, String date){
        return repoPayment.findByUserAndDate(u, date);
    }

    public List<Payment> listarPorUsuarioyPagosNoNulos(Usuario u){
        return repoPayment.findByUserAndTotalGreaterThan(u, 0);
    }


}
