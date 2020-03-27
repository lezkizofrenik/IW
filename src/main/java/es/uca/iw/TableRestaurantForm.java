package es.uca.iw;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;

import org.springframework.beans.factory.annotation.Autowired;

import es.uca.iw.backend.TableRestaurant;
import es.uca.iw.backend.TableService;
import es.uca.iw.backend.Restaurant;
import es.uca.iw.backend.RestaurantService;
import es.uca.iw.backend.ServicioService;


public class TableRestaurantForm extends FormLayout {
    /**
     *
     */
    private static final long serialVersionUID = 1L; 
    private NumberField id = new NumberField("Id");

    private NumberField capacity = new NumberField("Capacidad");
    private ComboBox<Restaurant> restaurant = new ComboBox<>("Restaurante");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private TableService servicioservice;
    private TableRestaurantView servicioView;

    @Autowired
    public TableRestaurantForm(TableRestaurantView servicioView, TableService service, RestaurantService ships,
           ServicioService services) {
        this.servicioservice = service;
        this.servicioView = servicioView;
        save.addClickShortcut(Key.ENTER);
        id.setVisible(false);
        restaurant.setItems(ships.listar());
        capacity.setMin(1);
        capacity.setMax(6);
        capacity.setValue(1d);
        capacity.setHasControls(true);


        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id,capacity, restaurant,buttons);

        save.addClickListener(event -> {
            if (id.getValue() != null && capacity.getValue().intValue()>0 && capacity.getValue() != null &&
             restaurant.getValue() != null && restaurant.getValue().getName() != ""){
                
            	Restaurant aux = ships.searchById(restaurant.getValue().getId());
                if(aux != null){
                TableRestaurant a = new TableRestaurant(id.getValue().intValue(),capacity.getValue().intValue(),restaurant.getValue());
                servicioservice.guardar(a);
                setTableRestaurant(new TableRestaurant(0,1,new Restaurant()));
                this.servicioView.updateList();
                }else
                    Notification.show("Por favor Seleccione un restaurante", 3000, Notification.Position.BOTTOM_START);  
             }else
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            
        });
        delete.addClickListener(event -> {
            if (id.getValue() != null){
                TableRestaurant a = new TableRestaurant(id.getValue().intValue(),capacity.getValue().intValue(),restaurant.getValue());
                servicioservice.guardar(a);
                servicioservice.borrar(a);
                //services.borrarService(b);
                setTableRestaurant(new TableRestaurant(0,1,new Restaurant()));
                this.servicioView.updateList();
            }
                
        });

    }

    public void setTableRestaurant(TableRestaurant service) {
    	if(service != null) {
            setVisible(true);
            id.setValue((double) service.getId());
            capacity.setValue((double)service.getCapacity());
            Restaurant aux = service.getRestaurant();
            if(aux != null)
            	restaurant.setValue(aux);
 
    	}else
            setVisible(false);
        
    }

}
