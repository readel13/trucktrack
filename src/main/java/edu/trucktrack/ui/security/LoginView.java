package edu.trucktrack.ui.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private LoginForm login = new LoginForm();

	public LoginView() {
		addClassName("login-view");
		setSizeFull();

		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);


		login.setAction("login");

		Dialog registerTypeChose = new Dialog();
		registerTypeChose.setHeaderTitle("Please select user type");
		FormLayout formLayout = new FormLayout();
		Button defaultUser = new Button("Driver/Manager");
		defaultUser.setIcon(new Icon(VaadinIcon.USER));
		Button owner = new Button("Owner");
		owner.setIcon(new Icon(VaadinIcon.USERS));
		formLayout.add(defaultUser, owner);
		registerTypeChose.add(formLayout);

		defaultUser.addClickListener(e -> {
			UI.getCurrent().navigate(RegisterUserView.class);
			registerTypeChose.close();
		});

		owner.addClickListener(e -> {
			UI.getCurrent().navigate(RegisterCompanyView.class);
			registerTypeChose.close();
		});

		add(new H1("Login"), login, new Button("Register", e -> registerTypeChose.open()));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if(beforeEnterEvent.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			login.setError(true);
		}
	}
}
