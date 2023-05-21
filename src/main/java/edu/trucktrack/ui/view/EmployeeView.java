package edu.trucktrack.ui.view;

import com.github.diasadm.DateRange;
import com.github.diasadm.DateRangePicker;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.SeriesTooltip;
import com.vaadin.flow.component.charts.model.Stacking;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import edu.trucktrack.api.dto.EmployeeDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.service.EmployeeService;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.dao.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.TripModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@PermitAll
@RequiredArgsConstructor
@PageTitle("Employees")
@Route(value = "employee", layout = MainLayout.class)
public class EmployeeView extends VerticalLayout {

    private final SecurityUtils securityUtils;

    private final EmployeeService employeeService;

    private Supplier<Object> updateGridCallBack;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private EmployeeEntity currentEmployee;

    private Chart chart;

    private Grid<EmployeeDTO> employeeGrid;

    private String currentCurrency;
    private SearchCriteriaRequest criteriaRequest;

    @PostConstruct
    public void init() {
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.currentCurrency = securityUtils.getCurrentEmployee().getCurrency().getName();
        this.criteriaRequest = buildInitialCriteriaRequest();

        this.updateGridCallBack = () -> {
            List<EmployeeDTO> updatedData = fetchData();
            employeeGrid.setItems(updatedData);
            fetchChartData(chart.getConfiguration(), updatedData);
            chart.getConfiguration().getxAxis().setTitle("Money in " + currentCurrency);
            chart.drawChart();
            return null;
        };

        List<EmployeeDTO> data = fetchData();
        this.chart = buildChart(currentCurrency, data);
        this.employeeGrid = buildGrid(data);

        var changeCurrencyMenuBar = new MenuBar();
        var changeCurrency = changeCurrencyMenuBar.addItem("Change currency");
        var changeCurrencySubMenu = changeCurrency.getSubMenu();
        for (var currency : Currency.values()) {
            changeCurrencySubMenu.addItem(currency.label(), event -> {
                currentCurrency = event.getSource().getText();
                updateGridCallBack.get();
            });
        }

        var dateRangePicker = new DateRangePicker("Select dates ", "", "", true);
        dateRangePicker.getStartDatePicker().setPlaceholder("Start Date");
        dateRangePicker.getEndDatePicker().setPlaceholder("End Date");
        dateRangePicker.addValueChangeListener(e -> {
            DateRange value = e.getValue();
            criteriaRequest = criteriaRequest.toBuilder()
                    .filterBy(criteriaRequest.getFilterBy().toBuilder()
                            .from(value.getBeginDate())
                            .to(value.getEndDate())
                            .build())
                    .build();
            updateGridCallBack.get();
        });
        var clearButton = new Button("Clear", e -> {
            criteriaRequest = criteriaRequest.toBuilder()
                    .filterBy(criteriaRequest.getFilterBy().toBuilder()
                            .from(null)
                            .to(null)
                            .build())
                    .build();
            updateGridCallBack.get();
            dateRangePicker.clear();
        });

        add(new H1("Your team"), changeCurrencyMenuBar, chart, dateRangePicker, employeeGrid);
    }

    private Chart buildChart(String currency, List<EmployeeDTO> data) {
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration configuration = chart.getConfiguration();
        configuration.getyAxis().setTitle("Money in " + currency);
        configuration.getxAxis().setTitle("Employees");
        configuration.setTooltip(new Tooltip());
        configuration.setExporting(true);
        configuration.setTitle("Employee performance");

        fetchChartData(configuration, data);

        return chart;
    }

    public void fetchChartData(Configuration config, List<EmployeeDTO> data) {
        config.setSeries(buildChartSalariesData(data), buildChartExpensesData(data));
        config.getxAxis().setCategories(data.stream().map(EmployeeDTO::getName).toArray(String[]::new));
    }

    private DataSeries buildChartSalariesData(List<EmployeeDTO> employees) {
        var seriesItems = employees.stream()
                .map(employeeDTO -> new DataSeriesItem(employeeDTO.getName(), employeeDTO.getTotalSalary()))
                .toList();
        DataSeries dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Salaries");
        PlotOptionsColumn plotOpts = new PlotOptionsColumn();
        plotOpts.setColor(SolidColor.LIGHTBLUE); // or SolidColor.LIME
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Salary earned: {point.y} " + currentCurrency);
        plotOpts.setTooltip(tooltip);
        dataSeries.setPlotOptions(plotOpts);
        return dataSeries;
    }

    private DataSeries buildChartExpensesData(List<EmployeeDTO> employees) {
        var seriesItems = employees.stream()
                .map(employeeDTO -> new DataSeriesItem(employeeDTO.getName(), employeeDTO.getTotalExpenses()))
                .toList();
        DataSeries dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Expenses");
        PlotOptionsColumn plotOpts = new PlotOptionsColumn();
        plotOpts.setColor(SolidColor.LIGHTCORAL);
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Expenses: {point.y} " + currentCurrency);
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

    private Grid<EmployeeDTO> buildGrid(List<EmployeeDTO> data) {
        GridPro<EmployeeDTO> grid = new GridPro<>();
        grid.addThemeVariants(GridProVariant.LUMO_COMPACT, GridProVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(EmployeeDTO::getName).setHeader("Name").setSortable(true).setAutoWidth(true);
        grid.addColumn(EmployeeDTO::getEmail).setHeader("Email").setSortable(true).setAutoWidth(true);
        grid.addColumn(EmployeeDTO::getPhoneNumber).setHeader("Phone number").setAutoWidth(true);
        grid.addColumn(e -> "%d %s".formatted(e.getTotalSalary(), e.getTotalCurrency())).setSortable(true).setComparator(EmployeeDTO::getTotalSalary).setHeader("Current salary").setAutoWidth(true);
        grid.addColumn(e -> "%d %s".formatted(e.getTotalExpenses(), e.getTotalCurrency())).setSortable(true).setComparator(EmployeeDTO::getTotalExpenses).setHeader("Current expenses").setAutoWidth(true);
        grid.addColumn(e -> e.getCurrency().getName()).setHeader("Primary Currency").setAutoWidth(true);
        grid.addColumn(trip -> trip.getCreatedAt().format(dateTimeFormatter)).setHeader("As of");

        Function<Long, RouteParameters> employeeRouteParam = employeeId -> new RouteParameters("employeeId", String.valueOf(employeeId));

        grid.addComponentColumn(employee -> {
            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
            menuBar.addItem("View trips", event -> navigate(event, WorkTripView.class, employeeRouteParam.apply(employee.getId())));
            return menuBar;
        }).setWidth("70px").setFlexGrow(0);

        grid.setItems(data);

        return grid;
    }

    public void navigate(ClickEvent<?> event, Class<? extends Component> navigateTo, RouteParameters parameters) {
        event.getSource().getUI().ifPresent(ui -> ui.navigate(navigateTo, parameters));
    }

    private List<EmployeeDTO> fetchData() {
        return employeeService.getAllForGrid(currentEmployee.getCompany().getId(), currentCurrency);
    }
}
