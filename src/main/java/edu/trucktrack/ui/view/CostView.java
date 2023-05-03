package edu.trucktrack.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.trucktrack.api.dto.CostDTO;
import edu.trucktrack.service.CurrencyService;
import edu.trucktrack.ui.modal.AddCostModal;
import edu.trucktrack.ui.MainLayout;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@PermitAll
@RequiredArgsConstructor
@PageTitle("Your Costs")
@Route(value = "cost", layout = MainLayout.class)
public class CostView extends VerticalLayout {

    private Grid<CostDTO> grid;

    private final CurrencyService currencyService;

    @PostConstruct
    public void init() {
        var currencies = currencyService.getAll();

        var title = new H1("Cost page");

        var addCostModal = new AddCostModal(currencies);

        TextField searchTextField = buildSearchbar();

        var searchAndCreateLayout = new HorizontalLayout(searchTextField, addCostModal);
        searchAndCreateLayout.setAlignItems(Alignment.CENTER);

        this.grid = buildCostGrid();

        add(title, searchAndCreateLayout, grid);
    }

    private TextField buildSearchbar() {
        var searchTextField = new TextField();
        searchTextField.setPlaceholder("Search");
        searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchTextField.addValueChangeListener(event -> {
            grid.setItems(fetchCosts().stream().filter(costDTO -> costDTO.getName().contains(event.getValue())).toList());
        });
        searchTextField.setWidth("800px");
        return searchTextField;
    }

    private Grid<CostDTO> buildCostGrid() {
        GridPro<CostDTO> grid = new GridPro<>();
        grid.addThemeVariants(GridProVariant.LUMO_COMPACT, GridProVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(CostDTO::getId).setHeader("ID");
        grid.addColumn(CostDTO::getName).setHeader("Name");
        grid.addColumn(CostDTO::getDescription).setHeader("Description");
        grid.addColumn(CostDTO::getAmount).setHeader("Money");
        grid.addColumn(CostDTO::getCurrencyAmount).setHeader("Currency");

        grid.addComponentColumn(this::mapBadges).setHeader("Tags").setAutoWidth(true);

        // TODO: implement fetching real data
        grid.setItems(fetchCosts());

        return grid;
    }

    private List<CostDTO> fetchCosts() {
        return List.of(CostDTO.builder().id(1L)
                        .name("Milk")
                        .description("Very good milk from Molokia")
                        .amount(BigDecimal.valueOf(2000))
                        .currencyAmount("USD").badges(Set.of("Travel", "Food"))
                        .build(),
                CostDTO.builder().id(2L)
                        .name("Butter")
                        .description("Bought butter from ATB")
                        .amount(BigDecimal.valueOf(21))
                        .currencyAmount("USD").badges(Set.of("Sales"))
                        .build(),
                CostDTO.builder().id(3L)
                        .name("Water")
                        .description("Morshynska")
                        .amount(BigDecimal.valueOf(33))
                        .currencyAmount("USD").badges(Set.of("Other"))
                        .build(),
                CostDTO.builder().id(4L)
                        .name("Sprite")
                        .description("Not cola")
                        .amount(BigDecimal.valueOf(10))
                        .currencyAmount("USD").badges(Set.of("Work"))
                        .build());
    }


    private Span mapBadges(CostDTO costDTO) {
        var badges = costDTO.getBadges().stream().map(Badge::new).toArray(Badge[]::new);

        return new Span(badges);
    }
}
