package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Dial;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.annotation.Secured;

@Route(value = "ReservationRestaurant", layout = MainView.class)
@PageTitle("Reserva de los restaurantes")
@Secured({ "User" })
public class ReservationRestaurantView extends AbstractView {

    private Grid<Restaurant> grid = new Grid<>(Restaurant.class);
    private RestaurantService service;
    private ServicioService servicioService;
    private TableService tableService;
    private ReservationRestaurantService reservationRestaurantService;
    private static final long serialVersionUID = 1L;

    private ComboBox<String> comboBox = new ComboBox<>("Turno");
    private ComboBox<Integer> numberField = new ComboBox<>("Número de personas");
    private DatePicker date = new DatePicker("Fecha");
    private List<Restaurant> lista;

    public ReservationRestaurantView(TableService tableService,
            ReservationRestaurantService reservationRestaurantService, RestaurantService service, ServicioService servicioService) {

        this.servicioService = servicioService;
        this.service = service;
        this.tableService = tableService;
        this.reservationRestaurantService = reservationRestaurantService;
        this.lista = new ArrayList<>();

        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

            VerticalLayout v = new VerticalLayout();
            HorizontalLayout h = new HorizontalLayout();
            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
            inicializar(v, h, u);
            add(v);

        } else {
                add(new Text("Usted no está autorizado para ver este contenido"));
            }
    }


    public TableRestaurant findAvailableTable(Restaurant restaurant, boolean turn){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<TableRestaurant> list = new ArrayList<>();
        List<TableRestaurant> lista = tableService.searchTablesCapacity(restaurant, numberField.getValue().intValue());
        //Listo todas las mesas de un restaurante y de cierta capacidad y luego quito las que esten reservadas del turno deseado
        System.out.println("Fecha reserva" + formatter.format(date.getValue()));
        if(!lista.isEmpty()){
            for(int i = 0; i < lista.size(); i++){
                if(reservationRestaurantService.buscarPorFechaRestauranteYturnoYmesa(restaurant, formatter.format(date.getValue()), turn, lista.get(i) ).isEmpty()) list.add(lista.get(i));
            }
        }

        if(!list.isEmpty()) return list.get(0); //Cojo el primero

        return null;

    }

    public void reservarTurno(Restaurant restaurant, boolean turn, Dialog dialog, Usuario u) {
        TableRestaurant table = findAvailableTable(restaurant, false);
        if (lista != null) {
            Button cancelButton = new Button("Cancelar", event3 -> {
                dialog.close();
            });

            Button confirmButton = new Button("Confirmar reserva", event2 -> {
                ReservationRestaurant reservationRestaurant = new ReservationRestaurant(
                        date.getValue(),
                        new Date(),
                        restaurant,
                        u,
                        turn,
                        table);

                reservationRestaurantService.guardarReservation(reservationRestaurant);
                dialog.close();
                Notification.show("Reserva realizada con éxito", 3000, Notification.Position.BOTTOM_START);
                String message = "\nRestaurant: " + restaurant.getName() +
                        "\nTurn: " + reservationRestaurant.getTurnParsed() + "\n For: " + table.getCapacity() + " people" +
                        "\nDay: " + reservationRestaurant.getDateWanted() + "\nBooking made on: "
                        + reservationRestaurant.getDateReservation();
                EmailService.sendSimpleMessage(
                        u.getEmail(), "Your booking at Costa Vespertino", message);
                UI.getCurrent().getPage().reload();

            });

            confirmButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            dialog.add(confirmButton, cancelButton);
            dialog.open();
        } else{
            if(!turn) Notification.show("No quedan mesas para el almuerzo" , 3000, Notification.Position.BOTTOM_START);
            else  Notification.show("No quedan mesas para la cena", 3000, Notification.Position.BOTTOM_START);
        }
    }


    public void reservar(Usuario u){
        Dialog dialog = new Dialog();

        Restaurant restaurant = grid.asSingleSelect().getValue();
        if (comboBox.getValue().equals("Almuerzo"))  reservarTurno(restaurant, false, dialog, u);
        else  reservarTurno(restaurant, true, dialog, u);

    }


    public void inicializar(VerticalLayout v, HorizontalLayout h, Usuario u){
        H1 titulo = new H1("Reservar mesa");
        v.setWidth("90%");
        v.setAlignSelf(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);


        grid.setColumns("name", "description");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumnByKey("description").setHeader("Descripción");
        grid.getColumns().forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true)); // Columnas
        date.setMin(LocalDate.now());                                                                                                // autoajustables

        comboBox.setItems("Almuerzo", "Cena");
        numberField.setItems(1, 2, 3, 4, 5, 6);

        Button ButtonBuscar = new Button("Buscar", event3->{
            if (comboBox.getValue().isEmpty())
                Notification.show("Debe indicar para qué turno quiere reservar", 3000,
                        Notification.Position.BOTTOM_START);
            if (numberField.getValue() == null)
                Notification.show("Debe indicar cuántas personas le acompañan", 3000,
                        Notification.Position.BOTTOM_START);
            if(date.getValue().toString().isEmpty())
                Notification.show("Debe introducir la fecha de la reserva", 3000,
                        Notification.Position.BOTTOM_START);

            if(!comboBox.getValue().isEmpty() && numberField.getValue() != null && !date.getValue().toString().isEmpty()) {
                if(comboBox.getValue() == "Almuerzo") updateListFilter(u, false);
                else updateListFilter(u, true);
            }

        });


        Button showTables = new Button("Reservar", event -> {
            if (grid.asSingleSelect().getValue() != null) {
                reservar(u);
            }
            else{
                Notification.show("Seleccione un restaurante", 3000, Notification.Position.BOTTOM_START);
            }
        });
        v.add(titulo, h, grid, showTables );
        h.add(numberField, comboBox, date, ButtonBuscar);


    }


    public void updateListFilter(Usuario u, boolean turn) {

        List<Servicio> servicio = servicioService.buscarRestaurant(u.getShip()); //Restaurantes del barco
        if(!servicio.isEmpty()){
            List<Restaurant> list = new ArrayList<>();
            for(int i = 0; i < servicio.size(); i++){
                Restaurant restaurant = service.searchById(servicio.get(i).getIdServicio());
                if (restaurant!= null && findAvailableTable(restaurant, turn)!=null){ //Restaurantes DISPONIBLES
                    list.add(restaurant);
                }
            }
            lista = list;
            grid.setItems(lista);
        }
    }



}