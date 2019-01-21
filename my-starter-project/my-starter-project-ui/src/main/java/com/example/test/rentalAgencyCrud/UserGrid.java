package com.example.test.rentalAgencyCrud;

import com.example.test.backend.data.Category;
import com.example.test.backend.data.Product;
import com.example.test.backend.rentalAgencyData.User;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Grid of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class UserGrid extends Grid<User> {

    public UserGrid() {
        setSizeFull();

        addColumn(User::idToString)
                .setHeader("ID")
                .setFlexGrow(3)
                .setSortable(true);

        addColumn(User::getUsername)
                .setHeader("Username")
                .setFlexGrow(10)
                .setSortable(true);

        addColumn(User::getName)
                .setHeader("Name")
                .setFlexGrow(10)
                .setSortable(true);

        addColumn(User::getSurname)
                .setHeader("Surname")
                .setFlexGrow(10)
                .setSortable(true);

        addColumn(User::getEmail)
                .setHeader("E-mail")
                .setFlexGrow(15)
                .setSortable(true);

        addColumn(User::phoneToString)
                .setHeader("Phone")
                .setFlexGrow(10)
                .setSortable(true);

        addColumn(User::userTypeToString)
                .setHeader("Type")
                .setFlexGrow(5)
                .setSortable(true);


        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css and are
        // used here in availabilityTemplate.
        final String activityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activity]]\"></iron-icon> [[item.activity]]";
        addColumn(TemplateRenderer.<User>of(activityTemplate)
                .withProperty("activity", user -> user.isActiveToString()))
                .setHeader("Activity")
                .setComparator(Comparator.comparing(User::getIsActive))
                .setFlexGrow(5);

    }

    public User getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(User user) {
        getDataCommunicator().refresh(user);
    }

}
