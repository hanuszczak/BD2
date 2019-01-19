package com.example.test.rental;

import com.example.test.MainLayout;
import com.example.test.authentication.CurrentUser;
import com.example.test.backend.connection.RentalControl;
import com.example.test.backend.rentalAgencyData.Region;
import com.example.test.backend.rentalAgencyData.Station;
import com.example.test.backend.rentalAgencyData.Vehicle;
import com.example.test.backend.rentalAgencyData.VehicleType;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


@Route(value = "Account", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Account")
public class AccountView extends FlexLayout {

    public static final String VIEW_NAME = "Account Balance";

    private final RentalControl rentalControl;
    private final String currentUser = CurrentUser.get();

    private TextField actualBalance;
    private TextField topUpAmount;
    private Button topUpButton;

    private FormLayout formLayout;
    private FlexLayout centeringLayout;

    public AccountView() {
        rentalControl = new RentalControl();
        centeringLayout = new FlexLayout();
        buildFormLayout();
        buildUI();
        topUpAmount.focus();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(formLayout);

        add(centeringLayout);
    }


    private void buildFormLayout() {
        formLayout = new FormLayout();

        String balanceAsString = String.format("%.2f", rentalControl.getActualBalanceFor(currentUser));
        actualBalance = new TextField();
        actualBalance.setValue(balanceAsString);
        actualBalance.setReadOnly(true);
        actualBalance.setSuffixComponent(new Span("zł"));
        actualBalance.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        topUpAmount = new TextField();
        topUpAmount.setPattern("[0-9]*");
        topUpAmount.setPreventInvalidInput(true);
        topUpAmount.setSuffixComponent(new Span("zł"));
        topUpAmount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        formLayout.setWidth("310px");
        formLayout.addFormItem(actualBalance, "Balance");
        actualBalance.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(topUpAmount, "Top up amount");
        topUpAmount.setWidth("20em");


        HorizontalLayout buttons = new HorizontalLayout();
        formLayout.add(new Html("<br/>"));
        formLayout.add(buttons);

        buttons.add(topUpButton = new Button("Rent"));
        topUpButton.setEnabled(false);
        topUpButton.addClickListener(event -> topUp());
        formLayout.getElement().addEventListener("keypress", event -> topUp()).setFilter("event.key == 'Enter'");
        topUpButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }

    public void topUp() {
        topUpButton.setEnabled(false);
        try {
            if (rentalControl.topUp(currentUser, Integer.parseInt(topUpAmount.getValue()))) {
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("An error occurred. " +
                        "Please try again"));
                topUpAmount.focus();
            }
        } finally {
            topUpButton.setEnabled(true);
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }
}
