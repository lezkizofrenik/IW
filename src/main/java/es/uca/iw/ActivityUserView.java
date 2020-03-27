package es.uca.iw;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

@Route(value = "ActivityUserView", layout = MainView.class)
@Secured({ "User" })
@PageTitle("Actividades")
public class ActivityUserView extends VerticalLayout {
    private ActivityService activityService;
    private ServicioService servicioService;

    public ActivityUserView(ActivityService activityService, ServicioService servicioService){
        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
        this.servicioService = servicioService;
        this.activityService = activityService;

        H1 titulo = new H1("Actividades");
        VerticalLayout v2 = new VerticalLayout();

        VerticalLayout v = new VerticalLayout();
        add(v2);
        setClassName("layout");
        v.setAlignItems(Alignment.CENTER);
        List<Servicio> servicios = servicioService.buscarActivity(u.getShip());
        List<Activity>  lista = new ArrayList<>();
        for(int i = 0; i < servicios.size(); i++){
            Activity e = activityService.searchById(servicios.get(i).getIdServicio());
            if(e!=null) lista.add(e);
        }

        for(int i = 0; i < lista.size(); i++ ){
            TitleLabel p = new TitleLabel(lista.get(i).getName());
            SecondaryLabel s = new SecondaryLabel(lista.get(i).getDescription());

            SecondaryLabel s2 = new SecondaryLabel("Hora: " + lista.get(i).getTime());
            SecondaryLabel s3 = new SecondaryLabel("Duracion: " + lista.get(i).getTime());
            SecondaryLabel s4 = new SecondaryLabel("Días: " + lista.get(i).getDias());


            Card card = new Card(p, s, s2, s3, s4);
            card.setWidth("80%");
            card.setHeight("40%");
            v.add(card );

        }
        v2.add(titulo, v);
    }
}
