package es.uca.iw;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import es.uca.iw.backend.*;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "Charts", layout = MainView.class)
@Secured({"Gerente","Admin"})

public class EstadisticaView extends AbstractView {


    private static final long serialVersionUID = 1L;

    @Autowired
    public EstadisticaView(ActivityService activityService,
                           HikeService hikeService,
                           EstablishmentService establishmentService,
                           RestaurantService restaurantService,
                           CityService cityService,
                           ShipService shipService,
                           ReservationActivityService reservationActivityService) {
        Chart chart = activitiesChart(activityService,
                hikeService,
                establishmentService,
                restaurantService);
        this.add(chart);
        this.add(hikesByCity(hikeService, cityService));
    }

    private Chart activitiesChart(ActivityService activityService,
                                  HikeService hikeService,
                                  EstablishmentService establishmentService,
                                  RestaurantService restaurantService) {
        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Nº de actividades disponibles");

        chart.getConfiguration().getChart().setType(ChartType.BAR);

        configuration.addSeries(new ListSeries("actividades", activityService.listar().size()));
        configuration.addSeries(new ListSeries("hikes", hikeService.listar().size()));
        configuration.addSeries(new ListSeries("establishments",
                establishmentService.listar().size()));
        configuration.addSeries(new ListSeries("restaurants",
                restaurantService.listar().size()));

        XAxis x = new XAxis();
        x.setCategories("Tipos de actividades");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle yTitle = new AxisTitle();
        yTitle.setText("Numero de actividades de este tipo");
        yTitle.setAlign(VerticalAlign.HIGH);
        y.setTitle(yTitle);
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueSuffix(" Numero de actividades de este tipo");
        configuration.setTooltip(tooltip);

        PlotOptionsBar plotOptions = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions.setDataLabels(dataLabels);
        configuration.setPlotOptions(plotOptions);
        return chart;
    }

    private Chart hikesByCity(HikeService hikeService,
                              CityService cityService) {
        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Nº de hikes");

        chart.getConfiguration().getChart().setType(ChartType.BAR);

        for (City city : cityService.listarCity()) {
            configuration.addSeries(
                    new ListSeries(city.getName(),
                            hikeService.searchByCity(city).size()));
        }

        XAxis x = new XAxis();
        x.setCategories("City");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle yTitle = new AxisTitle();
        yTitle.setText("Numero de hikes");
        yTitle.setAlign(VerticalAlign.HIGH);
        y.setTitle(yTitle);
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueSuffix(" Numero de hikes");
        configuration.setTooltip(tooltip);

        PlotOptionsBar plotOptions = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions.setDataLabels(dataLabels);
        configuration.setPlotOptions(plotOptions);
        return chart;
    }


}
