package edu.trucktrack.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.trucktrack.api.dto.CargoDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.EmployeeEntityRecord;
import edu.trucktrack.dao.service.CargoService;
import edu.trucktrack.ui.MainLayout;
import edu.trucktrack.ui.modal.CargoModal;
import edu.trucktrack.ui.modal.TripModal;
import edu.trucktrack.util.Names;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@RequiredArgsConstructor
@PageTitle("Your Cargos")
@Route(value = "/cargo/:tripId?", layout = MainLayout.class)
public class CargoView extends VerticalLayout {

	private final CargoService cargoService;

	private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	private SearchCriteriaRequest request = SearchCriteriaRequest.builder().filterBy(FilterBy.builder().email(authentication.getName()).build()).build();

	private CargosFilter filterBy;

	private Select<String> select = new Select<>();

	private GridPro<CargoDTO> grid = new GridPro<>();

	@PostConstruct
	public void init() {
		grid = new GridPro<>();
		grid.addThemeVariants(GridProVariant.LUMO_COMPACT, GridProVariant.LUMO_WRAP_CELL_CONTENT);
		grid.addColumn(CargoDTO::getCargoName).setHeader("Cargo name").setAutoWidth(true);
		grid.addColumn(CargoDTO::getDriverName).setHeader("Driver name").setAutoWidth(true);
		grid.addColumn(CargoDTO::getCargoDescription).setHeader("Description").setAutoWidth(true);
		grid.addColumn(CargoDTO::getCargoWeight).setHeader("Weight").setAutoWidth(true);
		grid.addColumn(CargoDTO::getLoadingLocation).setHeader("Loading address").setAutoWidth(true);
		grid.addColumn(CargoDTO::getUnloadingLocation).setHeader("Unloading address").setAutoWidth(true);
		grid.addColumn(CargoDTO::getLoadingTime).setHeader("Loading time").setAutoWidth(true);
		grid.addColumn(CargoDTO::getUnloadingTime).setHeader("Unloading time").setAutoWidth(true);
		grid.addColumn(CargoDTO::getDistance).setHeader("Distance").setAutoWidth(true);
		grid.addColumn(CargoDTO::getDeliveredAt).setHeader("Delivered at").setAutoWidth(true);

		grid.addComponentColumn(cargo -> {
			MenuBar menuBar = new MenuBar();
			menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
			menuBar.addItem("Edit", e -> {
				Dialog dialog = updateCargo(cargo);
				this.add(dialog);
				dialog.open();
			});
			menuBar.addItem("Delete", e -> {
				cargoService.deleteCargo(cargo);
				addDataToTable(grid);
			});
			menuBar.addItem("Close cargo", e -> {
				cargoService.closeCargo(cargo);
			});
			return menuBar;
		});

		addDataToTable(grid);

		var searchTextField = new TextField();
		searchTextField.setWidth("400px");
		searchTextField.setPlaceholder("Search");
		searchTextField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchTextField.addValueChangeListener(event -> {
			switch (filterBy) {
				case CARGO -> request.getFilterBy().setName(event.getValue());
				case DRIVER -> request.getFilterBy().setEmployeeName(event.getValue());
				case LOADING_ADDRESS -> request.getFilterBy().setLoadingLocation(event.getValue());
				case UNLOADING_ADDRESS -> request.getFilterBy().setUnloadingLocation(event.getValue());
			}
			if (!StringUtils.isBlank(event.getValue())) {
				addDataToTable(grid);
			} else {
				request.setFilterBy(FilterBy.builder().email(authentication.getName()).build());
				addDataToTable(grid);
			}
		});

		select.setItems(CargosFilter.CARGO.getFilterName(), CargosFilter.DRIVER.getFilterName(), CargosFilter.LOADING_ADDRESS.getFilterName(), CargosFilter.UNLOADING_ADDRESS.getFilterName());
		//select.setValue("Filter By");
		select.addValueChangeListener(e -> {
			request.setFilterBy(FilterBy.builder().email(authentication.getName()).build());
			filterBy = CargosFilter.getCargoFilter(e.getValue());
		});

		Button create = new Button("Create");
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		create.addClickListener(e -> {
			Dialog dialog = createCargo();
			this.add(dialog);
			dialog.open();
		});

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.add(searchTextField, select, create);
		this.add(horizontalLayout);
		this.add(grid);
	}

	private void addDataToTable(GridPro<CargoDTO> gridPro) {
		List<CargoDTO> cargos = cargoService.findAllCargosForCompanyByEmail(request);
		gridPro.setItems(cargos);
	}

	private enum CargosFilter {
		DRIVER("Driver name"), CARGO("Cargo name"), LOADING_ADDRESS("Load address"), UNLOADING_ADDRESS("Unload address");

		private final String filterName;

		CargosFilter(String filterName) {
			this.filterName = filterName;
		}

		public String getFilterName() {
			return filterName;
		}

		public static CargosFilter getCargoFilter(String filterName) {
			return Arrays.stream(CargosFilter.values()).filter(item -> filterName.equals(item.getFilterName())).findFirst().get();
		}
	}

	private Dialog createCargo() {
		Map<String, EmployeeEntityRecord> map = cargoService.getEmployeeForCreatingCargo(request).stream().collect(Collectors.toMap(EmployeeEntityRecord::getEmail, v -> v));
		CargoModal cargoModal = new CargoModal(cargoService, map, grid, request);
		return cargoModal.getDialog();
	}

	private Dialog updateCargo(CargoDTO dto) {
		Map<String, EmployeeEntityRecord> map = cargoService.getEmployeeForCreatingCargo(request).stream().collect(Collectors.toMap(EmployeeEntityRecord::getEmail, v -> v));
		CargoModal cargoModal = new CargoModal(cargoService, map, grid, request, dto);
		return cargoModal.getDialog();
	}
}


