package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;
import org.springframework.security.access.annotation.Secured;

@Route(value = "ServicioView", layout = MainView.class)
@PageTitle("Gestión de los servicios")
@Secured({ "Admin" })
public class ServicioView extends AbstractView {

    private static final long serialVersionUID = 1L;
    private Grid<Servicio> grid = new Grid<>();
    private ComboBox<Ship> comboBox =
            new ComboBox<>("Barco");

    private ServicioService servicioService;

    public ServicioView(ShipService shipService, ServicioService servicioService){
        this.servicioService = servicioService;

        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

            VerticalLayout v = new VerticalLayout();
            v.setWidth("90%");
            v.setAlignSelf(Alignment.CENTER);
            setAlignItems(Alignment.CENTER);
            
            if(!shipService.listarShip().isEmpty()){
            	
                comboBox.setItems(shipService.listarShip());
                comboBox.addValueChangeListener(event -> updateList());
                H1 titulo = new H1("Gestión de los servicios");
                v.setWidth("90%");

                grid.addColumn(Servicio::getTypeParsed).setHeader("Tipo");
                grid.addColumn(Servicio::getIdServicio).setHeader("Id");

                v.add(titulo,comboBox,grid);
                
            }else
                v.add(new Text("No hay barcos que mostrar"));
            
            add(v);
        }else
            add(new Text("No está autorizado para ver este contenido"));
        
    }

    public void updateList(){
        if(comboBox.getValue()!= null)
        	grid.setItems(servicioService.buscarPorShip(comboBox.getValue()));
    }
}
