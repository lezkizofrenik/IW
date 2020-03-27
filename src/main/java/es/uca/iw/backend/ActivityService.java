package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private ActivityRepository repo;

    @Autowired
    private ActivityService(ActivityRepository repo){
        this.repo = repo;
    }
    public List<Activity> listar(){
        return repo.findAll();
    }
    public List<Activity> listarByDescription(String description){
        return repo.findByDescriptionStartsWithIgnoreCase(description);
    }
    public List<Activity> listarByName(String name){
        return repo.findByNameStartsWithIgnoreCase(name);
    }
    public Activity searchById(int id){
        return repo.findById(id);
    }
    public synchronized Activity guardarActivity(Activity entrada) {
        return repo.save(entrada);
    }
    public void borrarActivity(Activity entidad) {
        repo.delete(entidad);
    }


}
