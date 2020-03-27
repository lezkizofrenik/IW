package es.uca.iw;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;

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

import org.springframework.beans.factory.annotation.Autowired;

import es.uca.iw.backend.Cite;
import es.uca.iw.backend.CiteService;
import es.uca.iw.backend.Establishment;
import es.uca.iw.backend.EstablishmentService;
import es.uca.iw.backend.ReservationEstablishmentService;
import es.uca.iw.backend.ServicioService;


public class CiteForm extends FormLayout {
    /**
     *
     */
    private static final long serialVersionUID = 1L; 
    private NumberField id = new NumberField("Id");
    private TimePicker init_time = new TimePicker("Hora");

    private ComboBox<Establishment> establishment = new ComboBox<>("Establecimiento");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private CiteService servicioservice;
    private CiteView servicioView;

    @Autowired
    public  CiteForm(CiteView servicioView, CiteService service, EstablishmentService ships,
            ServicioService services, ReservationEstablishmentService res) {
        this.servicioservice = service;
        this.servicioView = servicioView;
        save.addClickShortcut(Key.ENTER);
        id.setVisible(false);
        init_time.setRequired(true);
        
        
        establishment.setItems(ships.listar());


        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id,  init_time,  establishment, buttons);

        save.addClickListener(event -> {
            if (id.getValue() != null &&  init_time.getValue() != null && establishment.getValue() != null){
                Establishment aux = ships.searchById(establishment.getValue().getId());
                if(aux != null){
	                Cite a = new Cite(id.getValue().intValue(), init_time.getValue(), establishment.getValue());
	                servicioservice.guardarCite(a);
	                setCite(new Cite(0,"00:00",new Establishment()));
	                this.servicioView.updateList(); 
                }else{
                    Notification.show("Por favor Seleccione un Establecimiento", 3000, Notification.Position.BOTTOM_START);
                }       
            }else{
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            }

        });
        delete.addClickListener(event -> {
            if (id.getValue() != null){
                Cite a = new Cite(id.getValue().intValue(), init_time.getValue(), establishment.getValue());
                if(res.listReservationByCite(a) == null) {
	                servicioservice.borrarCite(a);
                }else {
                	Notification.show("Primero borre las reservas hecha a esa cita", 3000, Notification.Position.BOTTOM_START);

                }
                setCite(new Cite(0, "00:00", new Establishment()));
                this.servicioView.updateList();
            }
                
        });

    }

    public void setCite(Cite service) {
    	if(service != null) {
            id.setValue((double) service.getId());


        	init_time.setValue(service.getTimeParsed());

            establishment.setValue(service.getEstablishment());

    	}else {
    		id.setValue(0d);
        	init_time.setValue(null);

        }
    }

}
