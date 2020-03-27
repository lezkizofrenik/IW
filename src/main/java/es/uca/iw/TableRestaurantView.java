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

import es.uca.iw.backend.TableRestaurant;

import es.uca.iw.backend.TableService;
import es.uca.iw.backend.Restaurant;
import es.uca.iw.backend.RestaurantService;
import es.uca.iw.backend.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

	@Route(value = "TableRestaurantView", layout = MainView.class)
	@Secured({ "Admin", "Gerente" })
	@PageTitle("Gestión de los mesas de los restaurantes")
	public class TableRestaurantView extends AbstractView {

	    private static final long serialVersionUID = 1L;
	    private TableRestaurantForm form;
	    private Grid<TableRestaurant> grid = new Grid<>(TableRestaurant.class);
	    private TextField filterText = new TextField();
		private TableService service;
		private boolean formStatus = false;
	    @Autowired
	    TableRestaurantView(TableService service, RestaurantService serviceCity, ServicioService serviceService){
	        this.service = service;
	        this.form = new TableRestaurantForm(this, service, serviceCity, serviceService);
	        H1 titulo = new H1("Gestión de mesas");
	        
	        Button addServicioBtn = new Button("Añade una mesa a un Restaurante");

	        filterText.setVisible(false);
	        filterText.setPlaceholder("Filtrar por restaurante"); // poner el campo
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
			grid.setColumns("restaurant", "capacity");
			grid.getColumnByKey("capacity").setHeader("Capacidad");
			grid.getColumnByKey("restaurant").setHeader("Restaurante");
			
	        grid.getColumns()
            .forEach(tablerestaurantColumn -> tablerestaurantColumn.setAutoWidth(true)); //Columnas autoajustables

	        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
	        mainContent.setSizeFull();
			addServicioBtn.addClickListener(e -> {
				formStatus = !formStatus;
				if(formStatus){
					mainContent.add(form);
					grid.asSingleSelect().clear();

				}
				else mainContent.remove(form);

			});

			grid.setSizeFull();

	        add(titulo, toolbar, mainContent);

	        setSizeFull();

	        updateList();

	        form.setTableRestaurant(new TableRestaurant(0,1,new Restaurant()));

	        grid.asSingleSelect().addValueChangeListener(event -> form.setTableRestaurant(grid.asSingleSelect().getValue()));
			grid.asSingleSelect().addValueChangeListener(event ->{
				if(grid.asSingleSelect().getValue()!=null)	form.setTableRestaurant(grid.asSingleSelect().getValue());
				else   form.setTableRestaurant(new TableRestaurant(0,1,new Restaurant()));;
			});
	    }
	    
	    public void updateList() {
	        if (filterText.isEmpty())
	            grid.setItems(service.listar());
	        else
	            grid.setItems(service.listar());
	    }
	    
}

