package edu.trucktrack.ui.security;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.trucktrack.configuration.SecurityConfig;
import edu.trucktrack.entity.CompanyEntity;
import edu.trucktrack.entity.CurrencyEntity;
import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.entity.RoleEntity;
import edu.trucktrack.repository.jpa.CompanyJpaRepository;
import edu.trucktrack.repository.jpa.CurrencyJpaRepository;
import edu.trucktrack.repository.jpa.RoleJpaRepository;
import edu.trucktrack.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

	private EmployeeService employeeService;
	private CurrencyJpaRepository currencyJpaRepository;
	private RoleJpaRepository roleJpaRepository;
	private CompanyJpaRepository companyJpaRepository;

	@Autowired
	public RegisterView(EmployeeService employeeService, CurrencyJpaRepository currencyJpaRepository, RoleJpaRepository roleJpaRepository, CompanyJpaRepository companyJpaRepository) {
		this.employeeService = employeeService;
		this.currencyJpaRepository = currencyJpaRepository;
		this.companyJpaRepository = companyJpaRepository;
		this.roleJpaRepository = roleJpaRepository;

		addClassName("login-view");
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		Set<String> selectedRoles = new HashSet<>();
		List<CurrencyEntity> allCurrencies = currencyJpaRepository.findAll();
		List<CompanyEntity> allCompanies = companyJpaRepository.findAll();
		AtomicReference<String> selectedCurrency = new AtomicReference<>("");
		AtomicReference<String> selectedCompany = new AtomicReference<>("");

		FormLayout registerFrom = new FormLayout();
		setHorizontalComponentAlignment(Alignment.CENTER, registerFrom);

		H3 label = new H3("Register");
		TextField username = new TextField("Username");

		MultiSelectComboBox<String> roles = new MultiSelectComboBox<>();
		roles.setItems(roleJpaRepository.findAll().stream().map(RoleEntity::getRole).collect(Collectors.toList()));
		roles.addValueChangeListener(event -> {
			selectedRoles.addAll(event.getValue());
		});
		roles.setLabel("Roles");

		Select<String> currency = new Select<>();
		currency.setItems(allCurrencies.stream().map(CurrencyEntity::getName).collect(Collectors.toList()));
		currency.setLabel("Currency");
		currency.addValueChangeListener(event -> selectedCurrency.set(event.getValue()));

		Select<String> companies = new Select<>();
		companies.setItems(allCompanies.stream().map(CompanyEntity::getName).collect(Collectors.toList()));
		companies.setLabel("Company");
		companies.addValueChangeListener(event -> selectedCompany.set(event.getValue()));

		EmailField email = new EmailField("Email");
		PasswordField password = new PasswordField("Password");
		TextField phone = new TextField("Phone");

		registerFrom.setMaxWidth("500px");
		setRequiredIndicatorVisible(username,
				email,
				roles,
				currency,
				companies,
				phone,
				password);

		Button registerButton = new Button("Register",
				event -> register(
						username.getValue(),
						email.getValue(),
						selectedRoles,
						phone.getValue(),
						password.getValue(),
						allCurrencies.stream().filter(item -> selectedCurrency.get().equals(item.getName())).findFirst().get(),
						allCompanies.stream().filter(item -> selectedCompany.get().equals(item.getName())).findFirst().get()
						));
		registerFrom.add(
				label,
				username,
				email,
				roles,
				currency,
				companies,
				phone,
				password,
				registerButton);
		add(registerFrom);

		registerFrom.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
				new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));


		registerFrom.setColspan(roles, 2);
		registerFrom.setColspan(label, 2);
		registerFrom.setColspan(registerButton, 2);
	}

	private void register(String username, String email, Set<String> roles, String phone, String password, CurrencyEntity currency, CompanyEntity company) {
		EmployeeEntity employee = employeeService.save(
				EmployeeEntity.builder()
						.name(username)
						.email(email)
						.roles(new HashSet<>(roleJpaRepository.findByRolesIn(roles.stream().toList())))
						.phoneNumber(phone)
						.password(SecurityConfig.passwordEncoder().encode(password))
						.active(true)
						.company(company)
						.currency(currency)
						.createdAt(LocalDateTime.now())
						.build());
		if (employee != null) {
			Notification.show("register successful");
			UI.getCurrent().navigate(LoginView.class);
		}

	}

	private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
		Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
	}
}
