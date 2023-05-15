package edu.trucktrack.ui.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Labels;
import com.vaadin.flow.component.charts.model.PlotOptionsArea;
import com.vaadin.flow.component.charts.model.PlotOptionsBar;
import com.vaadin.flow.component.charts.model.SeriesTooltip;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.dao.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.TripModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@PermitAll
@RequiredArgsConstructor
@PageTitle("Your work trips")
@Route(value = "work-trip", layout = MainLayout.class)
public class WorkTripView extends VerticalLayout {

    private final SecurityUtils securityUtils;

    private final TruckService truckService;

    private final WorkTripService workTripService;

    private Supplier<Object> updateGridCallBack;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private EmployeeEntity currentEmployee;
    private SearchCriteriaRequest criteriaRequest;

    @PostConstruct
    public void init() {
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.criteriaRequest = buildInitialCriteriaRequest();

        Chart chart = buildChart();
        Grid<WorkTripDTO> workTripGrid = buildWorkTripGrid();
        this.updateGridCallBack = () -> {
            workTripGrid.setItems(fetchData());
            chart.getConfiguration().setSeries(fetchChartSalaryData());
            chart.drawChart();
            return null;
        };

        var addTripModal = new TripModal(null, true, updateGridCallBack, workTripService, truckService, securityUtils);

        var searchTextField = new TextField();
        searchTextField.setWidth("800px");
        searchTextField.setPlaceholder("Search");
        searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchTextField.addValueChangeListener(event -> {
            criteriaRequest.getFilterBy().setName(event.getValue());
            updateGridCallBack.get();
        });

        var searchAndCreate = new HorizontalLayout(searchTextField, addTripModal);
        searchAndCreate.setAlignItems(Alignment.CENTER);

        add(new H1("Your trips"), chart, searchAndCreate, workTripGrid);
    }

    private Chart buildChart() {
        DataSeries salaries = fetchChartSalaryData();
        DataSeries expenses = fetchChartExpensesData();
        Chart chart = new Chart();
        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.AREA);
        configuration.getyAxis().setTitle("Money in " + currentEmployee.getCurrency().getName());
        configuration.getxAxis().setTitle("Time");
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setTickInterval(TimeUnit.DAYS.toMillis(3));
        configuration.setTooltip(new Tooltip());
        configuration.setExporting(true);
        configuration.setTitle("Salaries/Expenses from trips by time");
        configuration.setSeries(salaries);
        configuration.addSeries(expenses);
        return chart;
    }

    private DataSeries fetchChartSalaryData() {
        var seriesItems = fetchSalaryData().stream()
                .map(trip -> new DataSeriesItem(trip.getCreatedAt().toInstant(ZoneOffset.UTC), trip.getSalary()))
                .toList();
        DataSeries dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Salaries");
        PlotOptionsArea plotOpts = new PlotOptionsArea();
        plotOpts.setColor(SolidColor.LIME); // or SolidColor.LIME
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Salary earned: {point.y} " + currentEmployee.getCurrency().getName());
        plotOpts.setTooltip(tooltip);
        dataSeries.setPlotOptions(plotOpts);
        return dataSeries;
    }

    private DataSeries fetchChartExpensesData() {
        var seriesItems = fetchExpensesData().stream()
                .map(trip -> new DataSeriesItem(trip.getCreatedAt().toInstant(ZoneOffset.UTC), trip.getCosts()))
                .toList();
        DataSeries dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Expenses");
        PlotOptionsArea plotOpts = new PlotOptionsArea();
        plotOpts.setColor(SolidColor.LIGHTBLUE);
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Expenses: {point.y} " + currentEmployee.getCurrency().getName());
        plotOpts.setTooltip(tooltip);
        dataSeries.setPlotOptions(plotOpts);
        return dataSeries;
    }


    private SearchCriteriaRequest buildInitialCriteriaRequest() {
        return SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder()
                        .employeeId(currentEmployee.getId().intValue())
                        .companyId(currentEmployee.getCompany().getId().intValue())
                        .build())
                .build();
    }

    private Grid<WorkTripDTO> buildWorkTripGrid() {
        GridPro<WorkTripDTO> grid = new GridPro<>();
        grid.addThemeVariants(GridProVariant.LUMO_COMPACT, GridProVariant.LUMO_WRAP_CELL_CONTENT);

        // TODO: Map Employee or Not?
        grid.addColumn(WorkTripDTO::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getTruckName).setHeader("Truck").setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getSalary).setHeader("Current salary").setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getCurrency).setHeader("Currency").setAutoWidth(true);
        grid.addComponentColumn(t -> new Badge(t.getSalaryType())).setHeader("Salary Type").setAutoWidth(true);
        grid.addColumn(new NumberRenderer<>(WorkTripDTO::getSalaryRate, "%(,.2f")).setHeader("Salary Rate").setAutoWidth(true);
        grid.addComponentColumn(this::mapIsActive).setHeader("Active").setAutoWidth(true);
        grid.addColumn(trip -> trip.getCreatedAt().format(dateTimeFormatter)).setHeader("Created");
        grid.addColumn(trip ->
                Optional.ofNullable(trip.getClosedAt())
                        .map(d -> d.format(dateTimeFormatter))
                        .orElse("")
        ).setHeader("Closed").setAutoWidth(true);

        Function<Long, RouteParameters> tripRouteParam = tripId -> new RouteParameters("tripId", String.valueOf(tripId));

        grid.addComponentColumn(trip -> {
            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
            menuBar.addItem("View expenses", event -> navigate(event, ExpenseView.class, tripRouteParam.apply(trip.getId())));
            menuBar.addItem("View salary", event -> navigate(event, ExpenseView.class, tripRouteParam.apply(trip.getId())));
            menuBar.addItem("Edit", event -> {
                var updateModal = new TripModal(trip, false, updateGridCallBack, workTripService, truckService, securityUtils);
                add(updateModal);
                updateModal.getDialog().open();
            });
            menuBar.addItem("Delete", event -> {
                workTripService.delete(trip.getId());
                Notification notification = Notification.show("Trip Deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGridCallBack.get();
            });
            return menuBar;
        }).setWidth("70px").setFlexGrow(0);

        grid.setItems(fetchData());

        return grid;
    }

    public void navigate(ClickEvent<?> event, Class<? extends Component> navigateTo, RouteParameters parameters) {
        event.getSource().getUI()
                .ifPresent(ui -> ui.navigate(navigateTo, parameters));
    }

    public Badge mapIsActive(WorkTripDTO tripDTO) {
        Badge badge = new Badge(tripDTO.isActive() ? "Yes" : "No");
        badge.setVariant(tripDTO.isActive() ? Badge.BadgeVariant.SUCCESS : Badge.BadgeVariant.ERROR);
        return badge;
    }

    private List<WorkTripDTO> fetchExpensesData() {
        return workTripService.getAndConvertExpenses(criteriaRequest, currentEmployee.getCurrency().getName());
    }

    private List<WorkTripDTO> fetchSalaryData() {
        return workTripService.getAndConvertSalaries(criteriaRequest, currentEmployee.getCurrency().getName());
    }

    private List<WorkTripDTO> fetchData() {
        return workTripService.get(criteriaRequest);
    }
}
