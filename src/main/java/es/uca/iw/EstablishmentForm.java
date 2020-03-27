package es.uca.iw;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import org.springframework.beans.factory.annotation.Autowired;

import es.uca.iw.backend.CiteService;
import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Establishment;
import es.uca.iw.backend.EstablishmentService;
import es.uca.iw.backend.Servicio;
import es.uca.iw.backend.ServicioService;
import es.uca.iw.backend.Ship;
import es.uca.iw.backend.ShipService;


public class EstablishmentForm extends FormLayout {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private NumberField id = new NumberField("Id");
    private TextField name = new TextField("Nombre");
    private TextField description = new TextField("Descripción");
    private ComboBox<Ship> ship = new ComboBox<>("Barco");
    private ComboBox<String> personas = new ComboBox<>("Personas");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private EstablishmentService servicioservice;
    private EstablishmentView servicioView;
    private ServicioService services;
    private boolean personasParsed;


    @Autowired
    public  EstablishmentForm(EstablishmentView servicioView, EstablishmentService service, CityService ships,
    ServicioService services, ShipService shipservice, CiteService citeservice) {
        this.servicioservice = service;
        this.servicioView = servicioView;
        this.services = services;
        save.addClickShortcut(Key.ENTER);
        personas.addValueChangeListener(personasevent-> {
            if(personas.getValue()== "Una"){
                personasParsed = false;
            }
            else if (personas.getValue() == "Varias") personasParsed = true;
        });
        id.setVisible(false);
        personas.setItems("Una", "Varias");
        name.setRequired(true);
        description.setRequired(true);
        ship.setRequired(true);
        personas.setRequired(true);
        
        ship.setItems(shipservice.listarShip());
        
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        add(id, name, description, ship, personas, buttons);

        save.addClickListener(event -> {
            if (id.getValue() != null && description.getValue() != "" && name.getValue() != "" && ship.getValue() != null && personas.getValue()!=null){
                Establishment a = new Establishment(id.getValue().intValue(),name.getValue(),description.getValue(), personasParsed);
                servicioservice.guardarEstablishment(a);               
                Servicio aux = services.buscarPorIdServicioYTipo(a.getId(),3);
                shipservice.guardarShip(ship.getValue());
                Servicio b = new Servicio();
                if(aux == null){
                     b = new Servicio(a.getId(), 3, ship.getValue());
                }else{
                     b = new Servicio(aux.getId(), a.getId(), 3, ship.getValue());
                }
                services.guardarService(b);
                setEstablishment(new Establishment(0, "", "", personasParsed));
                this.servicioView.updateList();
                
            }else{
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            }
        });
        delete.addClickListener(event -> {
            if (id.getValue() != null && description.getValue() != null && name.getValue() != null && !personas.isEmpty()){
                Establishment a = new Establishment(id.getValue().intValue(),name.getValue(),description.getValue(), personasParsed);
                Servicio b = services.buscarPorIdServicioYTipo(a.getId(), 3);
                
                if(b != null){
                    if(citeservice.searchEstablishment(a).size() == 0){
                        services.borrarService(b);
                        servicioservice.borrarEstablishment(a);
                    }else
                        Notification.show("Borre antes todas las citas");
                    
                }
                setEstablishment(new Establishment(0, "", "", false));
                this.servicioView.updateList();
            }
                
        });

    }

    public void setEstablishment(Establishment service ) {
    	if(service != null) {
            setVisible(true);

    		id.setValue((double) service.getId());
        	description.setValue(service.getDescription());
            name.setValue(service.getName());
            Servicio aux = services.buscarPorIdServicioYTipo(service.getId(),3);
            personas.setValue(service.getTypeParsed());
            if(aux != null)
                 ship.setValue(aux.getShip());
            

    	}

    }
}
