package es.uca.iw;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uca.iw.backend.Usuario;

@Route("logOut")
public class logOut extends AppLayout{

	private static final long serialVersionUID = 1L;
	
	public logOut() {
		UI.getCurrent().getSession().close();
		UI.getCurrent().getSession().setAttribute(Usuario.class, null);
		VaadinSession.getCurrent().getSession().invalidate();
		UI.getCurrent().navigate("");

	}
}
