package es.uca.iw.backend;

import es.uca.iw.backend.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserAndType(Usuario u, int type);
    List<Payment> findByUser(Usuario u);
    List<Payment> findByUserAndDate(Usuario u, String date);
    List<Payment> findByUserAndTotalGreaterThan(Usuario u, int i);
}
