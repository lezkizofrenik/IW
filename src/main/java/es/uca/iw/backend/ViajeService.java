package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ViajeService {
    private ViajeRepository repoViaje;

    @Autowired
    private ViajeService(ViajeRepository repoViaje) {
        this.repoViaje = repoViaje;
    }

    public synchronized Viaje guardarViaje(Viaje entrada) {
        return repoViaje.save(entrada);
    }

    public Viaje buscarIdViaje(int id) {
        return repoViaje.findById((id));
    }
    public List<Viaje> listarViaje() {
        return repoViaje.findAll();
    }

    public Long contarViaje() {
        return repoViaje.count();
    }

    public void borrarViaje(Viaje entidad) {
        repoViaje.delete(entidad);
    }

    public List<Viaje> listarViajePorName(String nombre) {
        return repoViaje.findByNameStartsWithIgnoreCase(nombre);
    }
}