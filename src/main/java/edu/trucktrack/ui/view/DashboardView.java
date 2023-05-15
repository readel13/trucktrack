package edu.trucktrack.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.CompanyEntity;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.TripModal;
import edu.trucktrack.ui.modal.TruckModal;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.vaadin.addons.badge.Badge;

import java.util.List;
import java.util.function.Supplier;

@PermitAll
@PageTitle("Company Dashboard")
@RouteAlias(value = "", layout = MainLayout.class)
@RouteAlias(value = "/home", layout = MainLayout.class)
@Route(value = "dashboard", layout = MainLayout.class)
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout {

    private final TruckService truckService;

    private final SecurityUtils securityUtils;

    private Grid<TruckDTO> grid;

    private SearchCriteriaRequest criteriaRequest;

    private EmployeeEntity currentEmployee;

    @PostConstruct
    public void init() {
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.criteriaRequest = buildInitialCriteriaRequest();

        var mainInfoLayout = buildCompanyInfoLayout(currentEmployee.getCompany());

        Supplier<Object> updateListCallback = () -> grid.setItems(fetchTruckData());

        var truckModal = new TruckModal(null, false, truckService, updateListCallback, securityUtils);

        var truckGridTitle = new H3("Available trucks company");
        var titleButtonLayout = new HorizontalLayout(new Div(truckGridTitle), new Div(truckModal));
        titleButtonLayout.setAlignItems(Alignment.STRETCH);
        titleButtonLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        this.grid = buildTruckGrid();
        var contentLayout = new VerticalLayout();
        contentLayout.add(titleButtonLayout);
        contentLayout.add(grid);

        add(mainInfoLayout, contentLayout);
    }

    private SearchCriteriaRequest buildInitialCriteriaRequest() {
        return SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder()
                        //.employeeId(currentEmployee.getId().intValue())
                        .companyId(currentEmployee.getCompany().getId().intValue())
                        .build())
                .build();
    }

    private Grid<TruckDTO> buildTruckGrid() {
        Grid<TruckDTO> grid = new Grid<>();
        grid.addColumn(TruckDTO::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(TruckDTO::getTruckNumber).setHeader("Truck number").setAutoWidth(true);
        grid.addColumn(TruckDTO::getVinCode).setHeader("VinCode").setAutoWidth(true);
        grid.addColumn(TruckDTO::getFuelConsumption).setHeader("Fuel Consumption").setAutoWidth(true);
        grid.addComponentColumn(t -> mapBadge(t.isActive())).setHeader("Active").setAutoWidth(true);
        grid.addComponentColumn(t -> mapBadge(t.isAvailable())).setHeader("Available").setAutoWidth(true);
        grid.addColumn(TruckDTO::getCreatedAt).setHeader("Created At").setAutoWidth(true);
        grid.addComponentColumn(truck -> {
            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
            menuBar.addItem("Edit", event -> {
                var updateModal = new TruckModal(truck, true, truckService, () -> grid.setItems(fetchTruckData()), securityUtils);
                add(updateModal);
                updateModal.getDialog().open();
            });
            menuBar.addItem("Delete", event -> {
                truckService.deleteById(truck.getId());
                Notification notification = Notification.show("Truck Deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                grid.setItems(fetchTruckData());
            });
            return menuBar;
        }).setWidth("70px").setFlexGrow(0);

        grid.setItems(fetchTruckData());
        return grid;
    }

    private List<TruckDTO> fetchTruckData() {
        return truckService.getAll(criteriaRequest);
    }

    private static VerticalLayout buildCompanyInfoLayout(CompanyEntity company) {
        var companyTitle = new H2(company.getName());
        var email = new Label("Email: " + company.getEmail());
        var asOf = new Label("Company as of " + company.getCreatedAt().toLocalDate());
        var companyDescription = new Label(company.getDescription());

        HorizontalLayout titleEmail = new HorizontalLayout(companyTitle);
        titleEmail.setAlignItems(Alignment.CENTER);

        var mainInfoLayout = new VerticalLayout(titleEmail, asOf, email, companyDescription);

        mainInfoLayout.getStyle().set("background-color", "#f2f2f2");
        mainInfoLayout.getStyle().set("border", "1px solid #bfbfbf");
        mainInfoLayout.getStyle().set("border-radius", "3px");
        return mainInfoLayout;
    }

    public Badge mapBadge(boolean value) {
        Badge badge = new Badge(value ? "Yes" : "No");
        badge.setVariant(value ? Badge.BadgeVariant.SUCCESS : Badge.BadgeVariant.ERROR);
        return badge;
    }
}
