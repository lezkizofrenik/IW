package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private RestaurantRepository repo;

    @Autowired
    private RestaurantService(RestaurantRepository repo){
        this.repo = repo;
    }
    public List<Restaurant> listar(){
        return repo.findAll();
    }
    public List<Restaurant> listarByDescription(String description){
        return repo.findByDescription(description);
    }
    public List<Restaurant> listarByName(String description){
        return repo.findByNameStartsWithIgnoreCase(description);
    }
    public Restaurant searchById(int id){
        return repo.findById(id);
    }
    public synchronized Restaurant guardarRestaurant(Restaurant entrada) {
        return repo.save(entrada);
    }
    public void borrarRestaurant(Restaurant entidad) {
        repo.delete(entidad);
    }
}
