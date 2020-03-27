package es.uca.iw;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;
import org.springframework.security.access.annotation.Secured;

import java.awt.*;

@Route(value = "AdviceView", layout = MainView.class)
@Secured({ "Admin" })
@PageTitle("Gestión de los consejos")
public class AdviceView extends VerticalLayout {
    private AdviceService adviceService;
    private ComboBox<Ship> comboBox =
            new ComboBox<>("Barco");
    private Grid<Advice> grid = new Grid<>(Advice.class);
    private TextField labelField = new TextField();
    private boolean buttonClicked = false;
    private ShipService shipService;
    private String textButton = "Crear";

    public AdviceView(AdviceService adviceService, ShipService shipService){
        this.adviceService = adviceService;
        this.shipService = shipService;

        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {
            H1 titulo = new H1("Gestionar consejos");
            updateList(adviceService);
            labelField.setEnabled(false);
            labelField.getStyle().set("minHeight", "150px");
            labelField.setPlaceholder("Write here ...");
            labelField.setRequired(true);
            labelField.setMinLength(1);
            labelField.setMaxLength(255);
            comboBox.setRequired(true);

            updateComboBox(shipService);
            Button borrar = new Button("Borrar");
            borrar.addClickListener(borrarEvent->{
                if(grid.asSingleSelect().getValue()!=null) borrar(grid.asSingleSelect().getValue(), adviceService);
            });

            grid.getColumns()
                    .forEach(restaurantColumn -> restaurantColumn.setAutoWidth(true));
            Button crearoModificar = new Button(textButton);
            crearoModificar.addClickListener(event->{
                Advice advice = grid.asSingleSelect().getValue();
                if(advice!=null){
                    modificar(advice, adviceService);
                }
                else if(comboBox.getValue()!=null && !labelField.isEmpty()) crear(adviceService);
            });

            HorizontalLayout h2 = new HorizontalLayout(crearoModificar, borrar);
            VerticalLayout v = new VerticalLayout();
            v.add(comboBox, labelField, crearoModificar);
            VerticalLayout v2 = new VerticalLayout();
            Button añadir = new Button("Gestionar");
            v2.add(titulo, añadir, grid);
            HorizontalLayout h = new HorizontalLayout();
            h.add(v2);
            añadir.addClickListener(button->{
                buttonClicked = !buttonClicked;
                if(buttonClicked) h.add(v);
                else h.remove(v);

            });

            h.setSizeFull();
            h.getStyle().set("margin", "auto");
            v2.setMaxHeight("20%");
            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
            comboBox.addValueChangeListener(comboBoxevent->{
                if(comboBox.getValue()!=null) labelField.setEnabled(true);

            });

            grid.asSingleSelect().addValueChangeListener(gridEvent->{
                if(grid.asSingleSelect().getValue()!=null){
                    rellenar();
                    crearoModificar.setText("Modificar");
                }
                else{
                    rellenarPorDefecto();
                    crearoModificar.setText("Crear");
                }
            });

            add(h);

        }

    }

    public void updateComboBox(ShipService shipService){
        comboBox.setItems(shipService.listarShip());
    }

    public void updateList(AdviceService adviceService){
        grid.setItems(adviceService.listar());
    }

    public void rellenar(){
        comboBox.setValue(grid.asSingleSelect().getValue().getShip());
        labelField.setValue(grid.asSingleSelect().getValue().getDescription());
    }

    public void rellenarPorDefecto(){
        comboBox.setValue(null);
        labelField.setValue("");
    }

    public void modificar(Advice advice, AdviceService adviceService){
        advice.setShip(comboBox.getValue());
        advice.setDescription(labelField.getValue());
        adviceService.save(advice);
        rellenarPorDefecto();
        updateList(adviceService);
    }

    public void crear(AdviceService adviceService){
        Advice advice = new Advice(comboBox.getValue(), labelField.getValue());
        adviceService.save(advice);
        updateList(adviceService);
        rellenarPorDefecto();

    }

    public void borrar(Advice advice, AdviceService adviceService){
        Dialog dialog = new Dialog();
        VerticalLayout v = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();
        Text text = new Text("¿Está seguro de que desea eliminar el consejo?");
        Button confirmar = new Button("Confirmar");
        Button cancelar = new Button ("Cancelar");
        confirmar.addClickListener(confirmarEvent->{
            adviceService.borrar(advice);
            com.vaadin.flow.component.notification.Notification.show("Borrado con éxito", 3000, Notification.Position.BOTTOM_START);
            dialog.close();
            updateList(adviceService);
        });

        confirmar.addClickListener(cancelarEvent->{
            dialog.close();
        });
        h.add(confirmar, cancelar);
        v.add(text, h);
        dialog.add(v);
    }
}
