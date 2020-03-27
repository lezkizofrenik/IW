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
import es.uca.iw.backend.Ship;

import es.uca.iw.backend.ShipService;
import es.uca.iw.backend.UsuarioService;
import es.uca.iw.backend.Viaje;
import es.uca.iw.backend.ViajeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "ShipView", layout = MainView.class) 
@Secured({ "Admin"})
@PageTitle("Gestión de barcos")
public class ShipView extends AbstractView {

    private static final long serialVersionUID = 1L;
    private ShipForm form;
    private Grid<Ship> grid = new Grid<>(Ship.class);
    private TextField filterText = new TextField();
    private ShipService service;
    private boolean formStatus =false;
    @Autowired
    ShipView(ShipService service, CityService serviceCity, UsuarioService serviceClient,ViajeService viajeservice) {
        this.form = new ShipForm(this, service, serviceCity,viajeservice);
        this.service = service;
        H1 titulo = new H1("Gestión de barcos");
        
        Button addServicioBtn = new Button("Añade un barco");

        filterText.setPlaceholder("Filtrar por nombre"); // poner el campo
        filterText.setClearButtonVisible(true); // poner la cruz para borrar
        filterText.setValueChangeMode(ValueChangeMode.EAGER); // que se hagan los cambios cuando se escriba
        filterText.addValueChangeListener(event -> {
            if (service.listarShip() != null)
                updateList();
            else {
                filterText.clear();
                Notification.show("No hay barcos disponibles", 3000, Notification.Position.BOTTOM_START);
            }

        });
        updateList();
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addServicioBtn);
        grid.setColumns("name","viaje");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumnByKey("viaje").setHeader("Viaje");
        grid.getColumns()
        .forEach(shipColumn -> shipColumn.setAutoWidth(true)); //Columnas autoajustables

        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(titulo, toolbar, mainContent);

        setSizeFull();

        updateList();

        form.setShip(new Ship(0,"", new Viaje()));
        grid.asSingleSelect().addValueChangeListener(event ->{
            if(grid.asSingleSelect().getValue()!=null)	form.setShip(grid.asSingleSelect().getValue());
            else  form.setShip(new Ship(0,"", new Viaje()));

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
        if(filterText.isEmpty())
            grid.setItems(service.listarShip());
        else
            grid.setItems(service.listarShipPorName(filterText.getValue()));
        
    }
    
}