package edu.trucktrack.ui.view;

import com.github.diasadm.DateRange;
import com.github.diasadm.DateRangePicker;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsArea;
import com.vaadin.flow.component.charts.model.SeriesTooltip;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.entity.WorkTripEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.entity.enums.SalaryType;
import edu.trucktrack.dao.service.EmployeeService;
import edu.trucktrack.dao.service.SalaryService;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.dao.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.ChoosingDialog;
import edu.trucktrack.ui.modal.TripModal;
import edu.trucktrack.util.EmailUtil;
import edu.trucktrack.util.ReportUtil;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@PermitAll
@RequiredArgsConstructor
@PageTitle("Your work trips")
@Route(value = "/work-trip/:employeeId?", layout = MainLayout.class)
public class WorkTripView extends VerticalLayout implements BeforeEnterObserver {

    public static final String EMPLOYEE_ID_PATH_VARIABLE = "employeeId";
    private final SecurityUtils securityUtils;

    private final TruckService truckService;

    private final WorkTripService workTripService;

    private final ReportUtil reportUtil;

    private final EmailUtil emailUtil;

    private final SalaryService salaryService;

    private final EmployeeService employeeService;

    private Supplier<Object> updateGridCallBack;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private EmployeeEntity currentEmployee;

    private boolean isPassedEmployee;

    private Grid<WorkTripDTO> workTripGrid;
    private Grid.Column<WorkTripDTO> gridNameColumn;
    private Grid.Column<WorkTripDTO> gridSalaryTypeColumn;
    private Grid.Column<WorkTripDTO> gridSalaryRateColumn;
    private Grid.Column<WorkTripDTO> gridActiveColumn;

    private Chart chart;

    private String currentCurrency;
    private SearchCriteriaRequest criteriaRequest;

    @Override
    public void beforeEnter(BeforeEnterEvent enterEvent) {
        this.currentEmployee = enterEvent.getRouteParameters()
                .get(EMPLOYEE_ID_PATH_VARIABLE)
                .map(Long::valueOf)
                .map(employeeService::getById)
                .orElse(securityUtils.getCurrentEmployee());

        this.isPassedEmployee = !currentEmployee.getId().equals(securityUtils.getCurrentEmployee().getId());
        this.currentCurrency = currentEmployee.getCurrency().getName();
        this.criteriaRequest = buildInitialCriteriaRequest();

        this.chart = buildChart(currentCurrency);
        workTripGrid = buildWorkTripGrid();
        this.updateGridCallBack = () -> {
            setGridDataAndMetrics(workTripGrid);
            chart.getConfiguration().setSeries(fetchChartSalaryData(currentCurrency), fetchChartExpensesData(currentCurrency));
            chart.getConfiguration().getxAxis().setTitle("Money in " + currentCurrency);
            chart.drawChart();
            return null;
        };

        var changeCurrencyMenuBar = new MenuBar();
        var changeCurrency = changeCurrencyMenuBar.addItem("Change currency");
        var changeCurrencySubMenu = changeCurrency.getSubMenu();
        for (var currency : Currency.values()) {
            changeCurrencySubMenu.addItem(currency.label(), event -> {
                currentCurrency = event.getSource().getText();
                updateGridCallBack.get();
            });
        }

        var addTripModal = new TripModal(null, true, updateGridCallBack, workTripService, truckService, currentEmployee);

        var searchTextField = new TextField();
        searchTextField.setWidth("800px");
        searchTextField.setPlaceholder("Search");
        searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchTextField.addValueChangeListener(event -> {
            criteriaRequest.getFilterBy().setName(event.getValue());
            updateGridCallBack.get();
        });

        var dateRangePicker = new DateRangePicker("", "", "", false);
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

        var pickerLayout = new HorizontalLayout(dateRangePicker, clearButton);
        pickerLayout.getStyle().set("margin-left", "auto");

        var searchAndCreate = new HorizontalLayout(searchTextField, addTripModal, pickerLayout);
        searchAndCreate.setVerticalComponentAlignment(Alignment.CENTER, searchTextField, addTripModal, pickerLayout);
        searchAndCreate.setJustifyContentMode(JustifyContentMode.BETWEEN);
        searchAndCreate.setAlignItems(Alignment.CENTER);
        searchAndCreate.setWidthFull();

        add(new H1(isPassedEmployee ? "%s's work trips".formatted(currentEmployee.getName()) : "Your work trips"), changeCurrencyMenuBar, chart, searchAndCreate, workTripGrid);
    }

