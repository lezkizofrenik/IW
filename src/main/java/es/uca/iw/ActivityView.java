package es.uca.iw;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.backend.Activity;

import java.util.Date;

import es.uca.iw.backend.ActivityService;
import es.uca.iw.backend.ServicioService;
import es.uca.iw.backend.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

	@Route(value = "ActivityView", layout = MainView.class)
	@Secured({ "Admin", "Gerente" })
	@PageTitle("Gestión de las actividades")
	public class ActivityView extends AbstractView {

	    private static final long serialVersionUID = 1L;
	    private ActivityForm form;
	    private Grid<Activity> grid = new Grid<>(Activity.class);
	    private TextField filterText = new TextField();
		private ActivityService service;
		private boolean formStatus = false;
	    @Autowired
	    ActivityView(ActivityService service, ShipService serviceShip, ServicioService serviceService){
	        this.service = service;
	        this.form = new ActivityForm(this, service, serviceShip, serviceService);
	        H1 titulo = new H1("Gestión de las actividades");
	        
	        Button addServicioBtn = new Button("Añade una actividad");

	        filterText.setPlaceholder("Filtrar por nombres");
	        filterText.setClearButtonVisible(true);
	        filterText.setValueChangeMode(ValueChangeMode.EAGER);
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
			grid.setColumns( "name",  "description", "capacity", "init", "time", "price", "dias");
			grid.getColumnByKey("name").setHeader("Nombre");
            grid.getColumnByKey("description").setHeader("Descripción");
            grid.getColumnByKey("init").setHeader("Hora de inicio");
			grid.getColumnByKey("time").setHeader("Duración");
			grid.getColumnByKey("price").setHeader("Precio");
			grid.getColumnByKey("capacity").setHeader("Capacidad");
			
	        grid.getColumns()
            .forEach(activityColumn -> activityColumn.setAutoWidth(true)); //Columnas autoajustables

	        HorizontalLayout mainContent = new HorizontalLayout(grid);
	        
	        mainContent.setSizeFull();
	        grid.setSizeFull();


			add(titulo, toolbar, mainContent);

			setSizeFull();

	        updateList();

	        form.setActivity(new Activity("", "", "00:00", "00:00",0, 1, ""));

	        grid.asSingleSelect().addValueChangeListener(event ->{
	        	if(grid.asSingleSelect().getValue()!=null)	form.setActivity(grid.asSingleSelect().getValue());
				else form.setActivity(new Activity("", "", "00:00", "00:00", 0, 1, ""));
			});

			addServicioBtn.addClickListener(e -> {
				formStatus = !formStatus;
				if(formStatus){
					mainContent.add(form);
					grid.asSingleSelect().clear();

				}
				else mainContent.remove(form);

			});

	    }
	    
	    public void updateList() {
	        if (filterText.isEmpty())
	            grid.setItems(service.listar());
	        else
	            grid.setItems(service.listarByName(filterText.getValue()));
	    }
	    
}

