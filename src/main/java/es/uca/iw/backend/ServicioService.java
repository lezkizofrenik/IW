package es.uca.iw.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ServicioService {
    private ServicioRepository reposervicio;


    @Autowired
    private ServicioService(ServicioRepository reposervicio) {
        this.reposervicio = reposervicio;
    }
    public synchronized Servicio guardarService(Servicio entrada) {
        return reposervicio.save(entrada);
    }
    public Optional<Servicio> buscarIdService(long id) {
        return reposervicio.findById(id);
    }
    public List<Servicio> listarService() {
        return reposervicio.findAll();
    }

    public Long contarService() {
        return reposervicio.count();
    }

    public void borrarService(Servicio entidad) {
        reposervicio.delete(entidad);
    }

    public List<Servicio> buscarPorTipo(int tipo) {
        return reposervicio.findByType(tipo);
    }

    public List<Servicio> buscarPorShip(Ship ship){
        return reposervicio.findByShip(ship);
    }

    public List<Servicio> buscarPorShipYTipo(int tipo, Ship ship){
        return reposervicio.findByTypeAndShip(tipo, ship);
    }
    public Servicio buscarPorIdServicioYTipo(int idServicio, int tipo) {
        return reposervicio.findByIdServicioAndType(idServicio, tipo);
    }
    public List<Servicio> buscarRestaurant(Ship ship){
        return buscarPorShipYTipo(4, ship);
    }

    public List<Servicio> buscarHike(Ship ship){
        return buscarPorShipYTipo(1, ship);
    }
    public List<Servicio> buscarActivity(Ship ship){
        return buscarPorShipYTipo(2, ship);
    }

    public List<Servicio> buscarEstablishment(Ship ship){
        return buscarPorShipYTipo(3, ship);
    }

    public Servicio buscarIdRestaurant(int id) {
        return reposervicio.findByIdAndType(id,4);
    }
    public Servicio buscarIdHike(int id) {
        return reposervicio.findByIdAndType(id,1);
    }
    public Servicio buscarIdActivity(int id) {
        return reposervicio.findByIdAndType(id,2);
    }
    public Servicio buscarIdEstablishment(int id) {
        return reposervicio.findByIdAndType(id,3);
    }
    public List<Servicio> listHike(){ return reposervicio.findByType(1);}
    public List<Servicio> listActivity(){ return reposervicio.findByType(2);}
    public List<Servicio> listEstablishment(){ return reposervicio.findByType(3);}
    public List<Servicio> listRestaurant(){ return reposervicio.findByType(4);}

}