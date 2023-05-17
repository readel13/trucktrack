package edu.trucktrack.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
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
import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.repository.jooq.TagJooqRepository;
import edu.trucktrack.dao.service.ExpensesService;
import edu.trucktrack.dao.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.ExpensesModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;


@PermitAll
@RequiredArgsConstructor
@PageTitle("Your expenses")
@Route(value = "/expense/:tripId?", layout = MainLayout.class)
public class ExpenseView extends VerticalLayout implements BeforeEnterObserver {

    public static final String TRIP_ID_PATH_VARIABLE = "tripId";
    private WorkTripDTO trip;
    private Chart chart;
    private VirtualList<EmployeeExpensesDTO> virtualList;

    private final SecurityUtils securityUtils;

    private final WorkTripService workTripService;

    private final ExpensesService expensesService;

    // TODO: wire service
    private final TagJooqRepository tagJooqRepository;

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

      this.currentCurrency =  Optional.ofNullable(trip)
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

        var searchTextField = buildSearchbar();
        var searchAndCreateLayout = new HorizontalLayout(searchTextField);
        // TODO: fix modal, if trip is not present
        if (trip != null) {
            searchAndCreateLayout.add(new ExpensesModal(null, false, trip, updateData, expensesService, securityUtils, tagJooqRepository));
        }
        searchAndCreateLayout.setAlignItems(Alignment.CENTER);

        this.chart = buildChart();

        super.setHeightFull();

        title.setText(trip == null ? "Your all expenses" : "Your expenses from '%s' trip ".formatted(trip.getName()));
        VerticalLayout verticalLayout = new VerticalLayout(title, changeCurrencyMenuBar, new Div(chart), searchAndCreateLayout, virtualList);
        verticalLayout.setAlignItems(Alignment.STRETCH);
        verticalLayout.setSizeFull();
        add(verticalLayout);
    }

    private Chart buildChart() {
        Chart chart = new Chart(ChartType.PIE);

        var currency = Optional.ofNullable(trip)
                .map(WorkTripDTO::getCurrency)
                .orElse(currentEmployee.getCurrency().getName());

        PlotOptionsPie options = new PlotOptionsPie();
        options.setSize("100%");

        Configuration configuration = chart.getConfiguration();
        configuration.setExporting(true);
        configuration.setTitle("Expenses by tags");
        configuration.setSeries(fetchChartData(chart));
        configuration.setPlotOptions(options);
        configuration.getTooltip().setPointFormat("Total by group: {point.y} %s".formatted(currency));
        chart.setSizeFull();

        return chart;
    }

    private DataSeries fetchChartData(Chart chart) {
        var convertedData = expensesService.getConvertedData(searchCriteriaRequest, currentCurrency);
        long sum = convertedData.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(EmployeeExpensesDTO::getValue)
                .sum();

        var seriesItems = convertedData
                .entrySet()
                .stream()
                .map(entry -> mapToDataSeries(entry, sum))
                .toList();

        var dataSeries = new DataSeries(seriesItems);
        chart.getConfiguration().setSubTitle("Total - %d %s".formatted(sum, currentCurrency));
        return dataSeries;
    }

    private DataSeriesItem mapToDataSeries(Map.Entry<TagDTO, List<EmployeeExpensesDTO>> entry, long allSum) {
        var sum = sum(entry.getValue());
        double percentage = ((double) sum / allSum) * 100;
        return new DataSeriesItem("%s - %.2f%%".formatted(entry.getKey().getName(), percentage), sum);
    }

    private SearchCriteriaRequest buildInitialCriteriaRequest() {
        return SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder()
                        .companyId(currentEmployee.getCompany().getId().intValue())
                        .tripId(Optional.ofNullable(trip).map(WorkTripDTO::getId).orElse(null))
                        .build())
                .build();
    }

    private ComponentRenderer<Component, EmployeeExpensesDTO> getExpenseComponentRenderer() {
        return new ComponentRenderer<>(
                expense -> {
                    HorizontalLayout cardLayout = new HorizontalLayout();
                    cardLayout.setMargin(true);
                    cardLayout.setAlignItems(Alignment.CENTER);

                    VerticalLayout infoLayout = new VerticalLayout();
                    infoLayout.setSpacing(false);
                    infoLayout.setPadding(false);

                    var salary = new Text(expense.getValue() + " " + expense.getCurrency());

                    var nameBadge = new HorizontalLayout(new H4(expense.getName()), salary);
                    nameBadge.setAlignItems(Alignment.CENTER);

                    infoLayout.add(nameBadge, new Div(new Text(expense.getDescription())), new Div(mapBadges(expense)));

                    VerticalLayout moreDetailsLayout = new VerticalLayout();
                    moreDetailsLayout.setSpacing(false);
                    moreDetailsLayout.setPadding(false);

                    moreDetailsLayout.add(createRow("From work trip: ", new Text(expense.getTrip().getName())));
                    moreDetailsLayout.add(createRow("Created at: ", new Text(expense.getCreatedAt().format(dateTimeFormatter))));

                    infoLayout.add(new Details("More details", moreDetailsLayout));

                    cardLayout.add(infoLayout);

                    var contextMenu = new ContextMenu();
                    contextMenu.setTarget(cardLayout);
                    contextMenu.addItem("Edit", event -> {
                        var expensesModal = new ExpensesModal(expense, true, trip, updateData, expensesService, securityUtils, tagJooqRepository);
                        add(expensesModal);
                        expensesModal.getDialog().open();
                    });
                    contextMenu.addItem("Delete", event -> {
                        expensesService.deleteById(expense.getId());
                        virtualList.setItems(fetchData());
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

    public VirtualList<EmployeeExpensesDTO> createVirtualList() {
        VirtualList<EmployeeExpensesDTO> list = new VirtualList<>();
        list.setRenderer(getExpenseComponentRenderer());
        list.setHeight("100%");
        list.setItems(fetchData());
        return list;
    }

    private TextField buildSearchbar() {
        var searchTextField = new TextField();
        searchTextField.setPlaceholder("Search");
        searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchTextField.addValueChangeListener(event -> {
            searchCriteriaRequest.getFilterBy().setName(event.getValue());
            updateData.get();
        });
        searchTextField.setWidth("800px");
        return searchTextField;
    }

    public List<EmployeeExpensesDTO> fetchData() {
        return expensesService.get(searchCriteriaRequest);
    }

    private Span mapBadges(EmployeeExpensesDTO expensesDTO) {
        var badges = expensesDTO.getTags().stream().map(TagDTO::getName).map(Badge::new).toArray(Badge[]::new);
        return new Span(badges);
    }

    private Long sum(List<EmployeeExpensesDTO> expenses) {
        return expenses.stream()
                .mapToLong(EmployeeExpensesDTO::getValue)
                .sum();
    }
}
