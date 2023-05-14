package edu.trucktrack.ui.view;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import edu.trucktrack.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "analytics", layout = MainLayout.class)
public class AnalyticsView extends VerticalLayout {
    public AnalyticsView() {
        H1 title = new H1("This is analytics view. Some numbers");

        Chart chartPie = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries(
                new DataSeriesItem("Section 1", 100),
                new DataSeriesItem("Section 2", 10)
        );

        chartPie.getConfiguration().setSeries(dataSeries);

        DataSeries anotherSeries = new DataSeries(
                new DataSeriesItem("Section 1", 23),
                new DataSeriesItem("Section 1", 24),
                new DataSeriesItem("Section 1", 25),
                new DataSeriesItem("Section 2", 11),
                new DataSeriesItem("Section 2", 7),
                new DataSeriesItem("Section 2", 9)
        );

        anotherSeries.setName("Salaries");

        Chart chart = new Chart();
        chart.getConfiguration().getChart().setType(ChartType.AREA);
        chart.getConfiguration().setSeries(anotherSeries);


        add(title, chartPie, chart);
    }
}
