package edu.trucktrack.ui.modal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.SimpleEmployeeDTO;
import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.entity.enums.Currency;
import edu.trucktrack.repository.jooq.TagJooqRepository;
import edu.trucktrack.service.ExpensesService;
import edu.trucktrack.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.FastMoney;
import org.vaadin.addons.MoneyField;

import java.util.function.Supplier;

@Slf4j
public class AddCostModal extends VerticalLayout {

    private final ExpensesService expensesService;
    private final TagJooqRepository tagJooqRepository;
    private final Binder<EmployeeExpensesDTO> binder;

    private final Supplier<Object> updateListCallback;
    private final WorkTripDTO currentTrip;

    private final EmployeeEntity currentEmployee;

    private final MoneyField moneyField;
    private final TextField name = new TextField("Name", "name of cost");
    private final TextField description = new TextField("Description", "description of cost");
    private final Select<SimpleEmployeeDTO> employee = new Select<>();
    private final Select<WorkTripDTO> trip = new Select<>();
    private final MultiSelectComboBox<TagDTO> tags;

    private final Dialog dialog = new Dialog();
    private final Button saveButton = buildSaveButton();
    private final Button cancelButton = new Button("Cancel", e -> dialog.close());
    private final Button openModalButton = new Button("Create costs", e -> dialog.open());

    public AddCostModal(WorkTripDTO currentTrip,
                        Supplier<Object> updateListCallback,
                        ExpensesService expensesService,
                        SecurityUtils securityUtils,
                        TagJooqRepository tagJooqRepository) {
        this.updateListCallback = updateListCallback;
        this.expensesService = expensesService;
        this.tagJooqRepository = tagJooqRepository;
        this.currentTrip = currentTrip;
        this.currentEmployee = securityUtils.getCurrentEmployee();
        this.moneyField = getMoneyField();
        this.tags = buildBadgeMultiSelect();

        trip.setLabel("For trip");
        trip.setItemLabelGenerator(WorkTripDTO::getName);
        trip.setItems(currentTrip);
        trip.setSizeFull();

        this.name.setSizeFull();
        this.description.setSizeFull();

        this.binder = buildBinder();

        buildDialog();
    }

    private Binder<EmployeeExpensesDTO> buildBinder() {
        var binder = new BeanValidationBinder<>(EmployeeExpensesDTO.class);

        binder.forField(moneyField).bind(
                expense -> FastMoney.of(
                        expense.getValue() != null ? expense.getValue() : moneyField.getValue().getNumber(),
                        expense.getCurrency() != null ? expense.getCurrency() : moneyField.getValue().getCurrency().getCurrencyCode()),
                (expense, moneyField) -> {
                    expense.setValue(moneyField.getNumber().longValue());
                    expense.setCurrency(moneyField.getCurrency().getCurrencyCode());
                }
        );

        binder.forField(name).bind(EmployeeExpensesDTO::getName, EmployeeExpensesDTO::setName);
        binder.forField(description).bind(EmployeeExpensesDTO::getDescription, EmployeeExpensesDTO::setDescription);
        binder.forField(tags).bind(EmployeeExpensesDTO::getTags, EmployeeExpensesDTO::setTags);
        binder.forField(employee).bind(EmployeeExpensesDTO::getEmployee, EmployeeExpensesDTO::setEmployee);
        binder.forField(trip).bind(EmployeeExpensesDTO::getTrip, EmployeeExpensesDTO::setTrip);

        return binder;
    }

    private void buildDialog() {
        dialog.setHeaderTitle("Create new Costs!");

        var dialogLayout = createDialogLayout();

        dialog.add(dialogLayout);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        add(dialog, openModalButton);
    }

    private Button buildSaveButton() {
        Button saveButton = new Button("Save cost");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(event -> {
            try {
                var expensesDTO = EmployeeExpensesDTO.builder().build();
                binder.writeBean(expensesDTO);
                expensesDTO.setEmployee(new SimpleEmployeeDTO(currentEmployee.getId(), currentEmployee.getName()));

                log.info("Collected info: {}", expensesDTO);
                expensesService.saveOrUpdate(expensesDTO);

                Notification notification = Notification.show("Cost Added!");
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
        var dialogLayout = new VerticalLayout(name, description, trip, moneyField, tags);
        dialogLayout.setWidth("400px");

        return dialogLayout;
    }

    private MultiSelectComboBox<TagDTO> buildBadgeMultiSelect() {
        MultiSelectComboBox<TagDTO> badgeMultiselect = new MultiSelectComboBox<>();
        //TODO: for update add current tag from trip to items
        badgeMultiselect.setItems(tagJooqRepository.getAll());
        badgeMultiselect.setLabel("Cost tags");
        badgeMultiselect.setItemLabelGenerator(TagDTO::getName);
        badgeMultiselect.setSizeFull();
        return badgeMultiselect;
    }


    private MoneyField getMoneyField() {
        var currencies = Currency.getLabels();
        var defaultValue = FastMoney.of(100.0, currencies.get(0));
        MoneyField moneyField = new MoneyField(defaultValue, currencies, true);
        moneyField.setLabel("Input your first cost");
        moneyField.setPlaceholder("money");

        moneyField.setSizeFull();

        return moneyField;
    }
}
