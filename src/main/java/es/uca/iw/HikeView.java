package es.uca.iw;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.backend.City;
import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Hike;

import java.time.Duration;
import java.util.Date;

import es.uca.iw.backend.HikeService;
import es.uca.iw.backend.ServicioService;
import es.uca.iw.backend.ShipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

	@Route(value = "HikeView", layout = MainView.class)
	@Secured({ "Admin", "Gerente" })
	@PageTitle("Gestión de las Excursiones")
	public class HikeView extends AbstractView {

	    private static final long serialVersionUID = 1L;
	    private HikeForm form;
	    private Grid<Hike> grid = new Grid<>(Hike.class);
	    private TextField filterText = new TextField();
		private HikeService service;
		private boolean formStatus = false;
	    @Autowired
	    HikeView(HikeService service, CityService serviceCity){
	        this.service = service;
	        this.form = new HikeForm(this, service, serviceCity);
	        H1 titulo = new H1("Registro de las excursiones");

	        Button addServicioBtn = new Button("Añade una excursion");

	        filterText.setPlaceholder("Filtrar por nombre"); // poner el campo
	        filterText.setClearButtonVisible(true); // poner la cruz para borrar
	        filterText.setValueChangeMode(ValueChangeMode.EAGER); // que se hagan los cambios cuando se escriba
	        filterText.addValueChangeListener(event -> {
	            if (service.listar() != null)
	                updateList();
	            else {
	                filterText.clear();
	                Notification.show("No hay barco disponibles", 3000, Notification.Position.BOTTOM_START);
	            }

	        });
	        updateList(); 
	        
	        HorizontalLayout toolbar = new HorizontalLayout(filterText, addServicioBtn);
			grid.setColumns("name","description","init" ,"time", "capacity","city", "price");
			grid.getColumnByKey("capacity").setHeader("Capacidad");
			grid.getColumnByKey("description").setHeader("Descripción");
			grid.getColumnByKey("init").setHeader("Hora de inicio");
			grid.getColumnByKey("name").setHeader("Nombre");
			grid.getColumnByKey("price").setHeader("Precio");
			grid.getColumnByKey("time").setHeader("Duración");

			grid.getColumnByKey("city").setHeader("Ciudad");
	        
	        grid.getColumns()
            .forEach(hikeColumn -> hikeColumn.setAutoWidth(true)); //Columnas autoajustables
	        
	        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
	        mainContent.setSizeFull();
	        grid.setSizeFull();
			addServicioBtn.addClickListener(e -> {
				formStatus = !formStatus;
				if(formStatus){
					mainContent.add(form);
					grid.asSingleSelect().clear();

				}
				else mainContent.remove(form);

			});

			add(titulo, toolbar, mainContent);

	        setSizeFull();

	        updateList();

	        form.setHike(new Hike(0, new City(), "00:00", "00:00", 1, 0, "", ""));

			grid.asSingleSelect().addValueChangeListener(event ->{
				if(grid.asSingleSelect().getValue()!=null)	form.setHike(grid.asSingleSelect().getValue());
				else  form.setHike(new Hike(0, new City(), "00:00","00:00", 1, 0, "", ""));
			});


	    }
	    
	    public void updateList() {
	        if (filterText.isEmpty())
	            grid.setItems(service.listar());
	        else
	            grid.setItems(service.buscarByName(filterText.getValue()));
	    }
	    
}

