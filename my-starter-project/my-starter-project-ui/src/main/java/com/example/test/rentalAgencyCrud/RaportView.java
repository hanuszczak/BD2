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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.awt.*;
import java.time.LocalDate;
import java.util.Collections;


@Route(value = "Raport", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Raport")
public class RaportView extends FlexLayout {

    public static final String VIEW_NAME = "Raport";

    JDBCConnection jdbcConnection;
    RentalControl rentalControl;

    private ComboBox<Region> regionBox;
    private ComboBox<Station> stationBox;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<Integer> startHours;
    private ComboBox<Integer> startMinutes;
    private ComboBox<Integer> endHours;
    private ComboBox<Integer> endMinutes;
    private Label seperator;


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
        startDatePicker = new DatePicker();

        stationBox = new ComboBox<>();
        stationBox.setItemLabelGenerator(Station::getAddress);
        stationBox.setEnabled(false);

        stationBox.setItems(Collections.emptyList());

        startDatePicker = new DatePicker();
        startDatePicker.setLabel("From date");
//        startDatePicker.addValueChangeListener(event -> {
//            LocalDate selectedDate = event.getValue();
//            if (selectedDate != null) {
//                endDatePicker.setMin(selectedDate);
//            }
//        });

//        endDatePicker = new DatePicker();
//        endDatePicker.setLabel("To date");
//        endDatePicker.addValueChangeListener(event -> {
//            LocalDate selectedDate = event.getValue();
//            if (selectedDate != null) {
//                startDatePicker.setMax(selectedDate);
//            }
//        });

        startHours = new ComboBox<>("");
        startHours.setWidth("2em");
        startHours.setItems(0, 1, 2, 3);


        formLayout.setWidth("310px");
        formLayout.addFormItem(regionBox, "Choose district");
        regionBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(stationBox, "Choose station");
        stationBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(startDatePicker, "Choose type of vehicle");
        startDatePicker.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(endDatePicker, "Choose vehicle");
        endDatePicker.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        //formLayout.addFormItem(startHours);

        HorizontalLayout buttons = new HorizontalLayout();
        formLayout.add(new Html("<br/>"));
        formLayout.add(buttons);

        buttons.add(raportButton = new Button("Generate Raport"));
        raportButton.setEnabled(false);
        raportButton.addClickListener(event -> generateRaport());
        formLayout.getElement().addEventListener("keypress", event -> generateRaport()).setFilter("event.key == 'Enter'");
        raportButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }


    public void generateRaport() {
//        raportButton.setEnabled(false);
//        try {
//            if (jdbcConnection .generateReport(stationBox.getValue(), startDatePicker.getValue())) {
//                getUI().get().navigate("");
//            } else {
//                showNotification(new Notification("Renting a vehicle failed. " +
//                        "Please try again"));
//                regionBox.focus();
//            }
//        } finally {
//            raportButton.setEnabled(true);
//        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }
}
