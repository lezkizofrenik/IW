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

import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Restaurant;
import es.uca.iw.backend.RestaurantService;
import es.uca.iw.backend.Servicio;
import es.uca.iw.backend.ServicioService;
import es.uca.iw.backend.Ship;
import es.uca.iw.backend.ShipService;
import es.uca.iw.backend.TableService;


public class RestaurantForm extends FormLayout {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private NumberField id = new NumberField("Id");
    private TextField name = new TextField("Nombre");
    private TextField description = new TextField("Descripción");
    private ComboBox<Ship> ship = new ComboBox<>("Barco");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private RestaurantService servicioservice;
    private RestaurantView servicioView;
    private ServicioService services;

    @Autowired
    public  RestaurantForm(RestaurantView servicioView, TableService ts, RestaurantService service, CityService ships,ServicioService services, ShipService shipservice) {
        this.servicioservice = service;
        this.servicioView = servicioView;
        this.services = services;
        save.addClickShortcut(Key.ENTER);
        id.setVisible(false);
        name.setRequired(true);
        description.setRequired(true);
        ship.setRequired(true);
        ship.setItems(shipservice.listarShip());

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id,name,description,ship,buttons);

        save.addClickListener(event -> {
            if (id.getValue() != null && description.getValue() != "" && name.getValue() != "" && ship.getValue() != null){
                Restaurant a = new Restaurant(id.getValue().intValue(),name.getValue(),description.getValue());
                servicioservice.guardarRestaurant(a);
                Servicio aux = services.buscarPorIdServicioYTipo(a.getId(),4);
                shipservice.guardarShip(ship.getValue());
                Servicio b = new Servicio();
                if(aux == null){
                     b = new Servicio(a.getId(), 4, ship.getValue());
                }else{
                     b = new Servicio(aux.getId(),a.getId(), 4, ship.getValue());
                }
                services.guardarService(b);
                this.servicioView.updateList();
                
            }else{
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            }
        });
        delete.addClickListener(event -> {
            if (id.getValue() != null && description.getValue() != null && name.getValue() != null){
                Restaurant a = new Restaurant(id.getValue().intValue(),name.getValue(),description.getValue());
                Servicio b = services.buscarPorIdServicioYTipo(a.getId(), 4);
                if(b != null){
                	if(ts.searchByRestaurant(a).size() == 0) {
                		services.borrarService(b);
                    
                		servicioservice.borrarRestaurant(a);
                	}else {
                		Notification.show("Borre antes todas las mesas del restaurante", 3000, Notification.Position.BOTTOM_START);
                	}
                }
                setRestaurant(new Restaurant(0, "", ""));
                this.servicioView.updateList();
            }
                
        });

    }

    public void setRestaurant(Restaurant service) {
    	if(service != null) {
            setVisible(true);
    		id.setValue((double) service.getId());
        	description.setValue(service.getDescription());
        	name.setValue(service.getName());
            Servicio aux = services.buscarPorIdServicioYTipo(service.getId(),4);

            if(aux == null){
            }else{
                 ship.setValue(aux.getShip());
            }
            

    	}else {
            setVisible(false);
        }
    }
}
