package edu.trucktrack.ui.modal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import edu.trucktrack.api.dto.CostDTO;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.FastMoney;
import org.vaadin.addons.MoneyField;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
public class AddCostModal extends VerticalLayout {

    private final MoneyField moneyField;

    private final TextField name = new TextField("Name", "name of cost");
    private final TextField description = new TextField("Description", "description of cost");

    private final Binder<CostDTO> costDTOBinder;
    private final Dialog dialog = new Dialog();

    private final MultiSelectComboBox<String> badges;
    private final Button saveButton = buildSaveButton();
    private final Button cancelButton = new Button("Cancel", e -> dialog.close());
    private final Button openModalButton = new Button("Create costs", e -> dialog.open());

    public AddCostModal(List<String> currencies) {
        this.moneyField = getMoneyField(currencies);
        this.badges = buildBadgeMultiSelect();

        this.name.setSizeFull();
        this.description.setSizeFull();

        this.costDTOBinder = buildBinder();

        buildDialog();
    }

    private Binder<CostDTO> buildBinder() {
        var costDTOBinder = new BeanValidationBinder<>(CostDTO.class);

        costDTOBinder.forField(moneyField).bind(
                costDTO -> FastMoney.of(
                        costDTO.getAmount() != null ? costDTO.getAmount() : moneyField.getValue().getNumber(),
                        costDTO.getCurrencyAmount() != null ? costDTO.getCurrencyAmount() : moneyField.getValue().getCurrency().getCurrencyCode()),
                (costDTO, moneyField) -> {
                    costDTO.setAmount(BigDecimal.valueOf(moneyField.getNumber().doubleValue()));
                    costDTO.setCurrencyAmount(moneyField.getCurrency().getCurrencyCode());
                }
        );

        costDTOBinder.forField(name).bind(CostDTO::getName, CostDTO::setName);
        costDTOBinder.forField(description).bind(CostDTO::getDescription, CostDTO::setDescription);
        costDTOBinder.forField(badges).bind(CostDTO::getBadges, CostDTO::setBadges);

        return costDTOBinder;
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
                CostDTO costDTO = CostDTO.builder().build();
                costDTOBinder.writeBean(costDTO);
                log.info("Collected info: {}", costDTO);

                Notification notification = Notification.show("Cost Added!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

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
        var dialogLayout = new VerticalLayout(name, description, moneyField, badges);
        dialogLayout.setWidth("400px");

        return dialogLayout;
    }

    private MultiSelectComboBox<String> buildBadgeMultiSelect() {
        //TODO: fetch real data
        MultiSelectComboBox<String> badgeMultiselect = new MultiSelectComboBox<>();
        badgeMultiselect.setItems(Set.of("Food", "Travel", "Work"));
        badgeMultiselect.setLabel("Cost badges");
        badgeMultiselect.setSizeFull();
        return badgeMultiselect;
    }


    private MoneyField getMoneyField(List<String> currencies) {
        var defaultValue = FastMoney.of(100.0, currencies.get(0));
        MoneyField moneyField = new MoneyField(defaultValue, currencies, true);
        moneyField.setLabel("Input your first cost");
        moneyField.setPlaceholder("money");

        moneyField.addValueChangeListener(event -> System.out.println(event.getValue()));
        moneyField.setSizeFull();

        return moneyField;
    }
}
