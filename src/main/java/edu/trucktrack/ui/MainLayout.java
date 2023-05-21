package edu.trucktrack.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import edu.trucktrack.ui.view.DashboardView;
import edu.trucktrack.ui.view.EmployeeView;
import edu.trucktrack.ui.view.ExpenseView;
import edu.trucktrack.ui.view.MapView;
import edu.trucktrack.ui.view.SettingView;
import edu.trucktrack.ui.view.WorkTripView;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

@PermitAll
@Route("/ui")
public class MainLayout extends AppLayout {

    private final SecurityUtils securityUtils;
    private final transient AuthenticationContext authContext;

    public MainLayout(AuthenticationContext authContext, SecurityUtils securityUtils) {
        this.authContext = authContext;
        this.securityUtils = securityUtils;

        var toggle = new DrawerToggle();

        var title = new H1("TruckTrack");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title);
        addToDrawer(getTabs());

        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> addLogoutButtonToNavBar());
    }

    private void addLogoutButtonToNavBar() {
        var logoutWrapper = new Div();
        var logoutButton = new Button("Logout", click -> this.authContext.logout());
        logoutWrapper.add(logoutButton);
        logoutWrapper.getStyle().set("margin-left", "auto");
        logoutWrapper.getStyle().set("padding", "5px");

        addToNavbar(logoutWrapper);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DASHBOARD, "Dashboard", DashboardView.class),
                createTab(VaadinIcon.MAP_MARKER, "Map", MapView.class),
                createTab(VaadinIcon.ROAD, "WorkTrips", WorkTripView.class),
                createTab(VaadinIcon.MONEY_WITHDRAW, "Expenses", ExpenseView.class)
        );

        if (securityUtils.getCurrentEmployee().isManagerOrOwner()) {
            tabs.add(createTab(VaadinIcon.USER_CARD, "Employees", EmployeeView.class));
        }
        tabs.add(createTab(VaadinIcon.HAMMER, "Settings", SettingView.class));

        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class clazz) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));

        link.setRoute(clazz);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}
