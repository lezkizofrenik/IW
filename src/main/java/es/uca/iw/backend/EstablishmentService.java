package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstablishmentService {
    private EstablishmentRepository repo;

    @Autowired
    private EstablishmentService(EstablishmentRepository repo){
        this.repo = repo;
    }
    
    public List<Establishment> listar(){
        return repo.findAll();
    }
    
    public List<Establishment> listarByName(String description){
        return repo.findByNameStartsWithIgnoreCase(description);
    }

    public Establishment searchById(int id){
        return repo.findById(id);
    }
    
    public synchronized Establishment guardarEstablishment(Establishment entrada) {
        return repo.save(entrada);
    }
    
    public void borrarEstablishment(Establishment entidad) {
        repo.delete(entidad);
    }
}
