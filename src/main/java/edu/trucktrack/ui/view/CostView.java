package edu.trucktrack.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.trucktrack.api.dto.CostDTO;
import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.repository.jooq.TagJooqRepository;
import edu.trucktrack.service.ExpensesService;
import edu.trucktrack.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.AddCostModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;


@PermitAll
@RequiredArgsConstructor
@PageTitle("Your Costs")
@Route(value = "/cost/:tripId?", layout = MainLayout.class)
public class CostView extends VerticalLayout implements BeforeEnterObserver {

    private WorkTripDTO trip;
    private VirtualList<EmployeeExpensesDTO> virtualList;

    private final SecurityUtils securityUtils;

    private final WorkTripService workTripService;

    private final ExpensesService expensesService;

    // TODO: wire service
    private final TagJooqRepository tagJooqRepository;

    private EmployeeEntity currentEmployee;

    private SearchCriteriaRequest searchCriteriaRequest;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final H1 title = new H1();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var tripId = event.getRouteParameters().get("tripId").map(Long::valueOf).orElse(null);
        this.trip = workTripService.getById(tripId);
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.searchCriteriaRequest = buildInitialCriteriaRequest();
        this.virtualList = createVirtualList();

        Supplier<Object> updateListCallback = () -> {
            virtualList.setItems(fetchData());
            return null;
        };

        var addCostModal = new AddCostModal(trip, updateListCallback, expensesService, securityUtils, tagJooqRepository);

        var searchTextField = buildSearchbar();
        var searchAndCreateLayout = new HorizontalLayout(searchTextField, addCostModal);
        searchAndCreateLayout.setAlignItems(Alignment.CENTER);

        super.setHeightFull();

        title.setText(tripId == null ? "Your all costs" : "Your costs from %s trip ".formatted(trip.getName()));
        add(title, searchAndCreateLayout, virtualList);
    }

    private SearchCriteriaRequest buildInitialCriteriaRequest() {
        return SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder()
                        .companyId(currentEmployee.getCompany().getId().intValue())
                        .tripId(Optional.ofNullable(trip).map(WorkTripDTO::getId).orElse(0L))
                        .build())
                .build();
    }

    private final ComponentRenderer<Component, EmployeeExpensesDTO> costComponentRenderer = new ComponentRenderer<>(
            cost -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);

                var salary = new Text(cost.getValue() + " " + cost.getCurrency());

                var nameBadge = new HorizontalLayout(new H4(cost.getName()), salary);
                nameBadge.setAlignItems(Alignment.CENTER);

                infoLayout.add(nameBadge, new Div(new Text(cost.getDescription())), new Div(mapBadges(cost)));

                VerticalLayout moreDetailsLayout = new VerticalLayout();
                moreDetailsLayout.setSpacing(false);
                moreDetailsLayout.setPadding(false);

                moreDetailsLayout.add(createRow("From work trip: ", new Text(cost.getTrip().getName())));
                moreDetailsLayout.add(createRow("Created at: ", new Text(cost.getCreatedAt().format(dateTimeFormatter))));

                infoLayout.add(new Details("More details", moreDetailsLayout));

                cardLayout.add(infoLayout);
                return cardLayout;
            });

    private Div createRow(String title, Component... components) {
        var rowLayout = new HorizontalLayout(new H5(title));
        rowLayout.add(components);
        rowLayout.setAlignItems(Alignment.CENTER);
        return new Div(rowLayout);
    }

    public VirtualList<EmployeeExpensesDTO> createVirtualList() {
        VirtualList<EmployeeExpensesDTO> list = new VirtualList<>();
        list.setRenderer(costComponentRenderer);
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
            virtualList.setItems(fetchData());
        });
        searchTextField.setWidth("800px");
        return searchTextField;
    }

    public List<EmployeeExpensesDTO> fetchData() {
        return expensesService.get(searchCriteriaRequest);
    }

    private List<CostDTO> fetchFakeCosts() {
        return List.of(CostDTO.builder().id(1L)
                        .name("Milk")
                        .description("Very good milk from Molokia")
                        .value(BigDecimal.valueOf(2000))
                        .valueCurrency("USD")
                        .badges(Set.of("Travel", "Food"))
                        .tripName("Trip #1")
                        .createdAt(LocalDateTime.now())
                        .build(),
                CostDTO.builder().id(2L)
                        .name("Butter")
                        .description("Bought butter from ATB")
                        .value(BigDecimal.valueOf(21))
                        .valueCurrency("USD")
                        .badges(Set.of("Sales"))
                        .tripName("Trip #3")
                        .createdAt(LocalDateTime.now())
                        .build(),
                CostDTO.builder().id(3L)
                        .name("Water")
                        .description("Morshynska")
                        .value(BigDecimal.valueOf(33))
                        .valueCurrency("USD")
                        .badges(Set.of("Other"))
                        .tripName("Trip #3")
                        .createdAt(LocalDateTime.now())
                        .build(),
                CostDTO.builder().id(4L)
                        .name("Sprite")
                        .description("Not cola")
                        .value(BigDecimal.valueOf(10))
                        .valueCurrency("USD")
                        .badges(Set.of("Work"))
                        .tripName("Trip #5")
                        .createdAt(LocalDateTime.now())
                        .build());
    }

    private Span mapBadges(EmployeeExpensesDTO costDTO) {
        var badges = costDTO.getTags().stream().map(TagDTO::getName).map(Badge::new).toArray(Badge[]::new);
        return new Span(badges);
    }
}
