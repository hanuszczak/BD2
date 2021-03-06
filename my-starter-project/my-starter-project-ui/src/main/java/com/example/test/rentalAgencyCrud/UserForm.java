package com.example.test.rentalAgencyCrud;

import com.example.test.authentication.AccessControlFactory;
import com.example.test.backend.rentalAgencyData.Activity;
import com.example.test.backend.rentalAgencyData.User;
import com.example.test.backend.rentalAgencyData.UserType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A form for editing a single product.
 */
public class UserForm extends Div {

    private VerticalLayout content;

    private TextField usernameField;
    private ComboBox<UserType> userTypeBox;
    private TextField nameField;
    private TextField surnameField;
    private TextField emailField;
    private TextField phoneField;
    private ComboBox<Activity> activityBox;
    private Button save;
    private Button discard;
    private Button cancel;
    private Button delete;

    private CrudLogic viewLogic;
    private Binder<User> binder;
    private User currentUser;


    private static class IdConverter extends StringToIntegerConverter {

        public IdConverter() {
            super(0, "Could not convert value to " + Integer.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }
    }

    private static class PhoneConverter extends StringToLongConverter {

        public PhoneConverter() {
            super(0l, "Could not convert value to " + Long.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setGroupingUsed(false);
            return format;
        }
    }

    public UserForm(CrudLogic crudLogic) {
        setClassName("product-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = crudLogic;


        usernameField = new TextField("Username");
        usernameField.setRequired(true);
        usernameField.setValueChangeMode(ValueChangeMode.EAGER);
        usernameField.setEnabled(false);

        userTypeBox = new ComboBox<>("userTypeBox");
        userTypeBox.setWidth("100%");
        userTypeBox.setRequired(true);
        userTypeBox.setItems(UserType.values());
        userTypeBox.setAllowCustomValue(false);
        if(!isAdmin()){
            userTypeBox.setEnabled(false);
        }

        HorizontalLayout horizontalLayout1 = new HorizontalLayout(
                usernameField, userTypeBox);
        horizontalLayout1.setWidth("100%");
        horizontalLayout1.setFlexGrow(2, usernameField);
        horizontalLayout1.setFlexGrow(1, userTypeBox);
        content.add(horizontalLayout1);

        nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setValueChangeMode(ValueChangeMode.EAGER);

        surnameField = new TextField("Surname");
        surnameField.setRequired(true);
        surnameField.setValueChangeMode(ValueChangeMode.EAGER);


        HorizontalLayout horizontalLayout2 = new HorizontalLayout(nameField,
                surnameField);
        horizontalLayout2.setWidth("100%");
        horizontalLayout2.setFlexGrow(1, nameField, surnameField);
        content.add(horizontalLayout2);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setRequired(true);
        emailField.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(emailField);
        if(!isAdmin()){
            emailField.setEnabled(false);
        }

        phoneField = new TextField("Phone");
        phoneField.setWidth("100%");
        phoneField.setRequired(true);
        phoneField.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(phoneField);

        activityBox = new ComboBox<>("User status");
        activityBox.setWidth("100%");
        activityBox.setRequired(true);
        activityBox.setItems(Activity.values());
        activityBox.setAllowCustomValue(false);
        content.add(activityBox);

        binder = new BeanValidationBinder<>(User.class);
        binder.forField(phoneField).withConverter(new PhoneConverter()).bind("phone");
        binder.forField(nameField).bind("name");
        binder.forField(surnameField).bind("surname");
        binder.forField(usernameField).bind("username");
        binder.forField(emailField).bind("email");
        binder.forField(activityBox).bind("isActive");
        binder.forField(userTypeBox).bind("userType");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentUser != null
                    && binder.writeBeanIfValid(currentUser)) {
                viewLogic.saveUser(currentUser);
            }
        });

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editUser(currentUser));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelUser());
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelUser())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        if(!isAdmin()) {
            delete.setEnabled(false);
        }
        delete.addClickListener(event -> {
            if (currentUser != null) {
                viewLogic.deleteUser(currentUser);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editUser(User user) {
        if (user == null) {
            user = new User();
        }
        delete.setEnabled(isAdmin());
        currentUser = user;
        binder.readBean(user);
    }

    public boolean isAdmin() {
        return AccessControlFactory.getInstance().createAccessControl().isUserInRoleOfAdmin();
    }
}
