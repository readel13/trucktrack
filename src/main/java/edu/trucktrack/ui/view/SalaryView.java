package edu.trucktrack.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsArea;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.SeriesTooltip;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.SalaryDTO;
import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.repository.jooq.TagJooqRepository;
import edu.trucktrack.dao.service.ExpensesService;
import edu.trucktrack.dao.service.SalaryService;
import edu.trucktrack.dao.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.ExpensesModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


@PermitAll
@RequiredArgsConstructor
@PageTitle("Your salary")
@Route(value = "/salary/:tripId?", layout = MainLayout.class)
public class SalaryView extends VerticalLayout implements BeforeEnterObserver {

    public static final String TRIP_ID_PATH_VARIABLE = "tripId";
    private WorkTripDTO trip;
    private Chart chart;
    private VirtualList<SalaryDTO> virtualList;

    private final SecurityUtils securityUtils;

    private final WorkTripService workTripService;

    private final SalaryService salaryService;
    private Supplier<Object> updateData;

    private EmployeeEntity currentEmployee;

    private SearchCriteriaRequest searchCriteriaRequest;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String currentCurrency;

    private final H1 title = new H1();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.trip = event.getRouteParameters()
                .get(TRIP_ID_PATH_VARIABLE)
                .map(Long::valueOf)
                .map(workTripService::getById)
                .orElse(null);
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.searchCriteriaRequest = buildInitialCriteriaRequest();
        this.virtualList = createVirtualList();

        this.updateData = () -> {
            virtualList.setItems(fetchData());
            chart.getConfiguration().setSeries(fetchChartData(chart));
            chart.getConfiguration().setTooltip(new Tooltip());
            chart.getConfiguration().getTooltip().setPointFormat("Total by group: {point.y} %s".formatted(currentCurrency));
            chart.drawChart();
            return null;
        };

        this.currentCurrency = Optional.ofNullable(trip)
                .map(WorkTripDTO::getCurrency)
                .orElse(currentEmployee.getCurrency().getName());

        var changeCurrencyMenuBar = new MenuBar();
        var changeCurrency = changeCurrencyMenuBar.addItem("Change currency");
        var changeCurrencySubMenu = changeCurrency.getSubMenu();
        for (var currency : Currency.values()) {
            changeCurrencySubMenu.addItem(currency.label(), e -> {
                currentCurrency = e.getSource().getText();
                updateData.get();
            });
        }

        this.chart = buildChart();

        super.setHeightFull();

        title.setText(trip == null ? "Your all salaries" : "Your salary from '%s' trip ".formatted(trip.getName()));
        add(title, changeCurrencyMenuBar, chart, new Button("Save new"), virtualList);
    }

    private Chart buildChart() {
        Chart chart = new Chart();

        var currency = Optional.ofNullable(trip)
                .map(WorkTripDTO::getCurrency)
                .orElse(currentEmployee.getCurrency().getName());

        DataSeries dataSeries = fetchChartData(chart);

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.AREA);
        configuration.getyAxis().setTitle("Money in " + currency);
        configuration.getxAxis().setTitle("Time");
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setTickInterval(TimeUnit.DAYS.toMillis(1));
        configuration.setTooltip(new Tooltip());
        configuration.setExporting(true);
        configuration.setTitle("Salaries from trip '%s'".formatted(trip.getName()));
        configuration.setSeries(dataSeries);
        chart.setSizeFull();

        return chart;
    }

    private DataSeries fetchChartData(Chart chart) {
        var convertedData = fetchData();
        long sum = (long) convertedData.stream()
                .mapToDouble(SalaryDTO::getSalary)
                .sum();

        var seriesItems = convertedData
                .stream()
                .map(salary -> new DataSeriesItem(salary.getCreatedAt().toInstant(ZoneOffset.UTC), salary.getSalary()))
                .toList();

        var dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Salary");
        chart.getConfiguration().setSubTitle("Total - %d %s".formatted(sum, currentCurrency));
        PlotOptionsArea plotOpts = new PlotOptionsArea();
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Earned: {point.y} " + currentCurrency);
        plotOpts.setTooltip(tooltip);
        dataSeries.setPlotOptions(plotOpts);
        dataSeries.setPlotOptions(plotOpts);
        return dataSeries;
    }

    private SearchCriteriaRequest buildInitialCriteriaRequest() {
        return SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder()
                        .companyId(currentEmployee.getCompany().getId().intValue())
                        .tripId(Optional.ofNullable(trip).map(WorkTripDTO::getId).orElse(null))
                        .build())
                .build();
    }

    private ComponentRenderer<Component, SalaryDTO> getExpenseComponentRenderer() {
        return new ComponentRenderer<>(
                salaryDTO -> {
                    HorizontalLayout cardLayout = new HorizontalLayout();
                    cardLayout.setMargin(true);
                    cardLayout.setAlignItems(Alignment.CENTER);

                    VerticalLayout infoLayout = new VerticalLayout();
                    infoLayout.setSpacing(false);
                    infoLayout.setPadding(false);

                    var salary = "%.2f %s".formatted(salaryDTO.getSalary(), salaryDTO.getCurrency());

                    var nameBadge = new HorizontalLayout(new H5("Earned %s at %s".formatted(salary, salaryDTO.getCreatedAt().format(dateTimeFormatter))));
                    nameBadge.setAlignItems(Alignment.CENTER);
                    infoLayout.add(nameBadge);

                    VerticalLayout moreDetailsLayout = new VerticalLayout();
                    moreDetailsLayout.setSpacing(false);
                    moreDetailsLayout.setPadding(false);

                    moreDetailsLayout.add(createRow("From work trip: ", new Text(salaryDTO.getTripName())));
                    moreDetailsLayout.add(createRow("Earned at: ", new Text(salaryDTO.getCreatedAt().format(dateTimeFormatter))));

                    infoLayout.add(new Details("More details", moreDetailsLayout));

                    cardLayout.add(infoLayout);

                    var contextMenu = new ContextMenu();
                    contextMenu.setTarget(cardLayout);
                    contextMenu.addItem("Delete", event -> {
                        salaryService.deleteById(salaryDTO.getId());
                        updateData.get();
                    });

                    return cardLayout;
                });
    }

    private Div createRow(String title, Component... components) {
        var rowLayout = new HorizontalLayout(new H5(title));
        rowLayout.add(components);
        rowLayout.setAlignItems(Alignment.CENTER);
        return new Div(rowLayout);
    }

    public VirtualList<SalaryDTO> createVirtualList() {
        VirtualList<SalaryDTO> list = new VirtualList<>();
        list.setRenderer(getExpenseComponentRenderer());
        list.setHeight("100%");
        list.setItems(fetchData());
        return list;
    }

    public List<SalaryDTO> fetchData() {
        return salaryService.getAll(searchCriteriaRequest, currentCurrency);
    }
}
