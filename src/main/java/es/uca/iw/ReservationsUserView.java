package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;
import com.vaadin.flow.component.dialog.Dialog;

import org.springframework.security.access.annotation.Secured;


@Route(value = "ReservationsUser", layout = MainView.class)
@PageTitle("Reservas de los usuarios")
@Secured({"User"})
public class ReservationsUserView extends VerticalLayout{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Grid<ReservationActivity> gridActivity = new Grid<>();
    private Grid<ReservationRestaurant> gridRestaurant = new Grid<>();
    private Grid<ReservationHike> gridHikes = new Grid<>();
    private Grid<ReservationEstablishment> gridEstablishment = new Grid<>();
    private ActivityService activityService;
    private HikeService hikeService;
    private ReservationActivityService reservationActivityService;
    private ReservationHikeService reservationHikeService;
    private ReservationRestaurantService reservationRestaurantService;
    private ReservationEstablishmentService reservationEstablishmentService;
    private PaymentService paymentService;
    private BankAccountService bankAccountService;
    private TableService tableService;
    private CiteService citeService;



    public ReservationsUserView(CiteService citeService, TableService tableService, BankAccountService bankAccountService, PaymentService paymentService, ReservationRestaurantService reservationRestaurantService, ReservationEstablishmentService reservationEstablishmentService,ActivityService activityService, ReservationActivityService reservationActivityService, CiteService establishmentService, HikeService hikeService, ReservationHikeService reservationHikeService){
        this.activityService = activityService;
        this.reservationActivityService = reservationActivityService;
        this.reservationEstablishmentService = reservationEstablishmentService;
        this.hikeService = hikeService;
        this.reservationHikeService = reservationHikeService;
        this.reservationRestaurantService = reservationRestaurantService;
        this.paymentService = paymentService;
        this.bankAccountService = bankAccountService;
        this.tableService = tableService;
        this.citeService = citeService;


        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
            VerticalLayout v= new VerticalLayout();
            v.setWidth("90%");


            Button borrarActivity = new Button("Cancelar reserva");
            borrarActivity.addClickListener(buttonHikeEvent ->{
            	if(gridActivity.asSingleSelect().getValue()!=null){
                    Dialog dialog = new Dialog();

                    Button cancelarReservaActivity = new Button("Confirmar");
                    cancelarReservaActivity.addClickListener(event -> {
                        borrarActivityFunction(gridActivity.asSingleSelect().getValue(), u);
                        updateActivityList(u);
                        dialog.close();
                        Notification.show("Reserva cancelada", 3000, Notification.Position.BOTTOM_START);
                    });
                    Button cancelar = new Button("Cancelar");
                    cancelar.addClickListener(cancelarEvent-> dialog.close());
                    dialog.add(cancelarReservaActivity, cancelar);
            		dialog.open();
            	}else 
            		Notification.show("Debe seleccionar una reserva", 3000, Notification.Position.BOTTOM_START);
            });


            
            Button borrarHike = new Button("Cancelar reserva");
            borrarHike.addClickListener(buttonHikeEvent ->{
            	if(gridHikes.asSingleSelect().getValue()!=null){
                    Dialog dialog = new Dialog();
                    Button cancelarReservaHike =  new Button("Confirmar");
                    cancelarReservaHike.addClickListener(event -> {
                        borrarHikeFunction(gridHikes.asSingleSelect().getValue(), u);
                        updateHikesList(u);
                        dialog.close();
                        Notification.show("Reserva cancelada", 3000, Notification.Position.BOTTOM_START);
                    });
                    Button cancelar = new Button("Cancelar");
                    cancelar.addClickListener(cancelarEvent-> dialog.close());
                    dialog.add(cancelarReservaHike, cancelar);
            		dialog.open();
            	}else
            		Notification.show("Debe seleccionar una reserva", 3000, Notification.Position.BOTTOM_START);
            });


            
            Button borrarRestaurant = new Button("Cancelar reserva");
            borrarRestaurant.addClickListener(buttonHikeEvent ->{if(gridRestaurant.asSingleSelect().getValue()!=null){
                Dialog dialog = new Dialog();

                Button cancelarReservaRestaurant =  new Button("Confirmar");
                cancelarReservaRestaurant.addClickListener(event ->{
                    borrarReservaFunction(gridRestaurant.asSingleSelect().getValue(), u);
                    updateReservationList(u);
                    dialog.close();
                    Notification.show("Reserva cancelada", 3000, Notification.Position.BOTTOM_START);
                });
                Button cancelar = new Button("Cancelar");
                cancelar.addClickListener(cancelarEvent-> dialog.close());
                dialog.add(cancelarReservaRestaurant, cancelar);
                dialog.open();
            }else
            	Notification.show("Debe seleccionar una reserva", 3000, Notification.Position.BOTTOM_START);
            });


            
            Button borrarEstablishment = new Button("Cancelar reserva");
            borrarEstablishment.addClickListener(buttonEstablishment ->{if(gridEstablishment.asSingleSelect().getValue()!=null){
                Dialog dialog = new Dialog();

                Button cancelarReservaEstablishment = new Button("Confirmar");
                cancelarReservaEstablishment.addClickListener(event -> {
                    borrarEstablishmentFunction(gridEstablishment.asSingleSelect().getValue(), u);
                    updateEstablishmentList(u);
                    dialog.close();
                    Notification.show("Reserva cancelada", 3000, Notification.Position.BOTTOM_START);
                });
                Button cancelar = new Button("Cancelar");
                cancelar.addClickListener(cancelarEvent-> dialog.close());
                dialog.add(cancelarReservaEstablishment, cancelar);
                dialog.open();

            }else
            	Notification.show("Debe seleccionar una reserva", 3000, Notification.Position.BOTTOM_START);
            });

            gridActivity.addColumn(ReservationActivity::getActivityName).setHeader("Nombre");
            gridActivity.addColumn(ReservationActivity::getnPeople).setHeader("Personas");
            gridActivity.addColumn(ReservationActivity::getDateWanted).setHeader("Dia reservado");
            gridActivity.addColumn(ReservationActivity::getDateReservation).setHeader("Fecha de la reserva");
            gridActivity.getColumns()
                    .forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true)); //Columnas autoajustables

            gridEstablishment.addColumn(ReservationEstablishment::getEstablishmentName).setHeader("Nombre");
            gridEstablishment.addColumn(ReservationEstablishment::getTurnCite).setHeader("Turno");
            gridEstablishment.addColumn(ReservationEstablishment::getDateWanted).setHeader("Día reservado");
            gridEstablishment.addColumn(ReservationEstablishment::getDate).setHeader("Fecha de la reserva");
            gridEstablishment.getColumns()
                    .forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true));
            //Añadir numero de personas de la reserva
            gridHikes.addColumn(ReservationHike::getNameHike).setHeader("Nombre");
            gridHikes.addColumn(ReservationHike::getCityHike).setHeader("Escala");
            gridHikes.addColumn(ReservationHike::getnPeople).setHeader("Personas");
            gridHikes.addColumn(ReservationHike::getDate).setHeader("Fecha de la reserva");
            gridHikes.getColumns()
                    .forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true));

            gridRestaurant.addColumn(ReservationRestaurant::getRestaurantName).setHeader("Nombre");
            gridRestaurant.addColumn(ReservationRestaurant::getTurnParsed).setHeader("Turno");
            gridRestaurant.addColumn(ReservationRestaurant::getPeopleReservation).setHeader("Personas");
            gridRestaurant.addColumn(ReservationRestaurant::getDateWanted).setHeader("Dia reservado");
            gridRestaurant.addColumn(ReservationRestaurant::getDateReservation).setHeader("Fecha de la Reserva");
            gridRestaurant.getColumns()
                    .forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true));

            updateActivityList(u);
            updateReservationList(u);
            updateEstablishmentList(u);
            updateHikesList(u);
            
            H1 titulo = new H1("Reservas realizadas");
            H3 actividades = new H3("Actividades");
            H3 excursiones = new H3("Excursiones");
            H3 restaurantes = new H3("Restaurantes");
            H3 servicios = new H3("Establecimientos");

            v.add(titulo, actividades, gridActivity, borrarActivity, excursiones, gridHikes, borrarHike, restaurantes, gridRestaurant, borrarRestaurant, servicios, gridEstablishment, borrarEstablishment);
            v.setAlignSelf(Alignment.CENTER);
            setAlignItems(Alignment.CENTER);
            add(v);

        }else
            add(new Text("Usted no está autorizado para ver este contenido"));
    }

    private void borrarEstablishmentFunction(ReservationEstablishment reservationEstablishment, Usuario u) {
        if (reservationEstablishment != null) {
            reservationEstablishmentService.borrarReservation(reservationEstablishment);

        }
    }

    private void borrarReservaFunction(ReservationRestaurant reservationRestaurant, Usuario u) {
        if(reservationRestaurant!= null){
            if(!reservationRestaurant.getTurn()){
                String message = "\nRestaurant: " + reservationRestaurant.getRestaurant().getName() +
                        "\nTurn: Lunch" + "\n For: " + reservationRestaurant.getTableRestaurant().getCapacity() + " people" +
                        "\nDay: " + reservationRestaurant.getDateWanted() +   "\nBooking made on: " + reservationRestaurant.getDateReservation();;
                EmailService.sendSimpleMessage(
                        u.getEmail(), "Your booking cancelation at Costa Vespertino", message);
            }
            else{
                String message = "We're so sorry that you had to cancel your booking. There are the details:\n" +
                        "\nRestaurant: " + reservationRestaurant.getRestaurant().getName() +
                        "\nTurn: Dinner" + "\n For: " + reservationRestaurant.getTableRestaurant().getCapacity() + " people" +
                        "\nDay: " + reservationRestaurant.getDateWanted() +   "\nBooking made on: " + reservationRestaurant.getDateReservation();
                EmailService.sendSimpleMessage(
                        u.getEmail(), "Your booking cancelation at Costa Vespertino", message);
            }
            reservationRestaurantService.borrarReservation(reservationRestaurant);
        }
    }

    private void borrarHikeFunction(ReservationHike reservationHike, Usuario u) {
        if(reservationHike!= null){
            String message = "We're so sorry that you had to cancel your booking. There are the details:\n" +
                    "\nHike: " + reservationHike.getHike().getName() + "\nCity: " + reservationHike.getHike().getCity().getName() + "\n For: " +
                    reservationHike.getnPeople() + " people" +  "\nBooking made on: " + reservationHike.getDate();
            EmailService.sendSimpleMessage(
                    u.getEmail(), "Your booking cancelation at Costa Vespertino", message);

            reservationHike.getHike().incrementCapacity(reservationHike.getnPeople());
            hikeService.guardarHike(reservationHike.getHike());
            u.getBankAccount().setRefund(reservationHike.getPayment().getTotal());
            bankAccountService.guardarBankAccount(u.getBankAccount());
            reservationHikeService.borrarReservation(gridHikes.asSingleSelect().getValue());
            paymentService.borrarPayment(reservationHike.getPayment());

        }

    }


    private void borrarActivityFunction(ReservationActivity reservationActivity, Usuario u) {
        if (reservationActivity != null) {
            String message = "\nActivity: " + reservationActivity.getActivity().getName() + "\nHour: " + reservationActivity.getActivity().getInit() + "\n For: " +
                    reservationActivity.getnPeople() + " people" + "Activity date: " + reservationActivity.getDateWanted() +
                    "\nBooking made on: " + reservationActivity.getDateReservation();
            EmailService.sendSimpleMessage(
                    u.getEmail(), "Your booking cancelation at Costa Vespertino", message);
            //reservationActivity.getActivity().incrementCapacity(reservationActivity.getnPeople());
            //activityService.guardarActivity(reservationActivity.getActivity());
            u.getBankAccount().setRefund(reservationActivity.getPayment().getTotal());
            bankAccountService.guardarBankAccount(u.getBankAccount());
            reservationActivityService.borrarReservation(reservationActivity);
            paymentService.borrarPayment(reservationActivity.getPayment());


        }
    }

    private void updateHikesList(Usuario u) {
        gridHikes.setItems(reservationHikeService.buscarPorUsuario(u));

    }

    private void updateEstablishmentList(Usuario u) {
        gridEstablishment.setItems(reservationEstablishmentService.buscarPorUsuario(u));

    }

    private void updateReservationList(Usuario u) {
        gridRestaurant.setItems(reservationRestaurantService.buscarPorUsuario(u));

    }

    private void updateActivityList(Usuario u) {
        gridActivity.setItems(reservationActivityService.buscarPorUsuario(u));
    }


}

