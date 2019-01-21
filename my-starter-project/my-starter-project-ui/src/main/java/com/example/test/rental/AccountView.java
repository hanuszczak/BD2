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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import net.bytebuddy.build.Plugin;


@Route(value = "Account", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Account")
public class AccountView extends FlexLayout {

    public static final String VIEW_NAME = "Account";

    private final RentalControl rentalControl;
    private final String currentUser = CurrentUser.get();

    private TextField actualBalance;
    private TextField topUpAmount;
    private Button topUpButton;

    private PasswordField oldPassword;
    private PasswordField newPassword;
    private Button changePassword;

    private FormLayout formLayout;
    private FormLayout passwordLayout;
    private FlexLayout centeringLayout;
    private HorizontalLayout forms;

    public AccountView() {
        rentalControl = new RentalControl();
        centeringLayout = new FlexLayout();
        buildFormLayout();
        buildPasswordLayout();
        buildUI();
        topUpAmount.focus();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        forms = new HorizontalLayout();
        forms.setClassName("forms-layout");

        forms.add(formLayout);
        forms.add(passwordLayout);
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(forms);
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
        topUpAmount.setValueChangeMode(ValueChangeMode.EAGER);

        formLayout.setWidth("310px");
        formLayout.addFormItem(actualBalance, "Balance");
        actualBalance.setWidth("12em");
        formLayout.add(new Html("<br/>"));
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(topUpAmount, "Top up amount");
        topUpAmount.setWidth("12em");


        HorizontalLayout buttons = new HorizontalLayout();
        formLayout.add(new Html("<br/>"));
        formLayout.add(buttons);

        buttons.add(topUpButton = new Button("Top Up!"));
        topUpButton.addClickListener(event -> topUp());
        formLayout.getElement().addEventListener("keypress", event -> topUp()).setFilter("event.key == 'Enter'");
        topUpButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }

    public void buildPasswordLayout() {
        passwordLayout = new FormLayout();

        passwordLayout.setWidth("310px");

        passwordLayout.addFormItem(oldPassword = new PasswordField(), "Old password");
        oldPassword.setWidth("12em");
        passwordLayout.add(new Html("<br/>"));

        passwordLayout.addFormItem(newPassword = new PasswordField(), "New password");
        newPassword.setWidth("12em");
        passwordLayout.add(new Html("<br/>"));

        HorizontalLayout buttons = new HorizontalLayout();
        passwordLayout.add(new Html("<br/>"));
        passwordLayout.add(buttons);

        buttons.add(changePassword = new Button("Change password!"));
        changePassword.addClickListener(event -> changePass());
        changePassword.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }
    public void changePass() {
        changePassword.setEnabled(false);
        try {
            if (rentalControl.changePass(currentUser, oldPassword.getValue(), newPassword.getValue())) {
                showNotification(new Notification("Password changed"));
                topUpAmount.focus();
            } else {
                showNotification(new Notification("An error occurred. " +
                        "Please try again"));
                topUpAmount.focus();
            }
        } finally {
            changePassword.setEnabled(true);
        }
    }

    public void topUp() {
        topUpButton.setEnabled(false);
        try {
            if (rentalControl.topUp(currentUser, Integer.parseInt(topUpAmount.getValue()))) {
                showNotification(new Notification("Account successfully loaded"));
                actualBalance.setValue(String.format("%.2f", rentalControl.getActualBalanceFor(currentUser)));
                topUpAmount.focus();
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
