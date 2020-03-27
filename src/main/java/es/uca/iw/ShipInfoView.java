package es.uca.iw;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.Usuario;
import org.springframework.security.access.annotation.Secured;

@Route(value = "ShipInfoView", layout = MainView.class)
@Secured({"User"})
@PageTitle("El barco")
public class ShipInfoView extends VerticalLayout {
    public ShipInfoView(){
        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
        VerticalLayout v = new VerticalLayout();
        v.setClassName("layout");
        VerticalLayout v2 = new VerticalLayout();
        VerticalLayout v3 = new VerticalLayout();
        VerticalLayout v4 = new VerticalLayout();
        HorizontalLayout v5 = new HorizontalLayout();
        v5.setWidth("80%");

        v5.add(v3,v4);
        v.add(v2, v5);
        setSizeFull();

        TitleLabel datos = new TitleLabel("El barco");
        PrimaryLabel nombre = new PrimaryLabel("Nombre: ");
        SecondaryLabel nombreShip =  new SecondaryLabel(u.getShip().getName());

        PrimaryLabel ruta = new PrimaryLabel("Ruta: ");
        SecondaryLabel rutaShip =  new SecondaryLabel(u.getShip().getViaje().getRoute());



        v3.add(nombre, ruta);
        v4.add(nombreShip,rutaShip);
        v4.setClassName("accountFont");
        v2.add(datos);
        Card data = new Card(v);
        data.setWidth("80%");
        data.setAlignItems(Alignment.CENTER);

        VerticalLayout vv = new VerticalLayout();
        vv.add(data);
        vv.setClassName("layout");
        add(vv);
    }
}
