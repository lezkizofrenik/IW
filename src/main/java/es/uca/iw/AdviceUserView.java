package es.uca.iw;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.Advice;
import es.uca.iw.backend.AdviceService;
import es.uca.iw.backend.Usuario;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Route(value = "AdviceUserView", layout = MainView.class)
@Secured({ "User" })
@PageTitle("Consejos")
public class AdviceUserView extends VerticalLayout {
    private AdviceService adviceService;

    public AdviceUserView(AdviceService adviceService){
        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);

        this.adviceService = adviceService;
        H1 titulo = new H1("Consejos");
        VerticalLayout v2 = new VerticalLayout();

        VerticalLayout v = new VerticalLayout();
        add(v2);
        setClassName("layout");
        v.setAlignItems(Alignment.CENTER);
        List<Advice>  lista = adviceService.findByShip(u.getShip());
        for(int i = 0; i < lista.size(); i++ ){
            VerticalLayout vc = new VerticalLayout();
            SecondaryLabel s = new SecondaryLabel(lista.get(i).getDescription());
            vc.add(s);
            vc.setHeight("40%");
            Card card = new Card(vc);
            card.setWidth("80%");
            card.setHeight("40%");
            v.add(card );

        }
        v2.add(titulo, v);
    }
}
