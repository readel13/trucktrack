package edu.trucktrack.ui.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import edu.trucktrack.ui.MainLayout;
import jakarta.annotation.security.PermitAll;


@PermitAll
@Route(value = "task", layout = MainLayout.class)
public class TaskView extends VerticalLayout {
    public TaskView() {
        H1 title = new H1("This task view");
        add(title);
    }
}
