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

import es.uca.iw.backend.CityService;
import es.uca.iw.backend.Restaurant;

import es.uca.iw.backend.RestaurantService;
import es.uca.iw.backend.ServicioService;
import es.uca.iw.backend.ShipService;
import es.uca.iw.backend.TableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

	@Route(value = "RestaurantView", layout = MainView.class)
	@Secured({ "Admin", "Gerente" })
	@PageTitle("Gestión de los Restaurantes")
	public class RestaurantView extends AbstractView {

	    private static final long serialVersionUID = 1L;
	    private RestaurantForm form;
	    private Grid<Restaurant> grid = new Grid<>(Restaurant.class);
	    private TextField filterText = new TextField();
		private RestaurantService service;
		private boolean formStatus = false;
	    @Autowired
	    RestaurantView(RestaurantService service, TableService ts, CityService serviceCity, ServicioService serviceService, ShipService shipservice){
	        this.service = service;
	        this.form = new RestaurantForm(this, ts, service, serviceCity, serviceService,shipservice);
	        H1 titulo = new H1("Gestión de restaurantes");
	        
	        Button addServicioBtn = new Button("Añade un Restaurante");
	        addServicioBtn.addClickListener(e -> {
	            grid.asSingleSelect().clear(); // clear para que borre si habia algo antes
	            form.setRestaurant(new Restaurant(0,"","")); // instancia un nuevo
	        });
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
			grid.setColumns("name", "description");
			grid.getColumnByKey("name").setHeader("Nombre");
			grid.getColumnByKey("description").setHeader("Descripción");
	        grid.getColumns()
            .forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true)); //Columnas autoajustables

	        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
	        mainContent.setSizeFull();
	        grid.setSizeFull();

	        add(titulo, toolbar, mainContent);

	        setSizeFull();

	        updateList();

			addServicioBtn.addClickListener(e -> {
				formStatus = !formStatus;
				if(formStatus){
					mainContent.add(form);
					grid.asSingleSelect().clear();

				}
				else mainContent.remove(form);

			});

	        form.setRestaurant(new Restaurant(0,"",""));
			grid.asSingleSelect().addValueChangeListener(event ->{
				if(grid.asSingleSelect().getValue()!=null)	form.setRestaurant(grid.asSingleSelect().getValue());
				else form.setRestaurant(new Restaurant(0,"",""));

			});

	    }
	    
	    public void updateList() {
	        if (filterText.isEmpty())
	            grid.setItems(service.listar());
	        else
	            grid.setItems(service.listarByName(filterText.getValue()));
	    }
	    
}

