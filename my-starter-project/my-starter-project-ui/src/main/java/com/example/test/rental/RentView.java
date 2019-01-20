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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.Collections;


@Route(value = "Rent", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Rent")
public class RentView extends FlexLayout {

    public static final String VIEW_NAME = "Rent a vehicle";

    private final RentalControl rentalControl;
    private final String currentUser = CurrentUser.get();

    private ComboBox<Region> regionBox;
    private ComboBox<Station> stationBox;
    private ComboBox<VehicleType> vehicleTypeBox;
    private ComboBox<Vehicle> vehicleBox;
    private Button rentButton;


    private FormLayout formLayout;
    private FlexLayout centeringLayout;

    public RentView() {
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
           if(station != null) {
               rentalControl.getVehicleTypesFromDatabaseForStation(station);
               vehicleTypeBox.setItems(rentalControl.getVehicleTypes());
               vehicleTypeBox.setEnabled(true);
           }
           else {
               vehicleTypeBox.setValue(null);
               vehicleTypeBox.setEnabled(false);
           }
        });


        vehicleTypeBox = new ComboBox<>();
        vehicleTypeBox.setItemLabelGenerator(VehicleType::getType);
        vehicleTypeBox.setEnabled(false);
        vehicleTypeBox.setItems(Collections.emptyList());
        vehicleTypeBox.addValueChangeListener(event ->{
           VehicleType vehicleType = vehicleTypeBox.getValue();
           if(vehicleType != null) {
               rentalControl.getVehiclesFromDatabaseForStation(stationBox.getValue(), vehicleType);
               vehicleBox.setItems(rentalControl.getVehicles());
               vehicleBox.setEnabled(true);
           }
           else {
               vehicleBox.setValue(null);
               vehicleBox.setEnabled(false);
           }
        });

        vehicleBox = new ComboBox<>();
        vehicleBox.setItemLabelGenerator(Vehicle::toString);
        vehicleBox.setEnabled(false);
        vehicleBox.setItems(Collections.emptyList());
        vehicleBox.addValueChangeListener(event -> {
            Vehicle vehicle = vehicleBox.getValue();
            if(vehicle != null){
                rentButton.setEnabled(true);
            }
            else {
                rentButton.setEnabled(false);
            }
        });

        formLayout.setWidth("310px");
        formLayout.addFormItem(regionBox, "Choose district");
        regionBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(stationBox, "Choose station");
        stationBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(vehicleTypeBox, "Choose type of vehicle");
        vehicleTypeBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));
        formLayout.addFormItem(vehicleBox, "Choose vehicle");
        vehicleBox.setWidth("20em");
        formLayout.add(new Html("<br/>"));

        HorizontalLayout buttons = new HorizontalLayout();
        formLayout.add(new Html("<br/>"));
        formLayout.add(buttons);

        buttons.add(rentButton = new Button("Rent"));
        rentButton.setEnabled(false);
        rentButton.addClickListener(event -> rent());
        formLayout.getElement().addEventListener("keypress", event -> rent()).setFilter("event.key == 'Enter'");
        rentButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

    }

    public void rent() {
        rentButton.setEnabled(false);
        try {
            if (rentalControl.rent(currentUser, stationBox.getValue(), vehicleTypeBox.getValue(), vehicleBox.getValue() )) {
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("Renting a vehicle failed. " +
                        "Please try again"));
                regionBox.focus();
            }
        } finally {
            rentButton.setEnabled(true);
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }
}
