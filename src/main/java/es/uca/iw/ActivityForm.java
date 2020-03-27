package es.uca.iw;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import org.springframework.beans.factory.annotation.Autowired;

import es.uca.iw.backend.Activity;
import es.uca.iw.backend.ActivityService;
import es.uca.iw.backend.Servicio;
import es.uca.iw.backend.ServicioService;
import es.uca.iw.backend.Ship;
import es.uca.iw.backend.ShipService;
import org.vaadin.gatanaso.MultiselectComboBox;

public class ActivityForm extends FormLayout {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private NumberField id = new NumberField("Id");
    private NumberField capacity = new NumberField("Capacidad");
    private TextField description = new TextField("Descripción");
    private TimePicker init_time = new TimePicker("Hora");
    private TimePicker time = new TimePicker("Hora");

    private TextField name = new TextField("Nombre");
    private NumberField price = new NumberField("Precio");
    private ComboBox<Ship> ship = new ComboBox<>("Barco");
    MultiselectComboBox<String> diaSemana = new MultiselectComboBox("Días de la semana");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private ActivityService servicioservice;
    private ActivityView servicioView;
    private ServicioService services;


    @Autowired
    public ActivityForm(ActivityView servicioView, ActivityService service, ShipService shipservice, ServicioService services) {
        this.services = services;
        this.servicioservice = service;
        this.servicioView = servicioView;
        save.addClickShortcut(Key.ENTER);
        id.setVisible(false);
        name.setRequired(true);
        description.setRequired(true);
        ship.setRequired(true);
        time.setLabel("Duración");
        init_time.setLabel("Hora");
        time.setPlaceholder("Duración");
        init_time.setPlaceholder("Hora");
        diaSemana.setItems("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo");

        price.setMin(0d);
        price.setMax(9999999999999d);
        capacity.setMin(0d);
        capacity.setMax(9999999999999d);
        ship.setItems(shipservice.listarShip());

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id, capacity, description, init_time, time, name, price, diaSemana, ship, buttons);

        save.addClickListener(event -> {
            if (id.getValue() != null && capacity.getValue() != null && capacity.getValue().intValue() > 0 && ship.getValue() != null &&
             time.getValue() != null && init_time.getValue() != null && description.getValue() != "" && name.getValue() != "" && price.getValue() != null && !diaSemana.getValue().isEmpty()){


                 Activity a = new Activity(id.getValue().intValue(), name.getValue(), description.getValue()
                , init_time.getValue(), time.getValue(), price.getValue().floatValue(), capacity.getValue().intValue(), diaSemana.getValue());

                servicioservice.guardarActivity(a);
                Servicio aux = services.buscarPorIdServicioYTipo(a.getId(),2);
                shipservice.guardarShip(ship.getValue());
                Servicio b = new Servicio();
                if(aux == null){
                     b = new Servicio(a.getId(), 2, ship.getValue());
                }else{
                     b = new Servicio(aux.getId(),a.getId(), 2, ship.getValue());
                }
                services.guardarService(b);
                setActivity(new Activity("", "", "00:00","00:00", 0, 0, ""));
                this.servicioView.updateList();
            } else{
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            }

        });
        delete.addClickListener(event -> {
                Activity a = new Activity(id.getValue().intValue(), name.getValue(), description.getValue(),
                init_time.getValue() , time.getValue(),
                price.getValue().floatValue(), capacity.getValue().intValue(), diaSemana.getValue());
                Servicio b = services.buscarPorIdServicioYTipo(a.getId(), 2);
                if(b != null){
                    services.borrarService(b);
                    servicioservice.borrarActivity(a);
                }
                setActivity(new Activity("", "", "00:00","00:00", 0, 0, ""));
                this.servicioView.updateList();
            
                
        });
        
    }

    public void setActivity(Activity service) {

          if(service!=null) {


              id.setValue((double) service.getId());
              init_time.setValue(service.getTimeParsed());
              description.setValue(service.getDescription());
              price.setValue((double) service.getPrice());
              name.setValue(service.getName());
              capacity.setValue((double) service.getCapacity());
              time.setValue(service.getTimeParsed());
              diaSemana.setValue(service.diasToSetString());
              Servicio aux = services.buscarPorIdServicioYTipo(service.getId(), 2);
              if (aux != null) {
                  ship.setValue(aux.getShip());
              }
          }
    }

}

