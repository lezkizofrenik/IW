package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class TableService {

    private TableRepository reporeservation;

    public TableService(){}
    @Autowired
    private TableService(TableRepository reporeservation) {
        this.reporeservation = reporeservation;
    }

    public synchronized TableRestaurant guardar(TableRestaurant entrada) {
        return reporeservation.save(entrada);
    }
    public Optional<TableRestaurant> buscarId(long id) {
        return reporeservation.findById(id);
    }
    public List<TableRestaurant> listar() {
        return reporeservation.findAll();
    }

    public List<TableRestaurant> searchByRestaurant(Restaurant restaurant){
        return reporeservation.findByRestaurant(restaurant);
    }

    public Long contar() {
        return reporeservation.count();
    }

    public void borrar(TableRestaurant entidad) {
        reporeservation.delete(entidad);
    }


    public List<TableRestaurant> searchTablesCapacity(Restaurant restaurant, int capacity){
        return reporeservation.findByRestaurantAndCapacity(restaurant, capacity);
    }



}
