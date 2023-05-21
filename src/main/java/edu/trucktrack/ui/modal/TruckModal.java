package edu.trucktrack.ui.modal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.service.TruckService;
import edu.trucktrack.util.SecurityUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.function.Supplier;

@Slf4j
public class TruckModal extends VerticalLayout {
    private final Binder<TruckDTO> binder;
    private final TruckService truckService;

    private final boolean update;
    private final Supplier<Object> updateListCallback;

    private final EmployeeEntity currentEmployee;

    private final TextField name = new TextField("Name", "name of truck");
    private final TextField truckNumber = new TextField("Truck number", "Truck number");
    private final TextField vinCode = new TextField("VIN code", "VIN");
    private final NumberField fuelConsumption = new NumberField();
    private final Checkbox active = new Checkbox("Active");


    // read-only fields
    private final Select<Long> id = new Select<>();
    private final Select<Long> companyId = new Select<>();
    private final Select<String> companyName = new Select<>();
    private final Select<LocalDate> createdAt = new Select<>();

    @Getter
    private final Dialog dialog = new Dialog();
    private final Button saveButton = buildSaveButton();
    private final Button cancelButton = new Button("Cancel", e -> dialog.close());
    private final Button openModalButton;

    public TruckModal(TruckDTO expensesDTO,
                      boolean update,
                      TruckService truckService,
                      Supplier<Object> updateListCallback,
                      SecurityUtils securityUtils) {
        this.update = update;
        this.truckService = truckService;
        this.updateListCallback = updateListCallback;
        this.currentEmployee = securityUtils.getCurrentEmployee();

        this.openModalButton = update ? null : new Button("Create new", e -> dialog.open());

        fuelConsumption.setLabel("Fuel consumption");
        fuelConsumption.setStep(0.5);
        fuelConsumption.setValue(30.0);
        fuelConsumption.setStepButtonsVisible(true);
        fuelConsumption.setSizeFull();

        this.name.setSizeFull();
        this.truckNumber.setSizeFull();
        this.vinCode.setSizeFull();

        this.binder = buildBinder();
        if (expensesDTO != null) {
            binder.readBean(expensesDTO);
        }

        buildDialog();
    }

    private Binder<TruckDTO> buildBinder() {
        var binder = new BeanValidationBinder<>(TruckDTO.class);

        binder.forField(id).bind(TruckDTO::getId, TruckDTO::setId);
        binder.forField(name).bind(TruckDTO::getName, TruckDTO::setName);
        binder.forField(companyId).bind(TruckDTO::getCompanyId, TruckDTO::setCompanyId);
        binder.forField(companyName).bind(TruckDTO::getCompanyName, TruckDTO::setCompanyName);
        binder.forField(truckNumber).bind(TruckDTO::getTruckNumber, TruckDTO::setTruckNumber);
        binder.forField(vinCode).bind(TruckDTO::getVinCode, TruckDTO::setVinCode);
        binder.forField(fuelConsumption).bind(TruckDTO::getFuelConsumption, TruckDTO::setFuelConsumption);
        binder.forField(active).bind(TruckDTO::isActive, TruckDTO::setActive);
        binder.forField(createdAt).bind(TruckDTO::getCreatedAt, TruckDTO::setCreatedAt);

        return binder;
    }

    private void buildDialog() {
        dialog.setHeaderTitle(update ? "Update existing truck" : "Create new truck!");

        var dialogLayout = createDialogLayout();

        dialog.add(dialogLayout);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        if (!update) {
            add(openModalButton);
        }

        add(dialog);
    }

    private Button buildSaveButton() {
        Button saveButton = new Button(update ? "Update truck"  : "Save truck");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(event -> {
            try {
                var truckDTO = TruckDTO.builder().build();
                binder.writeBean(truckDTO);
                truckDTO.setCompanyId(currentEmployee.getCompany().getId());
                truckDTO.setCompanyName(currentEmployee.getCompany().getName());

                log.info("Collected info: {}", truckDTO);
                truckService.save(truckDTO);

                Notification notification = Notification.show(update ? "Truck updated!" : "Truck Added!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                updateListCallback.get();

                dialog.close();
            } catch (ValidationException e) {
                Notification notification = Notification.show(String.format("Error: %s!", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                log.error("Validation error : {}", e.getMessage());
            }
        });

        return saveButton;
    }

    private VerticalLayout createDialogLayout() {
        var dialogLayout = new VerticalLayout(name, truckNumber, vinCode, fuelConsumption, active);
        dialogLayout.setWidth("400px");

        return dialogLayout;
    }
}
