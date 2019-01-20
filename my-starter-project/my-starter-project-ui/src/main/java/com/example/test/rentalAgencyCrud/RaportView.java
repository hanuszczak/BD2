package com.example.test.rentalAgencyCrud;

import com.example.test.MainLayout;
import com.example.test.authentication.CurrentUser;
import com.example.test.backend.connection.JDBCConnection;
import com.example.test.backend.connection.RentalControl;
import com.example.test.backend.rentalAgencyData.Region;
import com.example.test.backend.rentalAgencyData.Station;
import com.example.test.backend.rentalAgencyData.Vehicle;
import com.example.test.backend.rentalAgencyData.VehicleType;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.LocalDate;
import java.util.Collections;


@Route(value = "Raport", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Raport")
public class RaportView extends FlexLayout {

    public static final String VIEW_NAME = "Raport";

    JDBCConnection jdbcConnection;
    RentalControl rentalControl;

    private ComboBox<Region> regionBox;
    private ComboBox<Station> stationBox;
    private TextField daysField;

    private Button raportButton;

    private FormLayout formLayout;
    private FlexLayout centeringLayout;

    public RaportView() {
        jdbcConnection = new JDBCConnection();
        rentalControl = new RentalControl();
        centeringLayout = new FlexLayout();
        buildFormLayout();
        buildUI();
        regionBox.focus();
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

        regionBox = new ComboBox<>();
        regionBox.setItemLabelGenerator(Region::getName);
        regionBox.setEnabled(true);

        rentalControl.getRegionsFromDatabase();
        regionBox.setItems(rentalControl.getRegions());
        regionBox.addValueChangeListener(event -> {
            Region region = regionBox.getValue();
            if(region != null) {
                rentalControl.getStationsFromDatabseForRegion(region);
                stationBox.setItems(rentalControl.getStations());
                stationBox.setEnabled(true);
            }
            else {
                stationBox.setValue(null);
                stationBox.setEnabled(false);
            }
        });

        stationBox = new ComboBox<>();
        stationBox.setItemLabelGenerator(Station::getAddress);
        stationBox.setEnabled(false);
        stationBox.setItems(Collections.emptyList());
        stationBox.addValueChangeListener(event -> {
                    Station station = stationBox.getValue();
                    if (station != null) {
                        daysField.setEnabled(true);
                    } else {
                        daysField.setEnabled(false);
                    }
        });

        daysField = new TextField();
        daysField.setPattern("[0-9]*");
        daysField.setPreventInvalidInput(true);
        daysField.setEnabled(false);
        daysField.setValueChangeMode(ValueChangeMode.EAGER);

        daysField.addValueChangeListener(event -> {
           if(!daysField.isEmpty()) {
               raportButton.setEnabled(true);
           }
           else {
               raportButton.setEnabled(false);
           }
        });

        formLayout.setWidth("310px");
        formLayout.addFormItem(regionBox, "Choose district");
        regionBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(stationBox, "Choose station");
        stationBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(daysField, "Create raport for X days back");
        daysField.setWidth("20em");
        formLayout.add(new Html("<br/>"));


        HorizontalLayout buttons = new HorizontalLayout();
        formLayout.add(new Html("<br/>"));
        formLayout.add(buttons);

        buttons.add(raportButton = new Button("Generate Raport"));
        raportButton.setEnabled(false);
        raportButton.addClickListener(event -> {
            if(daysField.isEmpty()) {
                showNotification(new Notification("Please enter number of days"));
                }
            else {
                generateRaport();
            }
        });
        formLayout.getElement().addEventListener("keypress", event -> generateRaport()).setFilter("event.key == 'Enter'");
        raportButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }


    public void generateRaport() {
        raportButton.setEnabled(false);
        try {
            if (jdbcConnection.generateRaport(stationBox.getValue(), Integer.parseInt(daysField.getValue()))) {
                getUI().get().navigate("Users");
            } else {
                showNotification(new Notification("Generating a report failed. " +
                        "Please try again"));
                regionBox.focus();
            }
        } finally {
            raportButton.setEnabled(true);
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }
}
