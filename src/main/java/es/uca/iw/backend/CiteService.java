package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CiteService {
    private CiteRepository repo;

    @Autowired
    private CiteService(CiteRepository repo){
        this.repo = repo;
    }

    public List<Cite> searchEstablishment(Establishment establishment){
        return repo.findByEstablishment(establishment);
    }

    public int nCitesEstablishment(Establishment establishment){
        return searchEstablishment(establishment).size();
    }
    public List<Cite> listar(){
        return repo.findAll();
    }

    public Cite searchById(int id){
        return repo.findById(id);
    }
    public synchronized Cite guardarCite(Cite entrada) {
        return repo.save(entrada);
    }
    public void borrarCite(Cite entidad) {
        repo.delete(entidad);
    }
    public Cite searchByEstablishmentAndInit(Establishment establishment, String init){
        return repo.findByEstablishmentAndInit(establishment, init);
    }

}
