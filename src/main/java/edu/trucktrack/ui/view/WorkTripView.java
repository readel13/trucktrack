package edu.trucktrack.ui.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.service.TruckService;
import edu.trucktrack.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.TripModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

        Grid<WorkTripDTO> workTripGrid = buildWorkTripGrid();
        this.updateGridCallBack = () -> workTripGrid.setItems(fetchData());

        var addTripModal = new TripModal(null, true, updateGridCallBack, workTripService, truckService, securityUtils);

        var searchTextField = new TextField();
        searchTextField.setWidth("800px");
        searchTextField.setPlaceholder("Search");
        searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchTextField.addValueChangeListener(event -> {
            criteriaRequest.getFilterBy().setName(event.getValue());
            updateGridCallBack.get();
        });

        HorizontalLayout searchAndAdd = new HorizontalLayout(searchTextField, addTripModal);
        searchAndAdd.setAlignItems(Alignment.CENTER);

        add(new H1("Your trips"), searchAndAdd, workTripGrid);
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
        grid.addColumn(WorkTripDTO::getCurrency).setHeader("Currency").setAutoWidth(true);
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
            menuBar.addItem("View costs", event -> navigate(event, CostView.class, tripRouteParam.apply(trip.getId())));
            menuBar.addItem("View salary", event -> navigate(event, CostView.class, tripRouteParam.apply(trip.getId())));
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

    public List<WorkTripDTO> fetchFakeData() {
        return List.of(
                WorkTripDTO.builder()
                        .id(1L)
                        .name("Viva la France")
                        .description("Bon vouge from France")
                        .truckId(1L)
                        .truckName("Volvo")
                        .salary(1000)
                        .currency("USD")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .closedAt(LocalDateTime.now())
                        .build(),
                WorkTripDTO.builder()
                        .id(2L)
                        .name("Good Sweden!")
                        .description("Ola")
                        .truckId(2L)
                        .truckName("Volvo")
                        .salary(1000)
                        .currency("USD")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .closedAt(LocalDateTime.now())
                        .build(),
                WorkTripDTO.builder()
                        .id(5L)
                        .name("Trip to China")
                        .description("Made in China")
                        .truckId(5L)
                        .truckName("Volvo")
                        .salary(1000)
                        .currency("USD")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .closedAt(LocalDateTime.now())
                        .build(),
                WorkTripDTO.builder()
                        .id(8L)
                        .name("Viva la France")
                        .description("Bon vouge from France")
                        .truckId(8L)
                        .truckName("Volvo")
                        .salary(1000)
                        .currency("USD")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .closedAt(LocalDateTime.now())
                        .build()
        );
    }

    private List<WorkTripDTO> fetchData() {
        return workTripService.get(criteriaRequest);
    }
}
