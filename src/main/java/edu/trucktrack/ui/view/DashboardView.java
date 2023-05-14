package edu.trucktrack.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.dao.entity.CompanyEntity;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

@PermitAll
@PageTitle("Company Dashboard")
@RouteAlias(value = "", layout = MainLayout.class)
@RouteAlias(value = "/home", layout = MainLayout.class)
@Route(value = "dashboard", layout = MainLayout.class)
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout {

    private final SecurityUtils securityUtils;
    private final TruckService truckService;

    private EmployeeEntity currentEmployee;

    @PostConstruct
    public void init() {
        this.currentEmployee = securityUtils.getCurrentEmployee();
        CompanyEntity company = currentEmployee.getCompany();

        var mainInfoLayout = buildCompanyInfoLayout(company);

        var truckGridTitle = new H3("Available trucks company");
        var createNewTruckButton = new Button("Create new");
        var titleButtonLayout = new HorizontalLayout(truckGridTitle, createNewTruckButton);
        titleButtonLayout.setAlignItems(Alignment.AUTO);

        Grid<TruckDTO> grid = buildTruckGrid();
        var contentLayout = new VerticalLayout();
        contentLayout.add(titleButtonLayout);
        contentLayout.add(grid);

        add(mainInfoLayout, contentLayout);
    }

    private Grid<TruckDTO> buildTruckGrid() {
        // TODO: use company name instead of company id
        // TODO: add avaiable badge
        Grid<TruckDTO> grid = new Grid<>();
        grid.addColumn(TruckDTO::getId).setHeader("ID");
        grid.addColumn(TruckDTO::getCompanyId).setHeader("Company ID");
        grid.addColumn(TruckDTO::getName).setHeader("Name");
        grid.addColumn(TruckDTO::getTruckNumber).setHeader("Truck number");
        grid.addColumn(TruckDTO::getVinCode).setHeader("VinCode");
        grid.addColumn(TruckDTO::getFuelConsumption).setHeader("Fuel Consumption");
        grid.addColumn(TruckDTO::isActive).setHeader("Active");
        grid.addColumn(TruckDTO::getCreatedAt).setHeader("Created At");

        // TODO: fetch data by current company of user
        grid.setItems(truckService.getAll());
        return grid;
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

}
