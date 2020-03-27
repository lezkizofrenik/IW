package es.uca.iw;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


@Route("mainmenu")
public class MainView extends AppLayout {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MainView() {
        final VerticalLayout navbar = new VerticalLayout();
        this.setDrawerOpened(true);
		Image logo = new Image("https://www.pngrepo.com/png/272288/170/ocean.png", "logo not found");
		logo.setClassName("img");
		navbar.add(logo);

		RouterLink r_home = new RouterLink(null, HomeView.class);
		r_home.add("Inicio");
		final Tab home = new Tab();
		navbar.setClassName("bar");
		home.add(r_home);
		navbar.add(home);
		navbar.setSizeUndefined();
        navbar.setHeightFull();

        if(SecurityUtils.hasRole("User") || SecurityUtils.hasRole("Gerente") || SecurityUtils.hasRole("Admin")) {
			if (SecurityUtils.hasRole("User")) {


				Accordion infos = new Accordion();
				infos.close();
				RouterLink r44 = new RouterLink(null, HikeUserView.class);
				r44.add("Excursiones");
				final Tab t44 = new Tab();
				t44.add(r44);

				RouterLink r45 = new RouterLink(null, ActivityUserView.class);
				r45.add("Actividades");
				final Tab t45 = new Tab();
				t45.add(r45);

				RouterLink r46 = new RouterLink(null, EstablishmentUserView.class);
				r46.add("Establecimientos");
				final Tab t46 = new Tab();
				t46.add(r46);

				RouterLink r47 = new RouterLink(null, RestaurantUserView.class);
				r47.add("Restaurantes");
				final Tab t47 = new Tab();
				t47.add(r47);

				VerticalLayout vinfos = new VerticalLayout();
				vinfos.add(t44, t45, t46, t47);

				infos.add("Información", vinfos);

				Accordion info = new Accordion();
				info.close();
				RouterLink r41 = new RouterLink(null, AdviceUserView.class);
				r41.add("Consejos");
				final Tab t41 = new Tab();
				t41.add(r41);

				RouterLink r42 = new RouterLink(null, MapUserView.class);
				r42.add("Mapa");
				final Tab t42 = new Tab();
				t42.add(r42);

				RouterLink r43 = new RouterLink(null, ShipInfoView.class);
				r43.add("Información");
				final Tab t43 = new Tab();
				t43.add(r43);

				VerticalLayout vinfo = new VerticalLayout();
				vinfo.add(t41, t42, t43);

				info.add("El barco", vinfo);

				Accordion cuenta = new Accordion();
				RouterLink r40 = new RouterLink(null, PagoUserView.class);
				r40.add("Facturas");
				final Tab t40 = new Tab();
				t40.add(r40);

				RouterLink r3 = new RouterLink(null, AccountView.class);
				r3.add("Mis datos");
				final Tab t3 = new Tab();
				t3.add(r3);
				VerticalLayout vcuenta = new VerticalLayout();
				vcuenta.add(t3, t40);
				cuenta.add("Mi cuenta", vcuenta);

				Accordion reservas = new Accordion();
				RouterLink r4 = new RouterLink(null, ReservationHikeView.class);
				r4.add("Excursiones");
				final Tab t4 = new Tab();
				t4.add(r4);
				RouterLink r5 = new RouterLink(null, ReservationActivityView.class);
				r5.add("Actividades");
				final Tab t5 = new Tab();
				t5.add(r5);
				RouterLink r6 = new RouterLink(null, ReservationEstablishmentView.class);
				r6.add("Establecimientos");
				final Tab t6 = new Tab();
				t6.add(r6);
				RouterLink r7 = new RouterLink(null, ReservationRestaurantView.class);
				r7.add("Restaurantes");
				final Tab t7 = new Tab();
				t7.add(r7);
				RouterLink r8 = new RouterLink(null, ReservationsUserView.class);
				r8.add("Mis reservas");
				final Tab t8 = new Tab();
				t8.add(r8);
				VerticalLayout vreservas = new VerticalLayout();
				vreservas.add(t4, t5, t6, t7, t8);
				reservas.add("Reservas", vreservas);
				cuenta.close();
				reservas.close();
				navbar.add(info, infos, reservas, cuenta);
			}
			if (SecurityUtils.hasRole("Admin")) {
				RouterLink r1 = new RouterLink(null, ServicioView.class);
				r1.add("Servicios");
				final Tab t1 = new Tab();
				t1.add(r1);
				RouterLink r2 = new RouterLink(null, ShipView.class);
				r2.add("Barcos");
				final Tab t2 = new Tab();
				t2.add(r2);
				RouterLink r14 = new RouterLink(null, ViajeView.class);
				r14.add("Viajes");
				final Tab t14 = new Tab();
				t14.add(r14);	
				RouterLink r5 = new RouterLink(null, ActivityView.class);
				r5.add("Actividades");
				final Tab t5 = new Tab();
				t5.add(r5);
				RouterLink r6 = new RouterLink(null, CityView.class);
				r6.add("Ciudades");
				final Tab t6 = new Tab();
				t6.add(r6);
				RouterLink r15 = new RouterLink(null, AdviceView.class);
				r15.add("Consejos");
				final Tab t15 = new Tab();
				t15.add(r15);
				
				Accordion a = new Accordion();
				VerticalLayout va = new VerticalLayout();
				va.add(t1, t2, t6,t14, t15);
				a.add("Información básica", va);

				RouterLink r3 = new RouterLink(null, UsuarioView.class);
				r3.add("Usuarios");
				final Tab t3 = new Tab();
				t3.add(r3);
				RouterLink r4 = new RouterLink(null, PagoView.class);
				r4.add("Pagos");
				final Tab t4 = new Tab();
				t4.add(r4);
				
				Accordion usuarios = new Accordion();
				VerticalLayout vusuarios = new VerticalLayout();
				vusuarios.add(t3, t4);
				usuarios.add("Usuarios", vusuarios);

				Accordion servicios = new Accordion();
				
				RouterLink r7 = new RouterLink(null, HikeView.class);
				r7.add("Excursiones");
				final Tab t7 = new Tab();
				t7.add(r7);
				RouterLink r10 = new RouterLink(null, CiteView.class);
				r10.add("Citas");
				final Tab t10 = new Tab();
				t10.add(r10);
				RouterLink r8 = new RouterLink(null, TableRestaurantView.class);
				r8.add("Mesas");
				final Tab t8 = new Tab();
				t8.add(r8);
				RouterLink r9 = new RouterLink(null, RestaurantView.class);
				r9.add("Restaurantes");
				final Tab t9 = new Tab();
				t9.add(r9);
				RouterLink r11 = new RouterLink(null, EstablishmentView.class);
				r11.add("Establecimientos");
				final Tab t11 = new Tab();
				t11.add(r11);

				VerticalLayout vservicios = new VerticalLayout();
				vservicios.add(t7, t10, t5, t8, t9, t11);
				servicios.add("Servicios", vservicios);

				RouterLink r_account = new RouterLink(null, AccountView.class);
				r_account.add("Mi cuenta");
				final Tab t12 = new Tab();
				t12.add(r_account);

				servicios.close();
				usuarios.close();
				a.close();
				navbar.add(a, usuarios, servicios, t12);

			}
			if (SecurityUtils.hasRole("Gerente")) {
				RouterLink r1 = new RouterLink(null, EstadisticaView.class);
				r1.add("Estadisticas");
				final Tab t1 = new Tab();
				t1.add(r1);
				navbar.add(t1);

				RouterLink r_account = new RouterLink(null, AccountView.class);
				r_account.add("Mi cuenta");
				final Tab t3 = new Tab();
				t3.add(r_account);
				navbar.add(t3);
			}


			RouterLink r_about = new RouterLink(null, About.class);
			r_about.add("Sobre nosotros");
			final Tab t4 = new Tab();
			t4.add(r_about);
			navbar.add(t4);

			RouterLink r_logout = new RouterLink(null, logOut.class);
			r_logout.add("Cerrar sesión");
			final Tab t2 = new Tab();
			t2.add(r_logout);
			navbar.add(t2);
			DrawerToggle drawerToggle = new DrawerToggle();
			drawerToggle.setClassName("drawer");
			addToNavbar(drawerToggle, new Span("Costa-Vespertino Cruceros"));
			addToDrawer(navbar);
		}
    }
}