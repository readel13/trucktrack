package edu.trucktrack.ui.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.trucktrack.dao.entity.CompanyEntity;
import edu.trucktrack.dao.repository.jpa.CompanyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Route("register/company")
@PageTitle("Register User")
@AnonymousAllowed
public class RegisterCompanyView extends VerticalLayout {

	private CompanyJpaRepository companyJpaRepository;

	@Autowired
	public RegisterCompanyView(CompanyJpaRepository companyJpaRepository) {
		this.companyJpaRepository = companyJpaRepository;
		addClassName("login-view");
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		FormLayout registerFrom = new FormLayout();
		setHorizontalComponentAlignment(Alignment.CENTER, registerFrom);

		H3 label = new H3("Register Company");
		TextField username = new TextField("Company name");
		EmailField email = new EmailField("Email");
		TextField url = new TextField("Company url");
		TextField address = new TextField("Company address");
		TextField zipCode = new TextField("zip code");
		TextArea description = new TextArea("Description");

		Button register = new Button("Register Company",
				event -> registerCompany(
						username.getValue(),
						email.getValue(),
						description.getValue(),
						url.getValue(),
						address.getValue(),
						zipCode.getValue()));

		registerFrom.setMaxWidth("500px");
		registerFrom.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
				new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));


		registerFrom.add(
				label, username, email, url, zipCode, address, description, register);
		registerFrom.setColspan(address, 2);
		registerFrom.setColspan(description, 2);
		registerFrom.setColspan(label, 2);
		registerFrom.setColspan(register, 2);
		add(registerFrom);

	}

	private void registerCompany(String name, String email, String description, String url, String address, String zipCode) {
		CompanyEntity company = companyJpaRepository.save(
				CompanyEntity.builder()
						.name(name)
						.email(email)
						.description(description)
						.url(url)
						.address(address)
						.zipcode(zipCode)
						.createdAt(LocalDateTime.now())
						.build());

		if (company != null) {
			Notification.show("company successful created");
			HashMap<String, List<String>> queryParams = new HashMap<>();
			queryParams.put("companyId", Collections.singletonList(company.getId().toString()));
			UI.getCurrent().navigate(RegisterUserView.class, new QueryParameters(queryParams));
		}
	}
}
