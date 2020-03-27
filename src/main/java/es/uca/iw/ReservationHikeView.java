package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.security.access.annotation.Secured;

@Route(value = "ReservationHike", layout = MainView.class)
@PageTitle("Reserva de las excursiones")
@Secured({"User"})
public class ReservationHikeView extends AbstractView {

    private Grid<Hike> grid = new Grid<>(Hike.class);
    private HikeService service;
    private ServicioService servicioService;
    private ReservationHikeService reservationHikeService;
    private static final long serialVersionUID = 1L;
    private ComboBox<City> comboBox =
            new ComboBox<>("Escala");
    private NumberField numberField = new NumberField();

    public ReservationHikeView(BankAccountService bankAccountService, 
    ReservationHikeService reservationHikeService, HikeService service, 
    UsuarioService usuarioService, PaymentService pagoService, ServicioService servicioService) {

        this.servicioService = servicioService;
        this.service = service;
        this.reservationHikeService = reservationHikeService;


        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {
            numberField.setEnabled(false);
            numberField.setValue(1d);
            numberField.setHasControls(true);
            numberField.setMin(1);
            grid.asSingleSelect().addValueChangeListener(event->  {
                if(grid.asSingleSelect().getValue()!= null) {
                    numberField.setEnabled(true);
                    numberField.setValue(1d);
                    numberField.setMax(grid.asSingleSelect().getValue().getCapacity());
                }
                else numberField.setEnabled(false);

            });

            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
            updateComboBox(u);
            comboBox.addValueChangeListener(event -> updateListFilter(u));

            grid.setColumns("name", "description","city", "init", "time", "price", "capacity");
            grid.getColumnByKey("name").setHeader("Nombre");
            grid.getColumnByKey("description").setHeader("Descripción");
            grid.getColumnByKey("city").setHeader("Escala");
            grid.getColumnByKey("init").setHeader("Hora de inicio");
            grid.getColumnByKey("time").setHeader("Duración");
            grid.getColumnByKey("price").setHeader("Precio");
            grid.getColumnByKey("capacity").setHeader("Plazas disponibles");
            grid.getColumns()
            .forEach(hikeColumn -> hikeColumn.setAutoWidth(true));

            VerticalLayout v = new VerticalLayout();

            H1 titulo = new H1("Reservar excursiones");

            if (!updateList(u).isEmpty()) {
                grid.setItems(updateList(u));

                Button payButton = new Button("Confirmar reserva", event -> {
                    Hike hike = grid.asSingleSelect().getValue();

                    if (hike == null)
                        Notification.show("Seleccione una excursión", 3000, Notification.Position.BOTTOM_START);
                    else {
                        Dialog dialog = new Dialog();
                        Button confirmButton = new Button("Pagar", event2 -> {

                            float total = (hike.getPrice()*numberField.getValue().intValue());
                            //Numberfield esta controlado por el changeValue, luego no necesita mensaje de error
                            if (u.getBankAccount().getMoney() >= total && numberField.getValue().intValue() <= hike.getCapacity() ) {
                                Date date = new Date();
                                hike.decrementCapacity(numberField.getValue().intValue());
                                service.guardarHike(hike);
                                Payment payment = new Payment(total, u, hike.getId(), 1, date);
                                pagoService.guardarPayment(payment);
                                u.getBankAccount().payment(total);
                                bankAccountService.guardarBankAccount(u.getBankAccount());

                                ReservationHike reserva = new ReservationHike(
                                        hike,
                                        u,
                                        new Date(),
                                        payment,
                                        numberField.getValue().intValue());
                                reservationHikeService.guardarReservation(reserva);


                                Notification.show("Reserva realizada con éxito", 3000, Notification.Position.BOTTOM_START);
                                String message = "\nHike: " + hike.getName() + "\nCity: " + hike.getCity().getName() + "\n For: " +
                                        numberField.getValue().intValue() + " people" + "\nBooking made on: " + reserva.getDate();
                                EmailService.sendSimpleMessage(
                                        u.getEmail(), "Your booking at Costa Vespertino", message);
                                grid.setItems(updateList(u));
                            } else Notification.show("No dispone de dinero suficiente", 3000, Notification.Position.BOTTOM_START);
                            dialog.close();

                        });

                        confirmButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                        Button cancelButton = new Button("Cancelar", event3 -> {
                            dialog.close();
                        });
                        HorizontalLayout dv = new HorizontalLayout();
                        dv.setClassName("dialog");
                        dv.add(confirmButton, cancelButton);
                        dialog.add(dv);
                        dialog.open();

                    }
                });
                HorizontalLayout h = new HorizontalLayout();
                h.add(numberField, payButton);
                v.add(titulo, grid, h);
                v.setWidth("90%");
                v.setAlignSelf(Alignment.CENTER);
                setAlignItems(Alignment.CENTER);
                add(v);
            }
            else{
                v.add(new Text("No hay excursiones disponibles"));
            }
            add(v);
        }else {
            add(new Text("Usted no está autorizado para ver este contenido"));
        }


    }

    private void updateListFilter(Usuario u) {
        List<Hike> copia = updateList(u);
        if(comboBox.getValue()!=null){
            for(int i = 0; i < updateList(u).size(); i++){
                if(copia.get(i).getCity()!= comboBox.getValue()) copia.remove(i);
            }
            grid.setItems(copia);
        }
        else grid.setItems(updateList(u));

    }


    public List<Hike> updateList(Usuario u) {
        Set<String> ruta = u.getShip().getViaje().rutaToSetString();
        Iterator<String> it = ruta.iterator();
        List<Hike> lista = new ArrayList<>();

        while(it.hasNext()){
            lista.addAll(service.searchByCity_NameAndAvailables(it.next()));
        }

        return lista;
    }

    public void updateComboBox(Usuario u){
       List<Hike> list = updateList(u);
       if(!list.isEmpty()){
           List<City> cities = new ArrayList<>();
           for(int i = 0; i < list.size(); i++ ){
               cities.add(list.get(i).getCity());
           }

           comboBox.setItems(cities);
       }

    }
}