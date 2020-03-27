package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HikeService {
    private HikeRepository repo;

    @Autowired
    private HikeService(HikeRepository repo){
        this.repo = repo;
    }
    public List<Hike> listar(){
        return repo.findAll();
    }
    public List<Hike> listarByDescription(String description){
        return repo.findByDescription(description);
    }
    public List<Hike> buscarByName(String description){
        return repo.findByNameStartsWithIgnoreCase(description);
    }

    public Hike searchById(int id){
        return repo.findById(id);
    }
    public synchronized Hike guardarHike(Hike entrada) {
        return repo.save(entrada);
    }
    public void borrarHike(Hike entidad) {
        repo.delete(entidad);
    }
    public List<Hike> searchByCity(City city){ return repo.findByCity(city);}
    public List<Hike> searchByCity_Name(String name){ return repo.findByCity_Name(name);}
    public List<Hike> searchByCity_NameAndAvailables(String name){ return repo.findByCity_NameAndCapacityGreaterThan(name, 0);}


}
