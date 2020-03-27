package es.uca.iw;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import es.uca.iw.backend.City;
import es.uca.iw.backend.CityService;

public class CityForm extends FormLayout{
    
	private static final long serialVersionUID = 1L;
	private CityView cityview;
    private CityService repoCity;
	private TextField name = new TextField("Nombre");
    private Button save = new Button("Añadir");
    private Button delete = new Button("Borrar");
    private BeanValidationBinder<City> binder = new BeanValidationBinder<>(City.class);

    public CityForm(CityView s, CityService city) {
    	this.cityview = s;
    	this.repoCity = city;
    	
        name.setRequired(true);
  
        
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(name, buttons);
        
        save.addClickShortcut(Key.ENTER);
        binder.bindInstanceFields(this);

        save.addClickListener(event -> {
        	if(binder.getBean() != null){
                save();
                setCity(new City(0, ""));
            }
        });

        delete.addClickListener(event -> {
            if (binder.getBean() != null)
                delete();
        });
    }

    public void save() {
    	if(name.getValue() != "") {
    		City city = binder.getBean();
    		if(binder.validate().isOk()) {
        
    			repoCity.guardarCity(city);
    			this.cityview.updateList();

    		}
    	}else {
    		Notification.show("Por favor rellene los campos correctamente", 3000, Notification.Position.BOTTOM_START);
    	}
    }
    public void setCity(City city) {

        if(city!=null)binder.setBean(city);

    }

    public void delete() {
        City city = binder.getBean();
        if (binder.validate().isOk()) {
            repoCity.borrarCity(city);
            this.cityview.updateList();
            setCity(new City());
        } else
            Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
    }
}


