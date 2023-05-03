package edu.trucktrack.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.service.WorkTripService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

@PermitAll
@RequiredArgsConstructor
@PageTitle("Your work trips")
@Route(value = "work-trip", layout = MainLayout.class)
public class WorkTripView extends VerticalLayout {

    private final SecurityUtils securityUtils;

    private final WorkTripService workTripService;

    private EmployeeEntity currentEmployee;
    private SearchCriteriaRequest criteriaRequest;

    @PostConstruct
    public void init() {
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.criteriaRequest = buildInitialCriteriaRequest();

        // TODO: Create modal for creating trips

        // Add listener for fetching and updating grid
        var searchTextField = new TextField();
        searchTextField.setPlaceholder("Search");
        searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());

        Grid<WorkTripDTO> workTripGrid = buildWorkTripGrid();

        add(new H1("This is work trip view"), new Label("Your trips"), workTripGrid);
    }

    private SearchCriteriaRequest buildInitialCriteriaRequest() {
        return SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder()
                        .companyId(currentEmployee.getId().intValue())
                        .employeeId(currentEmployee.getCompany().getId().intValue())
                        .build())
                .build();
    }

    private Grid<WorkTripDTO> buildWorkTripGrid() {
        GridPro<WorkTripDTO> grid = new GridPro<>();
        grid.addThemeVariants(GridProVariant.LUMO_COMPACT, GridProVariant.LUMO_WRAP_CELL_CONTENT);

        // TODO: Map Employee or Not?
        grid.addColumn(WorkTripDTO::getId).setHeader("ID");
        grid.addColumn(WorkTripDTO::getName).setHeader("Name");
        grid.addColumn(WorkTripDTO::getDescription).setHeader("Description");
        grid.addColumn(WorkTripDTO::getSalary).setHeader("Money");
        grid.addColumn(WorkTripDTO::getCurrencyId).setHeader("Currency");
        grid.addColumn(WorkTripDTO::isActive).setHeader("Active");
        grid.addColumn(WorkTripDTO::getCreatedAt).setHeader("Created");
        grid.addColumn(WorkTripDTO::getClosedAt).setHeader("Closed");

        grid.setItems(fetchData());

        return grid;
    }

    private List<WorkTripDTO> fetchData() {
        return workTripService.get(criteriaRequest);
    }
}
