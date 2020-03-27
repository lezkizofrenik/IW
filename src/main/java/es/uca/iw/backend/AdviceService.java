package es.uca.iw.backend;

import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AdviceService {
    private AdviceRepository repo;

    private AdviceService(AdviceRepository repo){
        this.repo = repo;
    }


    public List<Advice> findByShip(Ship ship){
        return repo.findByShip(ship);
    }

    public Advice searchById(int id){
        return repo.findById(id);
    }
    public synchronized Advice save(Advice entrada) {
        return repo.save(entrada);
    }
    public void borrar(Advice entidad) {
        repo.delete(entidad);
    }
    public List<Advice> listar(){
        return repo.findAll();
    }


}
