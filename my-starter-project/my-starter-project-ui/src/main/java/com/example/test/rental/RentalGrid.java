package com.example.test.rental;

import com.example.test.backend.rentalAgencyData.Rental;
import com.example.test.backend.rentalAgencyData.User;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.Comparator;

/**
 * Grid of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class RentalGrid extends Grid<Rental> {

    public RentalGrid() {
        setSizeFull();

        addColumn(Rental::rentalIdToString)
                .setHeader("ID")
                .setFlexGrow(2)
                .setSortable(true);

        addColumn(Rental::vehicleIdToString)
                .setHeader("Vehicle")
                .setFlexGrow(2)
                .setSortable(true);

        addColumn(Rental::getStationFrom)
                .setHeader("From")
                .setFlexGrow(2)
                .setSortable(true);

        addColumn(Rental::getStationTo)
                .setHeader("To")
                .setFlexGrow(2)
                .setSortable(true);

        addColumn(Rental::getDateFrom)
                .setHeader("Start")
                .setFlexGrow(3)
                .setSortable(true);

        addColumn(Rental::getDateTo)
                .setHeader("End")
                .setFlexGrow(3)
                .setSortable(true);

        addColumn(Rental::paymentValueToString)
                .setHeader("Cost")
                .setFlexGrow(2)
                .setSortable(true);

        addColumn(Rental::getPaymentStatus)
                .setHeader("Status")
                .setFlexGrow(3)
                .setSortable(true);

    }
}
