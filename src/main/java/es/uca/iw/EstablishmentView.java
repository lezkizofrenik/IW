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

import es.uca.iw.backend.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

	@Route(value = "EstablishmentView", layout = MainView.class)
	@Secured({ "Admin", "Gerente" })
	@PageTitle("Gestión de los establecimientos")
	public class EstablishmentView extends AbstractView {

	    private static final long serialVersionUID = 1L;
	    private EstablishmentForm form;
	    private Grid<Establishment> grid = new Grid<>(Establishment.class);
	    private TextField filterText = new TextField();
		private EstablishmentService service;
		private boolean formStatus = false;
	    @Autowired
	    EstablishmentView(EstablishmentService service, CityService serviceCity, ServicioService serviceService, ShipService shipservice, CiteService citeservice){
	        this.service = service;
	        this.form = new EstablishmentForm(this, service, serviceCity, serviceService, shipservice, citeservice);
	        H1 titulo = new H1("Gestión de los establecimientos");
	        
	        Button addServicioBtn = new Button("Añade un establecimiento");

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
			grid.setColumns("name","description");
			grid.getColumnByKey("name").setHeader("Nombre");
			grid.getColumnByKey("description").setHeader("Descripción");
			grid.addColumn(Establishment::getTypeParsed).setHeader("Personas");

			grid.getColumns()
            .forEach(establishmentColumn -> establishmentColumn.setAutoWidth(true)); //Columnas autoajustables

	        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
	        mainContent.setSizeFull();
	        grid.setSizeFull();

	        add(titulo, toolbar, mainContent);

	        setSizeFull();

	        updateList();

	        form.setEstablishment(new Establishment(0,"","", false));

			grid.asSingleSelect().addValueChangeListener(event ->{
				if(grid.asSingleSelect().getValue()!=null)	form.setEstablishment(grid.asSingleSelect().getValue());
				else  form.setEstablishment(new Establishment(0,"","", false));

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

