package es.uca.iw;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import es.uca.iw.backend.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "UsuarioView", layout = MainView.class)
@Secured({"User", "Admin"})
public class UsuarioView extends AbstractView {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Grid<Usuario> grid = new Grid<>(Usuario.class);
    private UsuarioService service;
    private UsuarioForm form;

    @Autowired
    UsuarioView(UsuarioService service, BankAccountService bas, ShipService sp, ReservationEstablishmentService reservationEstablishmentService, ReservationActivityService reservationActivityService,
                ReservationRestaurantService reservationRestaurantService, ReservationHikeService reservationHikeService) {
        this.form = new UsuarioForm(this, service, bas, sp, reservationEstablishmentService, reservationActivityService,
                reservationRestaurantService,reservationHikeService);
        this.service = service;
        H1 titulo = new H1("Gestión de usuarios");
        VerticalLayout contenido;
        grid.setColumns("username", "nombre", "apellido1", "apellido2", "telefono", "email", "role", "ship");
        grid.getColumnByKey("username").setHeader("Usuario");
        grid.getColumnByKey("nombre").setHeader("Nombre");
        grid.getColumnByKey("apellido1").setHeader("1er apellido");
        grid.getColumnByKey("apellido2").setHeader("2º apellido");
        grid.getColumnByKey("telefono").setHeader("Teléfono");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("role").setHeader("Rol");
        grid.getColumnByKey("ship").setHeader("Barco");



        grid.getColumns()
        .forEach(usuarioColumn -> usuarioColumn.setAutoWidth(true)); //Columnas autoajustables

        if(UI.getCurrent().getSession().getAttribute(Usuario.class) != null &&
        		UI.getCurrent().getSession().getAttribute(Usuario.class).getRole().equals("Admin")) {
        	Button addServicioBtn = new Button("Añade un usuario");
            addServicioBtn.addClickListener(e -> {
            	form.setLectura(false);
                form.setNew(true);
                form.setUsuario(null);
            });
            HorizontalLayout toolbar = new HorizontalLayout( addServicioBtn);

            HorizontalLayout mainContent = new HorizontalLayout(grid, form);
             
            updateList();
            grid.asSingleSelect().addValueChangeListener(event -> {

	            form.setLectura(true);
	            Usuario s = grid.asSingleSelect().getValue();
	            if(s != null)
	            	s = service.listarPorUsername(grid.asSingleSelect().getValue().getUsername());
	            form.setUsuario(s);


            });
            mainContent.setSizeFull();
            grid.setSizeFull();
            add(titulo, toolbar, mainContent);

        }else {
        	contenido = new VerticalLayout(titulo, form);
            add(contenido);
            contenido.setSizeFull();

        }
        setSizeFull();

    }

    void updateList() {
    	this.grid.setItems(service.listarUsuario());
    }
}