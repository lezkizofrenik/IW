package es.uca.iw;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import es.uca.iw.backend.City;
import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Viaje;
import es.uca.iw.backend.ViajeService;
import org.vaadin.gatanaso.MultiselectComboBox;

public class ViajeForm extends FormLayout {

    private static final long serialVersionUID = 1L;
    private ViajeView viajeView;
    private NumberField id = new NumberField("Id");
    private TextField name = new TextField("Nombre");
    MultiselectComboBox<String> viaje = new MultiselectComboBox("Ruta");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private BeanValidationBinder<Viaje> binder = new BeanValidationBinder<>(Viaje.class);
    private ViajeService service;
    private CityService city;

    public ViajeForm(ViajeView s, ViajeService service, CityService city) {
        this.city = city;
        this.service = service;
        this.viajeView = s;
        id.setVisible(false);
        name.setVisible(true);
        viaje.setRequired(true);
        
        List<City> ciudades = this.city.listarCity();
        Set<String> c = new HashSet<>();
        Iterator<City> itrc = ciudades.iterator();
        while(itrc.hasNext()){
        	c.add(itrc.next().getName());
        }
        viaje.setItems(c);



        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id, name, viaje, buttons);

        save.addClickListener(event -> {
            if(id.getValue() != null && name.getValue() != "" && !viaje.getValue().isEmpty()){
                save();
                Notification.show("Viaje Creado", 3000, Notification.Position.BOTTOM_START);
            }else
                Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
            
        });


        delete.addClickListener(event -> {
            if (id.getValue() != null && name.getValue() != null){
                delete();
                Notification.show("Viaje Borrado", 3000, Notification.Position.BOTTOM_START);
            }

        });


    }

    public void save() {
        if (binder.validate().isOk()) {
            String [] string = viaje.getValue().toArray(new String [viaje.getValue().size()]);

                Viaje s = new Viaje(id.getValue().intValue(), name.getValue(), String.join("/", string));

                service.guardarViaje(s);
                this.viajeView.updateList();
        }else 
            Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
    }
    


	public void delete() {
		if (binder.validate().isOk()) {
			Viaje s = service.buscarIdViaje(id.getValue().intValue());
			service.borrarViaje(s);
			this.viajeView.updateList();
         } else
            Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
	}

    public void deleteRoute(){
        if (binder.validate().isOk()) { 
            Viaje s = new Viaje(id.getValue().intValue(), name.getValue(), "");
            service.guardarViaje(s);
            this.viajeView.updateList();
        }else 
        	Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
           
    }

    public void setViaje(Viaje Viaje) {
        if (Viaje != null) {
        	
            setVisible(true);
            id.setValue((double) Viaje.getId());
            name.setValue(Viaje.getName());
            Set<String> set =  new HashSet<String>();
            if(!Viaje.getRoute().isEmpty()){
                String[] parts = Viaje.getRoute().split("/");
                for(int i = 0; i < parts.length; i++){
                    set.add(parts[i]);
                }
                viaje.setValue(set);

            }
            else viaje.setValue(null);


        }else
            setVisible(false);
    }
}