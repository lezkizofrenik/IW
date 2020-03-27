package es.uca.iw;

import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Ship;
import es.uca.iw.backend.ShipService;
import es.uca.iw.backend.Viaje;
import es.uca.iw.backend.ViajeService;

public class ShipForm extends FormLayout {

    private static final long serialVersionUID = 1L;
    private ShipView shipView;
    // private ClientService repoClient;
    private NumberField id = new NumberField("Id");
    private TextField name = new TextField("Nombre");
    private ComboBox<Viaje> viaje = new ComboBox<>("Viaje");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private BeanValidationBinder<Ship> binder = new BeanValidationBinder<>(Ship.class);
    private ShipService service;
    
    public ShipForm(ShipView s, ShipService service, CityService city,ViajeService viajeservice) {
        this.service = service;
        this.shipView = s;
        id.setVisible(false);
        name.setRequired(true);
        viaje.setVisible(true);
        viaje.setItems(viajeservice.listarViaje());

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id, name, viaje, buttons);

        save.addClickListener(event -> {
            if (id.getValue() != null && name.getValue() != "" && viaje.getValue() != null){
                Viaje aux = viajeservice.buscarIdViaje(viaje.getValue().getId());
                if(aux != null){
                viajeservice.guardarViaje(aux);
                Ship ship = new Ship(id.getValue().intValue(), name.getValue(), viaje.getValue());
                service.guardarShip(ship);
                setShip(new Ship(0,"",new Viaje()));
                this.shipView.updateList();
                }else{
                    Notification.show("Por favor asigne un viaje al barco", 3000, Notification.Position.BOTTOM_START);
                }
             }else{
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            }
        });

        delete.addClickListener(event -> {
            if (id.getValue() != null && name.getValue() != null)
                delete();
        });
    }



    public void setShip(Ship ship) {
        if (ship != null) {
            setVisible(true);
            id.setValue((double) ship.getId());
            name.setValue(ship.getName());         
            viaje.setValue(ship.getViaje());
            }            
            else {
            setVisible(false);
        }
    }

    public void delete() {
        if (binder.validate().isOk()) {
            Optional<Ship> s = service.buscarIdShip2(id.getValue().intValue());
            service.borrarShip(s.get());
            this.shipView.updateList();
        } else
            Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
    }
}