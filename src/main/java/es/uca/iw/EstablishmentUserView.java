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

@Route(value = "EstablishmentUserView", layout = MainView.class)
@Secured({ "User" })
@PageTitle("Establecimientos")
public class EstablishmentUserView extends VerticalLayout {
    private EstablishmentService establishmentService;
    private ServicioService servicioService;
    
    public EstablishmentUserView(EstablishmentService establishmentService, ServicioService servicioService){
        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
        this.servicioService = servicioService;
        this.establishmentService = establishmentService;
        
        H1 titulo = new H1("Establecimientos");
        VerticalLayout v2 = new VerticalLayout();

        VerticalLayout v = new VerticalLayout();
        add(v2);
        setClassName("layout");
        v.setAlignItems(Alignment.CENTER);
        List<Servicio> servicios = servicioService.buscarEstablishment(u.getShip());
        List<Establishment>  lista = new ArrayList<>();
        for(int i = 0; i < servicios.size(); i++){
            Establishment e = establishmentService.searchById(servicios.get(i).getIdServicio());
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
