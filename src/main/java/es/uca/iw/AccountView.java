package es.uca.iw;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.UI;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.Usuario;

@Route(value = "AccountView", layout = MainView.class)
@Secured({"User", "Admin", "Gerente"})
@PageTitle("Mi cuenta")
public class AccountView extends AbstractView {

    private static final long serialVersionUID = 1L;

    public AccountView() {

        Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);

        if(u != null) {
	        //Listado de datos personales
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

	        TitleLabel datos = new TitleLabel("Datos personales");
	        PrimaryLabel nombre = new PrimaryLabel("Nombre:");
	        SecondaryLabel nombreUser =  new SecondaryLabel(u.getNombre());

	        PrimaryLabel apellido = new PrimaryLabel("Apellidos:");
	        SecondaryLabel apellidoUser =  new SecondaryLabel(u.getApellido1() +
	         " " + u.getApellido2());

	        PrimaryLabel dni = new PrimaryLabel("DNI:");
	        SecondaryLabel dniUser =  new SecondaryLabel(u.getDni());

			PrimaryLabel telefono = new PrimaryLabel("Teléfono:");
	        SecondaryLabel telefonoUser =  new SecondaryLabel(u.getTelefono());

			v3.add(nombre, apellido, dni, telefono);
			v4.add(nombreUser, apellidoUser, dniUser, telefonoUser);
			v4.setClassName("accountFont");
			v2.add(datos);
			Card data = new Card(v);
			data.setAlignItems(Alignment.CENTER);

	        //Listado de cuenta

	        TitleLabel cuenta = new TitleLabel("Cuenta");
	        PrimaryLabel user = new PrimaryLabel("Usuario");
	        SecondaryLabel usuarioUser = new SecondaryLabel(u.getUsername());


	        PrimaryLabel email = new PrimaryLabel ("E-mail");
	        SecondaryLabel emailUser = new SecondaryLabel(u.getEmail());

			VerticalLayout v6 = new VerticalLayout();
			v6.setClassName("layout");
			VerticalLayout v7 = new VerticalLayout();
			VerticalLayout v8 = new VerticalLayout();
			VerticalLayout v9 = new VerticalLayout();
			HorizontalLayout v10 = new HorizontalLayout();
			v10.setWidth("80%");
			v9.add(cuenta);
			v7.add(user, email);
			v8.add(usuarioUser, emailUser);
			v8.setClassName("accountFont");

			v10.add(v7, v8);
			v6.add(v9, v10);
			Card data2 = new Card(v6);
			data2.setAlignItems(Alignment.CENTER);

			data.setWidth("80%");
			data2.setWidth("80%");

			VerticalLayout vv = new VerticalLayout();
			vv.add(data, data2);
			if (SecurityUtils.hasRole("User")) {
				PrimaryLabel money = new PrimaryLabel("Saldo");
				SecondaryLabel usuarioMoney = new SecondaryLabel(String.format("%.2f", u.getBankAccount().getMoney()) + " €");
				VerticalLayout v11 = new VerticalLayout();
				v11.add( usuarioMoney);
				v11.setWidth("accountFont");
				Card data3 = new Card(money, v11);
				data3.setWidth("80%");
				vv.add(data3);
			}
			vv.setClassName("layout");
			add(vv);
	        
        }

    }

}
