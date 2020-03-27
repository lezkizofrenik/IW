package es.uca.iw;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.backend.*;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

@Route(value = "mainview", layout = MainView.class)
@PageTitle("Bienvenido")
@Secured({ "User", "Admin", "Gerente" })
public class HomeView extends VerticalLayout {
    protected Chart chart;
    private ReservationActivityService reservationActivityService;
    private ReservationHikeService reservationHikeService;
    private ReservationRestaurantService reservationRestaurantService;
    private ReservationEstablishmentService reservationEstablishmentService;
    private PaymentService paymentService;

    public HomeView( PaymentService paymentService, ReservationActivityService reservationActivityService, ReservationHikeService reservationHikeService,
                    ReservationRestaurantService reservationRestaurantService, ReservationEstablishmentService reservationEstablishmentService) {
        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null) {

            this.paymentService = paymentService;
            this.reservationActivityService = reservationActivityService;
            this.reservationEstablishmentService = reservationEstablishmentService;
            this.reservationHikeService = reservationHikeService;
            this.reservationRestaurantService = reservationRestaurantService;

            Usuario u = UI.getCurrent().getSession().getAttribute(Usuario.class);
            H1 titulo = new H1("Bienvenido/a, " + u.getNombre());
            VerticalLayout v = new VerticalLayout();
            v.setClassName("layout");
            if(SecurityUtils.hasRole("User") ){
                areaChart(v, u, paymentService);
                pie(v, u);
            }
            add(titulo,v);

        }
    }

    public void pie(VerticalLayout v, Usuario u){
        int actividades = reservationActivityService.buscarPorUsuario(u).size();
        int establecimientos = reservationEstablishmentService.buscarPorUsuario(u).size();
        int excursiones = reservationHikeService.buscarPorUsuario(u).size();
        int restaurantes = reservationRestaurantService.buscarPorUsuario(u).size();
        Card c = new Card();
        VerticalLayout v2 = new VerticalLayout();
        int total = actividades + establecimientos + excursiones + restaurantes;
        chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Reservas realizadas");

        //Tooltip tooltip = new Tooltip();
        //tooltip.setValueDecimals(1);
        //conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Actividades", actividades));
        series.add(new DataSeriesItem("Excursiones", excursiones));
        if (establecimientos > 0) series.add(new DataSeriesItem("Establecimientos", establecimientos));
        if (restaurantes > 0) series.add(new DataSeriesItem("Restaurantes", restaurantes));


        conf.setSeries(series);
        chart.setVisibilityTogglingDisabled(true);

        if (total > 0) v2.add(chart);
        else  v2.add(new Text("Aún no ha realizado ninguna reserva"));
        c.setWidth("80%");
        c.add(v2);
        v.add(c);

    }


    public void areaChart(VerticalLayout v, Usuario u, PaymentService paymentService){
        VerticalLayout v2 = new VerticalLayout();
        Card c = new Card();
        v2.setClassName("layout");
        String [] axisX = categorias(paymentService, u);
        if(axisX.length>0){


            Chart chart = new Chart(ChartType.AREASPLINE);
            Configuration conf = chart.getConfiguration();
            conf.setTitle(new Title("Gastos"));


            Legend legend = new Legend();
            legend.setLayout(LayoutDirection.VERTICAL);
            legend.setAlign(HorizontalAlign.LEFT);
            legend.setFloating(true);
            legend.setVerticalAlign(VerticalAlign.TOP);
            legend.setX(150);
            legend.setY(100);
            conf.setLegend(legend);
            XAxis xAxis = new XAxis();
            xAxis.setCategories(axisX);


            PlotBand plotBand = new PlotBand(4.5, 6.5);
            plotBand.setZIndex(1);
            xAxis.setPlotBands(plotBand);
            conf.addxAxis(xAxis);

            YAxis yAxis = new YAxis();
            yAxis.setTitle(new AxisTitle("€"));
            conf.addyAxis(yAxis);

            Tooltip tooltip = new Tooltip();
            tooltip.setShared(true);
            tooltip.setValueSuffix("€");
            conf.setTooltip(tooltip);

            PlotOptionsArea plotOptions = new PlotOptionsArea();
            conf.setPlotOptions(plotOptions);

            ListSeries o = new ListSeries(u.getNombre());
            fillListSeries(o, axisX, u);
            conf.addSeries(o);
            v2.add(chart);
        }
        else v2.add(new Text("No ha realizado ningún gasto aún"));
        c.setWidth("80%");
        c.add(v2);
        v.add(c);
    }

    String[] categorias(PaymentService paymentService, Usuario u){
        List<String> lista = new ArrayList<>();
        List<Payment> payments = paymentService.buscarPorUsuario(u);
        String [] axisX = new String[0];
        if(!payments.isEmpty()){
            int j = 0;
            for(int i = 0; i < payments.size(); i++){
                if (!lista.contains(payments.get(i).getDate())){
                    lista.add(payments.get(i).getDate());
                }
            }
            axisX = new String[lista.size()];

            for(int i = 0; i < lista.size(); i++){
                axisX[i] = lista.get(i);
            }
        }


        return axisX;
    }

    public void fillListSeries(ListSeries o, String[] axisX, Usuario u ){
        for (int i = 0; i < axisX.length; i++){
            List<Payment> payments = paymentService.buscarPorUsuarioYFecha(u, axisX[i]);
            if(!payments.isEmpty()){
                float total = 0;
                for(int j = 0; j < payments.size(); j++){
                    total+=payments.get(j).getTotal();
                }
                o.addData(total);
            }
        }
    }
}