    private Chart buildChart(String currency) {
        DataSeries salaries = fetchChartSalaryData(currency);
        DataSeries expenses = fetchChartExpensesData(currency);
        Chart chart = new Chart();
        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.AREA);
        configuration.getyAxis().setTitle("Money in " + currency);
        configuration.getxAxis().setTitle("Time");
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setTickInterval(TimeUnit.DAYS.toMillis(3));
        configuration.setTooltip(new Tooltip());
        configuration.setExporting(true);
        configuration.setTitle("Salaries/Expenses from work trips by time");
        configuration.setSeries(salaries);
        configuration.addSeries(expenses);
        return chart;
    }

    private DataSeries fetchChartSalaryData(String currency) {
        var seriesItems = fetchSalaryData(currency).stream()
                .map(trip -> new DataSeriesItem(trip.getCreatedAt().toInstant(ZoneOffset.UTC), trip.getSalary()))
                .toList();
        DataSeries dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Salaries");
        PlotOptionsArea plotOpts = new PlotOptionsArea();
        plotOpts.setColor(SolidColor.LIGHTBLUE); // or SolidColor.LIME
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Salary earned: {point.y} " + currency);
        plotOpts.setTooltip(tooltip);
        dataSeries.setPlotOptions(plotOpts);
        return dataSeries;
    }

    private DataSeries fetchChartExpensesData(String currency) {
        var seriesItems = fetchExpensesData(currency).stream()
                .map(trip -> new DataSeriesItem(trip.getCreatedAt().toInstant(ZoneOffset.UTC), trip.getCosts()))
                .toList();
        DataSeries dataSeries = new DataSeries(seriesItems);
        dataSeries.setName("Expenses");
        PlotOptionsArea plotOpts = new PlotOptionsArea();
        plotOpts.setColor(SolidColor.LIGHTCORAL);
        SeriesTooltip tooltip = new SeriesTooltip();
        tooltip.setPointFormat("Expenses: {point.y} " + currency);
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
        gridNameColumn = grid.addColumn(WorkTripDTO::getName).setHeader("Name").setSortable(true).setComparator(WorkTripDTO::getName).setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getDescription).setHeader("Description").setSortable(true).setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getTruckName).setHeader("Truck").setSortable(true).setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getSalary).setHeader("Current salary").setAutoWidth(true);
        grid.addColumn(WorkTripDTO::getCurrency).setHeader("Currency").setAutoWidth(true);
        gridSalaryTypeColumn = grid.addComponentColumn(t -> new Badge(t.getSalaryType())).setHeader("Salary Type").setAutoWidth(true);
        gridSalaryRateColumn = grid.addColumn(new NumberRenderer<>(WorkTripDTO::getSalaryRate, "%(,.2f")).setHeader("Salary Rate").setSortable(true).setComparator(WorkTripDTO::getSalaryRate).setAutoWidth(true);
        gridActiveColumn = grid.addComponentColumn(this::mapIsActive).setHeader("Active").setSortable(true).setComparator(WorkTripDTO::isActive).setAutoWidth(true);
        grid.addColumn(trip -> trip.getCreatedAt().format(dateTimeFormatter)).setSortable(true).setHeader("Created");
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
            if (trip.getSalaryType().equals(SalaryType.PER_KM.name())) {
                menuBar.addItem("View salary", event -> navigate(event, ExpenseView.class, tripRouteParam.apply(trip.getId())));
            }
            menuBar.addItem("Edit", event -> {
                var updateModal = new TripModal(trip, false, updateGridCallBack, workTripService, truckService, currentEmployee);
                add(updateModal);
                updateModal.getDialog().open();
            });
            menuBar.addItem("Delete", event -> {
                workTripService.delete(trip.getId());
                Notification notification = Notification.show("Trip Deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGridCallBack.get();
            });
            if (trip.getClosedAt() == null) {
                MenuItem report = menuBar.addItem("Report");
                SubMenu repostSubMenu = report.getSubMenu();
                Dialog dialog = new ChoosingDialog("Chose file type");
                dialog.setMaxWidth("400px");
                repostSubMenu.addItem("Download report", e -> {

                    WorkTripEntity workTrip = reportUtil.getWorkTrip(trip.getId());
                    Map<EmployeeExpensesDTO, List<String>> expensesDTOListMap = reportUtil.getExpensesTagMap(workTrip);
                    File pdf = reportUtil.getPdfReport(workTrip, expensesDTOListMap);
                    File csv = reportUtil.getCsvFile(workTrip, expensesDTOListMap);

                    dialog.open();
                    HorizontalLayout pdfLayout = new HorizontalLayout();
                    pdfLayout.add(createFileDownloadButton(pdf, dialog));

                    pdfLayout.add(createEmailSenderButton(dialog, pdf));
                    HorizontalLayout csvLayout = new HorizontalLayout();
                    csvLayout.add(createFileDownloadButton(csv, dialog));
                    csvLayout.add(createEmailSenderButton(dialog, csv));
                    dialog.add(pdfLayout, csvLayout);

                });
            }
            if (trip.getClosedAt() == null) {
                menuBar.addItem("Close trip", e -> {
                    Double salary = salaryService.calculateSalary(trip);
                    trip.setSalary(salary.intValue());
                    trip.setClosedAt(LocalDateTime.now());
                    trip.setActive(false);
                    workTripService.saveOrUpdate(trip);
                    updateGridCallBack.get();
                });
            }
            return menuBar;
        }).setWidth("70px").setFlexGrow(0);

        grid.setAllRowsVisible(true);

        setGridDataAndMetrics(grid);

        return grid;
    }

    public void setGridDataAndMetrics(Grid<WorkTripDTO> workTripGrid) {
        List<WorkTripDTO> data = fetchData();

        if (!data.isEmpty()) {
            long total = data.size();
            long activeTrips = data.stream().filter(WorkTripDTO::isActive).count();
            double avgRate = data.stream().mapToDouble(WorkTripDTO::getSalaryRate).sum() / total;
            long allHourly = data.stream().filter(t -> t.getSalaryType().equals("HOURLY")).count();
            gridNameColumn.setFooter("All trips: %d".formatted(total));
            gridActiveColumn.setFooter("Active: %d, inactive: %d".formatted(activeTrips, total - activeTrips));
            gridSalaryRateColumn.setFooter("Avg: %.2f".formatted(avgRate));
            gridSalaryTypeColumn.setFooter("Hourly: %d, perkm: %d".formatted(allHourly, total - allHourly));
        }

        workTripGrid.setItems(data);
    }

    private Button createEmailSenderButton(Dialog dialog, File attachment) {
        Button sendFile = new Button("Send", pdfEvent -> {
            Dialog emailDialog = new Dialog();
            dialog.close();
            emailDialog.open();

            MessageInput input = new MessageInput();
            input.setWidth("400px");
            input.getI18n().setMessage("Email");
            input.addSubmitListener(e -> {
                emailUtil.sendEmailWithAttachment(e.getValue(), attachment);
                emailDialog.close();
            });
            emailDialog.add(input);
        });
        sendFile.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return sendFile;
    }

    private Anchor createFileDownloadButton(File file, Dialog dialog) {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        StreamResource streamResource = new StreamResource(file.getName(), () -> inputStream);

        Button button = new Button(String.format("%s (%d KB)", file.getName(), (int) file.length() / 1024), exc -> dialog.close());
        button.setWidth("250px");
        Anchor anchor = new Anchor(streamResource, "");
        anchor.add(button);
        anchor.getElement().setAttribute("download", true);
        return anchor;
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

    private List<WorkTripDTO> fetchExpensesData(String currency) {
        return workTripService.getAndConvertExpenses(criteriaRequest, currency);
    }

    private List<WorkTripDTO> fetchSalaryData(String currentCurrency) {
        return workTripService.getAndConvertSalaries(criteriaRequest, currentCurrency);
    }

    private List<WorkTripDTO> fetchData() {
        return workTripService.get(criteriaRequest);
    }
}
