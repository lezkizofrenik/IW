package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.security.access.annotation.Secured;

@Route(value = "ReservationActivity", layout = MainView.class)
@PageTitle("Reserva de las actividades")
@Secured({"User"})
public class ReservationActivityView extends AbstractView {

    private static final long serialVersionUID = 1L;
    private Grid<Activity> grid = new Grid<>(Activity.class);
    private ActivityService service;
    private ServicioService servicioService;
    private PaymentService pagoService;
    private ReservationActivityService reservationActivityService;
    private NumberField numberField = new NumberField();
    private DatePicker date = new DatePicker("Fecha");


    public ReservationActivityView(BankAccountService bankAccountService, ReservationActivityService reservationActivityService,
                                   ActivityService service, PaymentService pagoService,
                                   ServicioService servicioService) {

        this.servicioService = servicioService;
        this.service = service;
        this.reservationActivityService = reservationActivityService;
        this.pagoService = pagoService;
        date.setMin(LocalDate.now());                                                                                                // autoajustables
        date.setValue(LocalDate.now());
        numberField.setValue(1d);
        numberField.setHasControls(true);
        numberField.setMin(1);
        numberField.setEnabled(false);
        //setClassName("login");
        //setSizeFull();
        VerticalLayout v = new VerticalLayout();
        v.setWidth("90%");
        v.setAlignSelf(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);


         if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);

            grid.setColumns("name", "description", "init", "price");
            grid.getColumnByKey("name").setHeader("Nombre");
            grid.getColumnByKey("description").setHeader("Descripción");
            grid.getColumnByKey("init").setHeader("Hora de inicio");
            grid.getColumnByKey("price").setHeader("Precio");
             grid.getColumns()
            .forEach(activityColumn -> activityColumn.setAutoWidth(true)); //Columnas autoajustables
            date.addValueChangeListener(date ->{
                grid.setItems(updateList(u));
                numberField.setValue(1d);
            } );
            H1 titulo = new H1("Reservar actividades");


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                grid.asSingleSelect().addValueChangeListener(event->{
                    if(grid.asSingleSelect().getValue()!= null) {
                        numberField.setEnabled(true);
                        numberField.setValue(1d);
                        int nReservas = reservationActivityService.actividadesReservadas(grid.asSingleSelect().getValue(), formatter.format(date.getValue()));
                        int max = grid.asSingleSelect().getValue().getCapacity() - nReservas;
                        numberField.setMax(max);
                    }
                    else{
                        numberField.setEnabled(false);
                    }
                });
                grid.setItems(updateList(u));

                Button payButton = new Button("Reservar", event -> {
                    //Set<Activity> activities = grid.asMultiSelect().getSelectedItems();

                    Activity activity = grid.asSingleSelect().getValue();
                    if(date.getValue().toString().isEmpty())
                        Notification.show("Debe introducir la fecha de la reserva", 3000,
                                Notification.Position.BOTTOM_START);
                    else if (activity == null)
                        Notification.show("Seleccione una actividad");

                    else{

                        Dialog dialog = new Dialog();

                        Button confirmButton = new Button("Pagar", event2 ->
                           reservar(activity, u, pagoService, bankAccountService, dialog));

                        confirmButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                        Button cancelButton = new Button("Cancelar", event3 -> {
                            dialog.close();
                        });
                        dialog.add(confirmButton, cancelButton);
                        dialog.open();

                    }
                });
                HorizontalLayout h = new HorizontalLayout();
                h.add(numberField, payButton);
                v.add(titulo, date, grid, h);
            

        } else 
             v.add(new Text("Usted no está autorizado para ver este contenido"));
         
        add(v);
    }

    public void reservar(Activity activity, Usuario u, PaymentService pagoService, BankAccountService bankAccountService, Dialog dialog){
        float total = (activity.getPrice()*numberField.getValue().intValue());
        if (u.getBankAccount().getMoney() >= total ) {
            Date date2 = new Date();
            Payment payment = new Payment(total, u, activity.getId(), 2, date2);
            pagoService.guardarPayment(payment);
            u.getBankAccount().payment(total);
            bankAccountService.guardarBankAccount(u.getBankAccount());
            ReservationActivity reserva = new ReservationActivity(
                    activity,
                    u,
                    date2,
                    date.getValue(),
                    payment,
                    numberField.getValue().intValue());
            save(activity, reserva);

            Notification.show("Reserva realizada con éxito", 3000, Notification.Position.BOTTOM_START);
            String message = "\nActivity: " + activity.getName() + "\nHour: " + activity.getInit() + "\n For: " +
                    numberField.getValue().intValue() + " people" + "Activity date: " + reserva.getDateWanted() +
                    "\nBooking made on: " + reserva.getDateReservation();
            EmailService.sendSimpleMessage(
                    u.getEmail(), "Your booking at Costa Vespertino", message);
            grid.setItems(updateList(u));
        } else Notification.show("No dispone de dinero suficiente", 3000, Notification.Position.BOTTOM_START);
        dialog.close();
    }

    public void save(Activity activity, ReservationActivity reservationActivity) {
        if (activity != null) {
            reservationActivityService.guardarReservation(reservationActivity);

        } else {
            Notification.show("Error", 3000, Notification.Position.BOTTOM_START);
        }
    }


    public List<Activity> updateList(Usuario u) {
        // Todas las actividades del barco con capacidad
        List<Servicio> servicio = servicioService.buscarActivity(u.getShip());
        List<Activity> lista = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dayoftheweek = date.getValue().format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")));
        for (int i = 0; i < servicio.size(); i++) {
            Activity activity = service.searchById(servicio.get(i).getIdServicio());
            Set<String> set = activity.diasToSetString();
            if (activity != null && activity.getCapacity() > 0 &&
                    set.contains(dayoftheweek) &&
                reservationActivityService.actividadesReservadas(activity, formatter.format(date.getValue())) < activity.getCapacity()) lista.add(activity);
        }

        return lista;
    }


}