package es.uca.iw;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.backend.*;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "PagoView", layout = MainView.class)
@PageTitle("Lista de pagos")
@Secured({ "Admin" })

public class PagoView extends AbstractView {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

	private Grid<Payment> grid = new Grid<>();
	private PaymentService service;

    @Autowired
    PagoView(PaymentService service) {
		this.service = service;

		H1 titulo = new H1("Mis facturas");
		VerticalLayout contenido = new VerticalLayout(titulo, grid);

		if(UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

			Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
			grid.setItems(service.listarPayment());

			grid.addColumn(Payment::getUsuario).setHeader("Nombre");
			grid.addColumn(Payment::getUsuarioId).setHeader("Id usuario");
			grid.addColumn(Payment::getIdServicio).setHeader("Id servicio");
			grid.addColumn(Payment::getTypeParsed).setHeader("Tipo");
			grid.addColumn(Payment::getTotal).setHeader("Importe");
			grid.addColumn(Payment::getDate).setHeader("Fecha");
			grid.getColumns()
					.forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true)); //Columnas autoajustables

			contenido.setSizeFull();
			add(contenido);
			setSizeFull();
		}

	}

}