package com.example.test.authentication;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@HtmlImport("css/shared-styles.html")
public class LoginScreen extends FlexLayout {

    private TextField username;
    private PasswordField password;
    private Button login;
    private Button signUpButton;

    private TextField usernameSignUp;
    private PasswordField passwordSignUp;
    private PasswordField confirmPassword;
    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField phone;
    private Button signUp;

    private FlexLayout centeringLayout;
    private FormLayout signUpForm;
    private FormLayout loginForm;
    private AccessControl accessControl;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        centeringLayout = new FlexLayout();
        buildLoginForm();
        buildSignUpForm();
        buildUI();
        username.focus();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // layout to center login form when there is sufficient screen space

        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        // information text about logging in
        Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(centeringLayout);
    }

    private void buildLoginForm() {
        loginForm = new FormLayout();

        loginForm.setWidth("310px");

        loginForm.addFormItem(username = new TextField(), "Username");
        username.setWidth("15em");
        loginForm.add(new Html("<br/>"));
        loginForm.addFormItem(password = new PasswordField(), "Password");
        password.setWidth("15em");

        HorizontalLayout buttons = new HorizontalLayout();
        loginForm.add(new Html("<br/>"));
        loginForm.add(buttons);

        buttons.add(login = new Button("Login"));
        login.addClickListener(event -> login());
        loginForm.getElement().addEventListener("keypress", event -> login()).setFilter("event.key == 'Enter'");
        login.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        buttons.add(signUpButton = new Button("Sign Up!"));
        signUpButton.addClickListener(event -> {
            centeringLayout.remove(loginForm);
            centeringLayout.add(signUpForm);
        });
        signUpButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    }
    private void buildSignUpForm() {
        signUpForm = new FormLayout();

        signUpForm.setWidth("310px");

        signUpForm.addFormItem(usernameSignUp = new TextField(), "Username");
        usernameSignUp.setWidth("15em");
        signUpForm.add(new Html("<br/>"));
        signUpForm.addFormItem(passwordSignUp = new PasswordField(), "Password");
        passwordSignUp.setWidth("15em");
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

    }



    private Component buildLoginInformation() {
        VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        H1 loginInfoHeader = new H1("Warsaw Rental Agency");
        Span loginInfoText = new Span(
                "Log in to gain opportunity to rent bicycles and other mechanical vehicles");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);

        return loginInformation;
    }

    private void login() {
        login.setEnabled(false);
        try {
            if (accessControl.signIn(username.getValue(), password.getValue())) {
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("Login failed. " +
                        "Please check your username and password and try again."));
                username.focus();
            }
        } finally {
            login.setEnabled(true);
        }
    }

    private void signUp() {
        signUp.setEnabled(false);
        try {
            if (accessControl.signUp(usernameSignUp.getValue(), passwordSignUp.getValue(), name.getValue(),
                    surname.getValue(), email.getValue(), phone.getValue())){
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("Sign Up failed. " +
                        "Please check your data and try again."));
                usernameSignUp.focus();
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
