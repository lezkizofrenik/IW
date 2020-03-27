package es.uca.iw;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import sun.awt.image.ImageWatched;

@Route(value = "About", layout = MainView.class)
@PageTitle("Bienvenido a Costa Vespertino")
public class About extends AbstractView {

    private static final long serialVersionUID = 1L;
    @Autowired
    About() {
        VerticalLayout v = new VerticalLayout();
        H1 titulo = new H1("Costa-Vespertino Cruceros");
        Text text = new Text("Costa Vespertino es una linea de cruceros con sede en Cádiz. Se fundó en el año 2019 para proveer durante todo el año rutas por el Mediterráneo y un amplio abanico de itinerarios estacionales por el Norte de Europa, el océano Atlántico, el Caribe, Cuba y las Antillas, Sudamérica, Sudáfrica, Dubái, Abu Dhabi y Sir Bani Yas.");
        Anchor anchor = new Anchor("costavespertino.info@gmail.com", "Contacto");
        v.add(titulo, text, anchor);

        add(v);
    }
    
}