package edu.trucktrack.ui.modal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import edu.trucktrack.api.dto.SimpleEmployeeDTO;
import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.entity.enums.SalaryType;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.dao.service.WorkTripService;
import edu.trucktrack.util.SecurityUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDateTime;
import java.util.function.Supplier;

@Slf4j
@Getter
public class TripModal extends VerticalLayout {

    private final TruckService truckService;
    private final WorkTripService workTripService;
    private final SecurityUtils securityUtils;

    private final Supplier<Object> updateGridCallback;

    private final EmployeeEntity currentEmployee;

    private final boolean create;

    private final Checkbox active = new Checkbox("Active");
    private final TextField name = new TextField("Name", "Name of trip");
    private final TextField description = new TextField("Description", "Description of trip");
    private final Select<TruckDTO> truck = new Select<>();
    private final Select<String> currency = new Select<>();
    private final Select<String> salaryType = new Select<>();
    private final NumberField salaryRate = new NumberField();

    // read-only fields
    private final Select<Long> tripId = new Select<>();
    private final Select<SimpleEmployeeDTO> employee = new Select<>();
    private final Select<LocalDateTime> createdAt = new Select<>();
    private final IntegerField salary = new IntegerField();

    private final Binder<WorkTripDTO> binder;
    private final Dialog dialog = new Dialog();

    private final Button saveButton;
    private final Button cancelButton = new Button("Cancel", e -> dialog.close());
    private final Button openModalButton;

    public TripModal(WorkTripDTO workTripDTO,
                     boolean create,
                     Supplier<Object> updateGrid,
                     WorkTripService workTripService, TruckService truckService,
                     SecurityUtils securityUtils) {
        this.name.setSizeFull();
        this.description.setSizeFull();

        this.create = create;
        this.truckService = truckService;
        this.securityUtils = securityUtils;
        this.workTripService = workTripService;
        this.updateGridCallback = updateGrid;
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.saveButton = buildSaveButton();

        openModalButton = create ? new Button("Create new", e -> dialog.open()) : null;

        truck.setLabel("Truck");
        truck.setItemLabelGenerator(TruckDTO::getName);
        var availableTrucks = truckService.getAllAvailableForEmployee(currentEmployee.getId());
        if (!create) {
            availableTrucks.add(TruckDTO.builder().id(workTripDTO.getTruckId()).name(workTripDTO.getTruckName()).build());
        }
        truck.setItems(availableTrucks);
        truck.setSizeFull();

        salaryType.setLabel("Salary type");
        salaryType.setItems(SalaryType.getLabels());
        salaryType.setSizeFull();

        salaryRate.setLabel("Salary rate");
        salaryRate.setStep(0.5);
        salaryRate.setValue(1.5);
        salaryRate.setStepButtonsVisible(true);
        salaryRate.setSizeFull();

        currency.setLabel("Currency");
        currency.setItems(Currency.getLabels());
        currency.setSizeFull();

        active.setSizeFull();

        this.binder = buildBinder();
        if (workTripDTO != null) {
            binder.readBean(workTripDTO);
        }

        super.setSizeFull();

        buildDialog();
    }

    private Binder<WorkTripDTO> buildBinder() {
        var binder = new BeanValidationBinder<>(WorkTripDTO.class);

        binder.forField(truck).bind(
                workTripDTO -> TruckDTO.builder().id(workTripDTO.getTruckId()).name(workTripDTO.getTruckName()).build(),
                (workTripDTO, truckDTO) -> workTripDTO.setTruckId(truckDTO.getId())
        );

        binder.forField(tripId).bind(WorkTripDTO::getId, WorkTripDTO::setId);
        binder.forField(name).bind(WorkTripDTO::getName, WorkTripDTO::setName);
        binder.forField(active).bind(WorkTripDTO::isActive, WorkTripDTO::setActive);
        binder.forField(employee).bind(WorkTripDTO::getEmployee, WorkTripDTO::setEmployee);
        binder.forField(currency).bind(WorkTripDTO::getCurrency, WorkTripDTO::setCurrency);
        binder.forField(salary).bind(WorkTripDTO::getSalary, WorkTripDTO::setSalary);
        binder.forField(salaryType).bind(WorkTripDTO::getSalaryType, WorkTripDTO::setSalaryType);
        binder.forField(salaryRate).bind(WorkTripDTO::getSalaryRate, WorkTripDTO::setSalaryRate);
        binder.forField(description).bind(WorkTripDTO::getDescription, WorkTripDTO::setDescription);
        binder.forField(createdAt).bind(WorkTripDTO::getCreatedAt, WorkTripDTO::setCreatedAt);

        return binder;
    }

    private void buildDialog() {
        dialog.setHeaderTitle(create ? "Create new WorkTrip and Bon voyage!" : "Update existing trip");

        var dialogLayout = new VerticalLayout(name, description, truck, currency, salaryType, salaryRate, active);
        dialogLayout.setWidth("400px");

        dialog.add(dialogLayout);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        if (create) {
            add(openModalButton);
        }

        add(dialog);
    }

    private Button buildSaveButton() {
        Button saveButton = new Button();
        saveButton.setText(create ? "Save trip" : "Update trip");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(event -> {
            try {
                var tripDTO = WorkTripDTO.builder().build();
                binder.writeBean(tripDTO);
                if (create) {
                    tripDTO.setEmployee(SimpleEmployeeDTO.builder().id(currentEmployee.getId()).build());
                }
                log.info("Collected info: {}", tripDTO);

                workTripService.saveOrUpdate(tripDTO);
                updateGridCallback.get();

                Notification notification = Notification.show(create ? "Trip Added!" : "Trip Updated!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                dialog.close();
            } catch (Exception e) {
                Notification notification = Notification.show(String.format("Error: %s!", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                log.error("Validation error : {}", e.getMessage());
            }
        });

        return saveButton;
    }
}
