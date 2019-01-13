package com.example.test.authentication;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.Route;



/**
 * UI content when the user is not signed up yet.
 */
@Route("SignUp")
@PageTitle("SignUp")
@HtmlImport("css/shared-styles.html")
public class SignUpScreen extends FlexLayout {

    public static final String VIEW_NAME = "SignUp";

    private TextField username;
    private PasswordField password;
    private PasswordField confirmPassword;
    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField phone;
    private Button signUp;
    private AccessControl accessControl;

    public SignUpScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
        username.focus();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // sign up form, centered in the available part of the screen
        Component signUpForm = buildSignUpForm();

        // layout to center sign in form when there is sufficient screen space
        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(signUpForm);

        // information text about signing up
        Component signUpInformation = buildSignUpInformation();

        add(signUpInformation);
        add(centeringLayout);
    }

    private Component buildSignUpForm() {
        FormLayout signUpForm = new FormLayout();

        signUpForm.setWidth("310px");

        signUpForm.addFormItem(username = new TextField(), "Username");
        username.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(password = new PasswordField(), "Password");
        password.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(confirmPassword = new PasswordField(), "Confirm Password");
        confirmPassword.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(name = new TextField(), "First Name");
        name.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(surname = new TextField(), "Surname");
        surname.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(email = new TextField(), "E-mail");
        email.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(phone = new TextField(), "Phone number");

        HorizontalLayout buttons = new HorizontalLayout();
        signUpForm.add(new Html("<br/>"));
        signUpForm.add(buttons);

        buttons.add(signUp = new Button("Sign up"));
        signUp.addClickListener(event -> signUp());
        signUpForm.getElement().addEventListener("keypress", event -> signUp()).setFilter("event.key == 'Enter'");
        signUp.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        return signUpForm;
    }

    private Component buildSignUpInformation() {
        VerticalLayout signUpInformation = new VerticalLayout();
        signUpInformation.setClassName("login-information");

        H1 signUpInfoHeader = new H1("Warsaw Rental Agency");
        Span signUpInfoText = new Span(
                "Sign up to the biggest Rental Agency in Warsaw to use ours bicycles and other mechanical vehicles");
        signUpInformation.add(signUpInfoHeader);
        signUpInformation.add(signUpInfoText);

        return signUpInformation;
    }

    private void signUp() {
        signUp.setEnabled(false);
        try {
            if (accessControl.signUp(username.getValue(), password.getValue(), name.getValue(),
                    surname.getValue(), email.getValue(), phone.getValue())){
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("Sign Up failed. " +
                        "Please check your data and try again."));
                username.focus();
            }
        } finally {
            signUp.setEnabled(true);
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }

}