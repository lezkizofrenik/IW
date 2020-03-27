package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

import org.springframework.security.access.annotation.Secured;

import es.uca.iw.backend.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Route(value = "ReservationEstablishment", layout = MainView.class)
@PageTitle("Reserva de los establecimientos")
@Secured({"User"})
public class ReservationEstablishmentView extends AbstractView {

    private Grid<Establishment> grid = new Grid<>(Establishment.class);
    private DatePicker date = new DatePicker("Fecha");

    private EstablishmentService service;
    private CiteService citeService;
    private ServicioService servicioService;
    private ReservationEstablishmentService reservationEstablishmentService;
    private static final long serialVersionUID = 1L;
    private ComboBox<String> comboBox =
            new ComboBox<>("Turno");
    private NumberField numberField = new NumberField();
    private DatePicker datePicker = new DatePicker("Fecha");

    public ReservationEstablishmentView(CiteService citeService, EstablishmentService service, ServicioService servicioService, ReservationEstablishmentService reservationEstablishmentService) {
        this.servicioService = servicioService;
        this.service = service;
        this.citeService = citeService;
        this.reservationEstablishmentService = reservationEstablishmentService;


        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
            H1 titulo = new H1("Reservar establecimientos");
            VerticalLayout v = new VerticalLayout();
            inicializar(v, u);


               //Puedo hacer un changelistener y filtrar por criterios

                Button showTables = new Button("Reservar", event -> {
                    if(grid.asSingleSelect().getValue()!= null)
                         reservar(u, reservationEstablishmentService);
                    else Notification.show("Seleccione un servicio", 3000, Notification.Position.BOTTOM_START);
                });

                HorizontalLayout h = new HorizontalLayout();
                h.add(datePicker, comboBox, numberField);
                v.add(titulo, h, grid, showTables);

           add(v);
        } else {
            add(new Text("Usted no está autorizado para ver este contenido"));
        }
    }


    public void updateList(Usuario u) {
        List<Servicio> servicio = servicioService.buscarEstablishment(u.getShip());
        List<Establishment> lista = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatted = formatter.format(datePicker.getValue());
        for(int i = 0; i < servicio.size(); i++){
            Establishment establishment = service.searchById(servicio.get(i).getIdServicio());
            List<ReservationEstablishment> reservas = reservationEstablishmentService.buscarPorEstablecimiento(establishment);
            if (establishment!= null && citeService.nCitesEstablishment(establishment) >
                    reservationEstablishmentService.nReservasPorEstablecimientoYFecha(establishment, dateFormatted))
                    lista.add(establishment);
        } //Lista los servicios con citas libres
        if(!lista.isEmpty()) //Puede salir directamente vacio, hay q quitarlo
            grid.setItems(lista);
        //else v.add(new Text("No hay servicios disponibles"));

    }


    public void updateComboBox(Usuario u){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatted = formatter.format(datePicker.getValue());
        List<String> citeList = new ArrayList<>();
        Establishment e = grid.asSingleSelect().getValue();
        List<Cite> cites = citeService.searchEstablishment(e);
        for(int i = 0; i < cites.size(); i++){
           if(reservationEstablishmentService.nReservasPorEstablecimientoYFechaYTurno(e, dateFormatted, cites.get(i).getInit())==0)  citeList.add(cites.get(i).getInit());
        }
        //Collections.sort(citeList); Si se mete en orden saldrá en orden
        comboBox.setValue(citeList.get(0));
        comboBox.setItems(citeList);
    }

    public void inicializar(VerticalLayout v, Usuario u){

        comboBox.setEnabled(false);
        v.setWidth("90%");
        v.setAlignSelf(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        datePicker.setMin(LocalDate.now());                                                                                                // autoajustables
        datePicker.setValue(LocalDate.now());
        updateList(u);
        numberField.setValue(1d);
        numberField.setHasControls(true);
        numberField.setMin(1);
        numberField.setEnabled(false);

        datePicker.addValueChangeListener(dateevent-> updateList(u));

        grid.asSingleSelect().addValueChangeListener(event->{
            Establishment e = grid.asSingleSelect().getValue();
            if(e!=null){
                comboBox.setEnabled(true);
                updateComboBox(u);
                numberField.setEnabled(true);
                numberField.setValue(1d);
                if(!e.getType()){
                    numberField.setMin(1);
                    numberField.setMax(1);
                }
                else{
                    numberField.setMin(1);
                    numberField.setMax(10);
                }
            }
            else{
                comboBox.setEnabled(false);
                numberField.setEnabled(false);
            }

        });

        grid.setColumns( "name", "description");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumnByKey("description").setHeader("Descripción");
        grid.getColumns()
                .forEach(establishmentColumn -> establishmentColumn.setAutoWidth(true)); //Columnas autoajustables
    }

    public void reservar(Usuario u, ReservationEstablishmentService reservationEstablishmentService) {
        Establishment establishment = grid.asSingleSelect().getValue();
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Cancelar", event3 -> {
            dialog.close();
        });
        Cite cite = citeService.searchByEstablishmentAndInit(establishment, comboBox.getValue());
        Button confirmButton = new Button("Confirmar reserva", event2 -> {
             ReservationEstablishment reservationEstablishment = new ReservationEstablishment(u, cite, new Date(),datePicker.getValue(), numberField.getValue().intValue());
            reservationEstablishmentService.guardarReservation(reservationEstablishment);
            Notification.show("Reserva realizada con éxito", 3000, Notification.Position.BOTTOM_START);
            updateList(u);
           email(reservationEstablishment, u);
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        dialog.add(confirmButton, cancelButton);
                dialog.open();
    }

    public void email(ReservationEstablishment reservationEstablishment, Usuario u){
        Establishment establishment = reservationEstablishment.getCite().getEstablishment();
        String message;
        if(!establishment.getType()){
           message = "\nEstablecimiento: " + establishment.getName() + "\nHour: " + reservationEstablishment.getCite().getInit() + "\nFor: "
                    +"\nDate: " + reservationEstablishment.getDateWanted() +
                    "\nBooking made on: " + reservationEstablishment.getDateReservation();
        }
        else{
            message = "\nEstablecimiento: " + establishment.getName() + "\nHour: " + reservationEstablishment.getCite().getInit() + "\nFor: " +
                    numberField.getValue().intValue() + " people" + "\nDate: " + reservationEstablishment.getDateWanted() +
                    "\nBooking made on: " + reservationEstablishment.getDateReservation();
        }

        EmailService.sendSimpleMessage(
                u.getEmail(), "Your booking at Costa Vespertino", message);
    }
}

