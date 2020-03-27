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

import es.uca.iw.backend.City;
import es.uca.iw.backend.CityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "CityView", layout = MainView.class)
@PageTitle("Gestión de las ciudades")
@Secured({ "Admin" })
public class CityView extends AbstractView {

    private static final long serialVersionUID = 1L;
    private CityForm form;
    private Grid<City> grid = new Grid<>(City.class);
    private TextField filterText = new TextField();
    private CityService service;
    private boolean formStatus = false;
    @Autowired
    CityView(CityService service) {
        this.form = new CityForm(this, service);
        this.service = service;
        H1 titulo = new H1("Gestión de las ciudades");
        
        Button addServicioBtn = new Button("Añade una ciudad");

        filterText.setPlaceholder("Filtrar por nombre"); // poner el campo
        filterText.setClearButtonVisible(true); // poner la cruz para borrar
        filterText.setValueChangeMode(ValueChangeMode.EAGER); // que se hagan los cambios cuando se escriba
        filterText.addValueChangeListener(event -> {
            if (service.listarCity() != null)
                updateList();
            else {
                filterText.clear();
                Notification.show("No hay barco disponibles", 3000, Notification.Position.BOTTOM_START);
            }

        });
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addServicioBtn);
        grid.setColumns("name");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumns()
        .forEach(cityColumn -> cityColumn.setAutoWidth(true)); //Columnas autoajustables

        HorizontalLayout mainContent = new HorizontalLayout(grid); // metemos en un objeto el grid y formulario
        mainContent.setSizeFull();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(event ->{
            if(grid.asSingleSelect().getValue()!=null)	form.setCity(grid.asSingleSelect().getValue());
            else form.setCity(new City( 0, "")); // instancia un nuevo


        });
        form.setCity(new City( 0, ""));

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


    }
    
    public void updateList() {
        if (filterText.isEmpty())
            grid.setItems(service.listarCity());
        else
            grid.setItems(service.buscarCityporNombre(filterText.getValue()));
    }
    
}