package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ShipService {
    private ShipRepository repoShip;

    @Autowired
    private ShipService(ShipRepository repoShip) {
        this.repoShip = repoShip;
    }

    public synchronized Ship guardarShip(Ship entrada) {
        return repoShip.save(entrada);
    }

    public Optional<Ship> buscarIdShip(int id) {
        return repoShip.findById((long) id);
    }
    public Optional<Ship> buscarIdShip2(int id){
        return repoShip.findById(id);
    }
    public List<Ship> listarShip() {
        return repoShip.findAll();
    }

    public Long contarShip() {
        return repoShip.count();
    }

    public void borrarShip(Ship entidad) {
        repoShip.delete(entidad);
    }

    public List<Ship> listarShipPorName(String nombre) {
        return repoShip.findByNameStartsWithIgnoreCase(nombre);
    }
}