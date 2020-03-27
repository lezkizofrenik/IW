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

import es.uca.iw.backend.Cite;
import es.uca.iw.backend.CiteService;
import es.uca.iw.backend.Establishment;
import es.uca.iw.backend.EstablishmentService;
import es.uca.iw.backend.ReservationEstablishmentService;
import es.uca.iw.backend.ServicioService;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.access.annotation.Secured;

	@Route(value = "CiteView", layout = MainView.class)
	@Secured({ "Admin", "Gerente" })
	@PageTitle("Gestión de las citas")
	public class CiteView extends AbstractView {

	    private static final long serialVersionUID = 1L;
	    private CiteForm form;
	    private Grid<Cite> grid = new Grid<>(Cite.class);
	    private TextField filterText = new TextField();
		private CiteService service;
		private boolean formStatus = false;
	    @Autowired
	    CiteView(CiteService service, EstablishmentService serviceCity, ServicioService serviceService, ReservationEstablishmentService res){
	        this.service = service;
	        this.form = new CiteForm(this, service, serviceCity, serviceService, res);
	        H1 titulo = new H1("Gestión de las citas");
	        
	        Button addServicioBtn = new Button("Añade una cita a un establecimiento");

	        filterText.setVisible(false);
	        filterText.setPlaceholder("Filtrar por establecimiento"); // poner el campo
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

	        HorizontalLayout toolbar = new HorizontalLayout(filterText, addServicioBtn);
			grid.setColumns("init","establishment");
			grid.getColumnByKey("init").setHeader("Hora de inicio");
			grid.getColumnByKey("establishment").setHeader("Establecimiento");
			
	        grid.getColumns()
            .forEach(citeColumn -> citeColumn.setAutoWidth(true)); //Columnas autoajustables

	        HorizontalLayout mainContent = new HorizontalLayout(grid);
	        mainContent.setSizeFull();
	        grid.setSizeFull();
			grid.asSingleSelect().addValueChangeListener(event ->{
				if(grid.asSingleSelect().getValue()!=null)	form.setCite(grid.asSingleSelect().getValue());
				else form.setCite(new Cite(0, "00:00",  new Establishment()));

			});
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

	        form.setCite(new Cite(0, "00:00",  new Establishment()));


	    }
	    
	    public void updateList() {
	            grid.setItems(service.listar());
	    }
	    
}

