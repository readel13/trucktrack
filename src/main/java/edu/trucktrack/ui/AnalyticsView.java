package edu.trucktrack.ui;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "analytics", layout = HomeView.class)
public class AnalyticsView extends VerticalLayout {
    public AnalyticsView() {
        H1 title = new H1("This is analytics view. Some numbers");

        Chart chartPie = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries(
                new DataSeriesItem("artur", 100),
                new DataSeriesItem("andrii", 10)
        );

        chartPie.getConfiguration().setSeries(dataSeries);

        add(title, chartPie);
    }
}
