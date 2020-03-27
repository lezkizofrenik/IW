package es.uca.iw;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

@Route(value = "RestaurantUserView", layout = MainView.class)
@Secured({ "User" })
@PageTitle("Restaurantes")
public class RestaurantUserView extends VerticalLayout {
    private RestaurantService restaurantService;
    private ServicioService servicioService;

    public RestaurantUserView(RestaurantService restaurantService, ServicioService servicioService){
        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
        this.servicioService = servicioService;
        this.restaurantService = restaurantService;

        H1 titulo = new H1("Restaurantes");
        VerticalLayout v2 = new VerticalLayout();

        VerticalLayout v = new VerticalLayout();
        add(v2);
        setClassName("layout");
        v.setAlignItems(Alignment.CENTER);
        List<Servicio> servicios = servicioService.buscarRestaurant(u.getShip());
        List<Restaurant>  lista = new ArrayList<>();
        for(int i = 0; i < servicios.size(); i++){
            Restaurant e = restaurantService.searchById(servicios.get(i).getIdServicio());
            if(e!=null) lista.add(e);
        }

        for(int i = 0; i < lista.size(); i++ ){
            VerticalLayout vc = new VerticalLayout();
            PrimaryLabel p = new PrimaryLabel(lista.get(i).getName());
            SecondaryLabel s = new SecondaryLabel(lista.get(i).getDescription());

            vc.add(p, s);
            vc.setHeight("40%");
            Card card = new Card(vc);
            card.setWidth("80%");
            card.setHeight("40%");
            v.add(card );

        }
        v2.add(titulo, v);
    }
}
