package es.uca.iw;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.Usuario;
import org.springframework.security.access.annotation.Secured;

@Route(value = "MapUserView", layout = MainView.class)
@Secured({"User"})
@PageTitle("Mapa")
public class MapUserView extends VerticalLayout {

    public MapUserView(){
        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {
            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);

            String src = "images/maps/" + u.getShip().getId() + ".png";
            Image map = new Image(src, "Image not found");
            add(map);
        }
    }
}
