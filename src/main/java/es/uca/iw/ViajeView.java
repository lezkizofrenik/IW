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

import es.uca.iw.backend.Viaje;

import es.uca.iw.backend.ViajeService;
import es.uca.iw.backend.CityService;
import es.uca.iw.backend.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "ViajeView", layout = MainView.class) 
@Secured({ "Admin"})
@PageTitle("Gestión de escalas")
public class ViajeView extends AbstractView {

    private static final long serialVersionUID = 1L;
    private ViajeForm form;
    private Grid<Viaje> grid = new Grid<>(Viaje.class);
    private TextField filterText = new TextField();
    private ViajeService service;
    private boolean formStatus = false;
    @Autowired
    ViajeView(ViajeService service, CityService serviceCity, UsuarioService serviceClient) {
        this.form = new ViajeForm(this, service, serviceCity);
        this.service = service;
        H1 titulo = new H1("Gestión de Viajes");
        
        Button addServicioBtn = new Button("Añade un Viaje");

        filterText.setPlaceholder("Filtrar por nombre"); // poner el campo
        filterText.setClearButtonVisible(true); // poner la cruz para borrar
        filterText.setValueChangeMode(ValueChangeMode.EAGER); // que se hagan los cambios cuando se escriba
        filterText.addValueChangeListener(event -> {
            if (service.listarViaje() != null)
                updateList();
            else {
                filterText.clear();
                Notification.show("No hay barco disponibles", 3000, Notification.Position.BOTTOM_START);
            }

        });
        updateList();
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addServicioBtn);
        grid.setColumns("name","route");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumnByKey("route").setHeader("Ruta");
        grid.getColumns()
        .forEach(viajeColumn -> viajeColumn.setAutoWidth(true)); //Columnas autoajustables

        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(titulo, toolbar, mainContent);

        setSizeFull();

        updateList();

        form.setViaje(new Viaje(0, "", ""));
        grid.asSingleSelect().addValueChangeListener(event ->{
            if(grid.asSingleSelect().getValue()!=null)	form.setViaje(grid.asSingleSelect().getValue());
            else  form.setViaje(new Viaje(0, "", ""));

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
            grid.setItems(service.listarViaje());
        else
            grid.setItems(service.listarViajePorName(filterText.getValue()));
        
    }
    
}