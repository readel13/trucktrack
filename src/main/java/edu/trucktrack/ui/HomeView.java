package edu.trucktrack.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Route("/ui")
@PermitAll
public class HomeView extends AppLayout {

    private final transient AuthenticationContext authContext;

    public HomeView(AuthenticationContext authContext) {
        this.authContext = authContext;

        Tab tab = new Tab("tab1");

        GridPro<MyRecord> grid = new GridPro<>();
        grid.addColumn(MyRecord::id).setHeader("ID");
        grid.addColumn(MyRecord::name).setHeader("Name");

        List<MyRecord> myRecords = List.of(new MyRecord(1, "Artur"), new MyRecord(2, "ASDASD"));
        grid.setItems(myRecords);
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.add(new H1("We are the champions!"));
        contentLayout.add(grid);

        setContent(contentLayout);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("TruckTrack");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title);
        addToDrawer(getTabs());

        authContext.getAuthenticatedUser(UserDetails.class)
                .ifPresent(user -> {
                    Button logout = new Button("Logout", click -> this.authContext.logout());
                    addToNavbar(logout);
                });
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DASHBOARD, "Dashboard", HomeView.class),
                createTab(VaadinIcon.CART, "Orders", AnalyticsView.class),
                createTab(VaadinIcon.USER_HEART, "Customers", AnalyticsView.class),
                createTab(VaadinIcon.PACKAGE, "Products", AnalyticsView.class),
                createTab(VaadinIcon.RECORDS, "Documents", AnalyticsView.class),
                createTab(VaadinIcon.LIST, "Tasks", AnalyticsView.class),
                createTab(VaadinIcon.CHART, "Analytics", AnalyticsView.class));
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
        // Demo has no routes


        link.setRoute(clazz);

        link.setTabIndex(-1);

        return new Tab(link);
    }


    public record MyRecord(Integer id, String name) {
    }
}
