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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Route(value = "HikeUserView", layout = MainView.class)
@Secured({ "User" })
@PageTitle("Establishment")
public class HikeUserView extends VerticalLayout {
    private HikeService hikeService;
    private ServicioService servicioService;

    public HikeUserView(HikeService hikeService, ServicioService servicioService){
        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
        this.servicioService = servicioService;
        this.hikeService = hikeService;

        H1 titulo = new H1("Excursiones");
        VerticalLayout v2 = new VerticalLayout();

        VerticalLayout v = new VerticalLayout();
        add(v2);
        setClassName("layout");
        v.setAlignItems(Alignment.CENTER);
        Set<String> ciudades = u.getShip().getViaje().rutaToSetString();
        List<Hike>  lista = new ArrayList<>();
        Iterator<String> it = ciudades.iterator();
        while(it.hasNext()){
            lista.addAll(hikeService.searchByCity_Name(it.next()));
        }


        for(int i = 0; i < lista.size(); i++ ){
            VerticalLayout vc = new VerticalLayout();
            PrimaryLabel p = new PrimaryLabel(lista.get(i).getName());
            SecondaryLabel s = new SecondaryLabel(lista.get(i).getDescription());
            SecondaryLabel s2 = new SecondaryLabel("Escala: " + lista.get(i).getCity().getName());
            SecondaryLabel s3 = new SecondaryLabel("Hora: " + lista.get(i).getInit());
            SecondaryLabel s4 = new SecondaryLabel("Duración: " + lista.get(i).getTime());
            SecondaryLabel s5 = new SecondaryLabel("Precio: " + lista.get(i).getPrice());

            vc.add(p, s, s2, s3, s4, s5);
            vc.setHeight("40%");
            Card card = new Card(vc);
            card.setWidth("80%");
            card.setHeight("40%");
            v.add(card );

        }
        v2.add(titulo, v);
    }
}
