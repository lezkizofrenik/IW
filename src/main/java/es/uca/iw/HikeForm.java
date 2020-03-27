package es.uca.iw;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

import es.uca.iw.backend.City;
import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Hike;
import es.uca.iw.backend.HikeService;



public class HikeForm extends FormLayout {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private NumberField id = new NumberField("Id");
    private NumberField capacity = new NumberField("Capacidad");
    private TimePicker init_time = new TimePicker("Hora");
    private TimePicker time = new TimePicker("Duración");
    private TextField description = new TextField("Descripción");
    private TextField name = new TextField("Nombre");
    private NumberField price = new NumberField("Precio");
    private ComboBox<City> city = new ComboBox<>("Ciudad");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private HikeView servicioView;

    @Autowired
    public  HikeForm(HikeView servicioView, HikeService hikeservice, CityService cityService) {
        setVisible(false);
        this.servicioView = servicioView;
        save.addClickShortcut(Key.ENTER);
        id.setVisible(false);
        name.setRequired(true);
        description.setRequired(true);
        city.setRequired(true);


        init_time.setLabel("Hora");
        init_time.setPlaceholder("Hora");
        init_time.setStep(Duration.ofMinutes(15));
        time.setStep(Duration.ofMinutes(15));

        price.setMin(0d);
        price.setMax(9999999999999d);
        capacity.setMin(1d);
        capacity.setMax(9999999999999d);
        capacity.setHasControls(true);

        List<City> barcos = cityService.listarCity();
        Iterator<City> itrb = barcos.iterator();
        List<City> itri = new LinkedList<>();
        while(itrb.hasNext()){
            itri.add(itrb.next());
        }
        city.setItems(itri);
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id,time, capacity, description, init_time, time, name, price, city, buttons);
        save.addClickListener(event -> {
            if (id.getValue() != null  && init_time.getValue() != null && description.getValue() != "" && name.getValue() != "" && price.getValue() != null
            && time.getValue() != null && time.getValue()!=null && capacity.getValue() != null && capacity.getValue().intValue() > 0
            && city.getValue() != null){


                Hike a = new Hike(id.getValue().intValue(),city.getValue(), init_time.getValue(),
                time.getValue(),capacity.getValue().intValue(),price.getValue().intValue(), description.getValue(),name.getValue());
                hikeservice.guardarHike(a);
                setHike(new Hike(0, new City(),"10:00", "10:00", 1,0,"",""));
                this.servicioView.updateList();
                
            }else{
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            }
        });
        delete.addClickListener(event -> {
            if (id.getValue() != null && init_time.getValue() != null && description.getValue() != null && name.getValue() != null && price.getValue() != null){
                Hike a = new Hike(id.getValue().intValue(),city.getValue(), init_time.getValue(),
                time.getValue(),capacity.getValue().intValue(),price.getValue().intValue(), description.getValue(),name.getValue());
                hikeservice.borrarHike(a);
                setHike(new Hike(0, new City(), "10:00", "10:00", 1, 0, "", ""));
                this.servicioView.updateList();
            }
                
        });

    }

    public void setHike(Hike service) {
    	if(service != null) {
            setVisible(true);
    		id.setValue((double) service.getId());
        	init_time.setValue(service.initParsed());
        	description.setValue(service.getDescription());
        	price.setValue((double) service.getPrice());
        	name.setValue(service.getName());
            capacity.setValue((double) service.getCapacity());
            time.setValue(service.timeParsed());
            city.setValue(service.getCity());


    	}else {
            setVisible(false);
        }
    }


}
