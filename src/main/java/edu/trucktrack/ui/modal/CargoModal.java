package edu.trucktrack.ui.modal;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import edu.trucktrack.api.dto.CargoDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.EmployeeEntityRecord;
import edu.trucktrack.dao.service.CargoService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargoModal {

	private Dialog dialog;
	private FormLayout formLayout;
	private CargoService cargoService;
	Map<String, EmployeeEntityRecord> map;
	private TextField cargoName;
	private NumberField weight;
	private TextField loadingLocation;
	private DateTimePicker loadingTime;
	private TextField unloadingLocation;
	private DateTimePicker unloadingTime;
	private Select<String> drivers;
	private TextArea desc;
	private GridPro<CargoDTO> grid;
	private SearchCriteriaRequest request;

	public CargoModal(CargoService cargoService, Map<String, EmployeeEntityRecord> map, GridPro<CargoDTO> gridPro, SearchCriteriaRequest request) {
		this.cargoService = cargoService;
		this.map = map;
		this.grid = gridPro;
		this.request = request;
		createModalWindow(createClickListener());
	}

	public CargoModal(CargoService cargoService, Map<String, EmployeeEntityRecord> map, GridPro<CargoDTO> gridPro, SearchCriteriaRequest request, CargoDTO cargoDTO) {
		this.cargoService = cargoService;
		this.map = map;
		this.grid = gridPro;
		this.request = request;
		createModalWindow(updateClickListener(cargoDTO));
		cargoName.setValue(cargoDTO.getCargoName());
		weight.setValue(Double.valueOf(cargoDTO.getCargoWeight()));
		loadingLocation.setValue(cargoDTO.getLoadingLocation());
		loadingTime.setValue(cargoDTO.getLoadingTime());
		unloadingLocation.setValue(cargoDTO.getUnloadingLocation());
		unloadingTime.setValue(cargoDTO.getUnloadingTime());
		drivers.setValue(cargoDTO.getDriverName() + " [" + cargoDTO.getDriverEmail() + "]");
		desc.setValue(cargoDTO.getCargoDescription());
	}

	public Dialog getDialog() {
		return dialog;
	}

	private ComponentEventListener<ClickEvent<Button>> createClickListener() {
		return event -> {
			CargoDTO cargoDTO = CargoDTO.builder()
					.tripId(map.get(getEmail(drivers.getValue())).getTripId())
					.cargoName(cargoName.getValue())
					.cargoDescription(desc.getValue())
					.cargoWeight(weight.getValue().intValue())
					.loadingLocation(loadingLocation.getValue())
					.loadingTime(loadingTime.getValue())
					.unloadingLocation(unloadingLocation.getValue())
					.unloadingTime(unloadingTime.getValue())
					.createdAt(LocalDateTime.now())
					.build();
			cargoService.saveCargo(cargoDTO, getEmail(drivers.getValue()));
			dialog.close();
			grid.setItems(cargoService.findAllCargosForCompanyByEmail(request));
		};
	}

	private  ComponentEventListener<ClickEvent<Button>> updateClickListener(CargoDTO dto) {
		return event -> {
			CargoDTO cargoDTO = CargoDTO.builder()
					.id(dto.getId())
					.tripId(map.get(getEmail(drivers.getValue())).getTripId())
					.cargoName(cargoName.getValue())
					.cargoDescription(desc.getValue())
					.cargoWeight(weight.getValue().intValue())
					.loadingLocation(loadingLocation.getValue())
					.loadingTime(loadingTime.getValue())
					.unloadingLocation(unloadingLocation.getValue())
					.unloadingTime(unloadingTime.getValue())
					.createdAt(LocalDateTime.now())
					.build();
			cargoService.updateCargo(cargoDTO, getEmail(drivers.getValue()), dto.getDriverEmail());
			dialog.close();
			grid.setItems(cargoService.findAllCargosForCompanyByEmail(request));
		};
	}

	private void createModalWindow(ComponentEventListener<ClickEvent<Button>> clickListener) {
		dialog = new Dialog();
		dialog.setWidth("800px");
		formLayout = new FormLayout();
		cargoName = new TextField("Cargo name");
		weight = new NumberField("Weight");
		loadingLocation = new TextField("Loading address");
		loadingTime = new DateTimePicker("Loading time");
		unloadingLocation = new TextField("Unloading address");
		unloadingTime = new DateTimePicker("Unloading time");
		drivers = new Select<>();
		drivers.setLabel("Drivers");
		map.forEach((k, v) -> drivers.setItems(v.getName() + " [" + k + "]"));
		desc = new TextArea("Description");
		Button submit = new Button("Save", clickListener);

		formLayout.add(cargoName, weight, loadingLocation, unloadingLocation, loadingTime, unloadingTime, drivers, desc, submit);

		formLayout.setColspan(loadingTime, 2);
		formLayout.setColspan(unloadingTime, 2);
		formLayout.setColspan(drivers, 2);
		formLayout.setColspan(submit, 2);
		formLayout.setColspan(desc, 2);

		dialog.add(formLayout);
	}


	private String getEmail(String val) {
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(val);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

}
