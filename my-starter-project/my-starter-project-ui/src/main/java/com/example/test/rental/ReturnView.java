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
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;


@Route(value = "Return", layout = MainLayout.class)
@PageTitle("Return")
public class ReturnView extends FlexLayout {

    public static final String VIEW_NAME = "Return a vehicle";

    private final RentalControl rentalControl;
    private final String currentUser = CurrentUser.get();

    private ComboBox<Region> regionBox;
    private ComboBox<Station> stationBox;
    private ComboBox<Vehicle> vehicleBox;
    private Button returnButton;


    private FormLayout formLayout;
    private Span span = new Span("No vehicles currently rented");
    private FlexLayout centeringLayout;

    public ReturnView() {
        rentalControl = new RentalControl();
        centeringLayout = new FlexLayout();
        rentalControl.getRentedVehiclesFromDatabaseForUser(currentUser);
        buildFormLayout();
        buildUI();
        regionBox.focus();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");
        removeAll();

        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.removeAll();

        if(!rentalControl.getVehicles().isEmpty())
            centeringLayout.add(formLayout);
        else
            centeringLayout.add(span);

        add(centeringLayout);
    }


    private void buildFormLayout() {
        formLayout = new FormLayout();

        vehicleBox = new ComboBox<>();
        vehicleBox.setItemLabelGenerator(Vehicle::toString);
        vehicleBox.setEnabled(true);
        rentalControl.getRentedVehiclesFromDatabaseForUser(currentUser);
        vehicleBox.setItems(rentalControl.getVehicles());
        vehicleBox.addValueChangeListener(event -> {
            Vehicle vehicle = vehicleBox.getValue();
            if(vehicle != null) {
                rentalControl.getRegionsFromDatabase();
                regionBox.setItems(rentalControl.getRegions());
                regionBox.setEnabled(true);
            }
            else {
                regionBox.setValue(null);
                regionBox.setEnabled(false);
            }
        });

        regionBox = new ComboBox<>();
        regionBox.setItemLabelGenerator(Region::getName);
        regionBox.setEnabled(false);
        regionBox.setItems(Collections.emptyList());
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
            if(station != null) {
                rentalControl.getVehicleTypesFromDatabaseForStation(station);
                returnButton.setEnabled(true);
            }
            else {
                returnButton.setEnabled(false);
            }
        });

        formLayout.addFormItem(vehicleBox, "Choose vehicle");
        vehicleBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.setWidth("310px");
        formLayout.addFormItem(regionBox, "Choose district");
        regionBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(stationBox, "Choose station");
        stationBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));

        HorizontalLayout buttons = new HorizontalLayout();
        formLayout.add(new Html("<br/>"));
        formLayout.add(buttons);

        buttons.add(returnButton = new Button("Return"));
        returnButton.setEnabled(false);
        returnButton.addClickListener(event -> returnVehicle());
        formLayout.getElement().addEventListener("keypress", event -> returnVehicle()).setFilter("event.key == 'Enter'");
        returnButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }

    public void returnVehicle() {
        returnButton.setEnabled(false);
        try {
            if (rentalControl.returnVehicle(currentUser, vehicleBox.getValue(), stationBox.getValue())) {
                getUI().get().navigate("Return");
                buildUI();
            } else {
                showNotification(new Notification("Returning a vehicle failed. " +
                        "Please try again."));
                regionBox.focus();
            }
        } finally {
            returnButton.setEnabled(true);
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }
}
