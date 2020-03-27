package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.backend.City;


import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private CityRepository repoCity;

    @Autowired
    private CityService(CityRepository repoCity) {
        this.repoCity = repoCity;
    }

    public synchronized City guardarCity(City entrada) {
        return repoCity.save(entrada);
    }   
    public synchronized void borrarCity(City entrada) {
         repoCity.delete(entrada);
    }

    public Optional<City> buscarIdCity(Long id) {
        return repoCity.findById(id);
    }
    public List<City> buscarCityporNombre(String name) {
        return repoCity.findByNameStartsWithIgnoreCase(name);
    }
    public List<City> listarCityporNombre(String name) {
        return repoCity.findByName(name);
    }
    public List<City> listarCity() {
        return repoCity.findAll();
    }
}