package edu.trucktrack.ui.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.util.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

@PermitAll
@RequiredArgsConstructor
@Route(value = "/settings", layout = MainLayout.class)
public class SettingView extends VerticalLayout {

    private final SecurityUtils securityUtils;

    private final TextField name = new TextField("Change your name", "name");

    @PostConstruct
    public void init () {
        H1 title = new H1("Your settings");

        add(title, name);
    }
}
