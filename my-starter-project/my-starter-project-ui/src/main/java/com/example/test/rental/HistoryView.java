package com.example.test.rental;

import com.example.test.MainLayout;
import com.example.test.authentication.CurrentUser;
import com.example.test.crud.SampleCrudLogic;
import com.example.test.rentalAgencyCrud.UserDataProvider;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link SampleCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "History", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class HistoryView extends HorizontalLayout {

    public static final String VIEW_NAME = "History";
    private RentalGrid grid;

    private Label title;

    private RentalDataProvider dataProvider = new RentalDataProvider();

    public HistoryView() {
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new RentalGrid();
        grid.setDataProvider(dataProvider);


        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);

    }

    public HorizontalLayout createTopBar() {

        title = new Label("Your rentals history");

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(title);
        topLayout.setVerticalComponentAlignment(Alignment.START, title);
        topLayout.expand(title);
        return topLayout;
    }


}
