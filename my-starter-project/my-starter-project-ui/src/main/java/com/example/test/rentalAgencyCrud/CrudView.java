package com.example.test.rentalAgencyCrud;

import com.example.test.MainLayout;
import com.example.test.backend.rentalAgencyData.User;
import com.example.test.crud.SampleCrudLogic;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link SampleCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "User", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class CrudView extends HorizontalLayout
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Users";
    private UserGrid grid;
    private UserForm form;
    private TextField filter;

    private CrudLogic viewLogic = new CrudLogic(this);

    private UserDataProvider dataProvider = new UserDataProvider();

    public CrudView() {
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new UserGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        form = new UserForm(viewLogic);
     
        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter name, availability or category");
        // Apply the filter to grid's data provider. TextField value is never null
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));


        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }


    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(User row) {
        grid.getSelectionModel().select(row);
    }

    public User getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void updateUser(User user) {
        dataProvider.save(user);
    }

    public void removeUser(User user) {
        dataProvider.delete(user);
    }

    public void editUser(User user) {
        showForm(user != null);
        //form.editUser(user);
    }

    public void showForm(boolean show) {
        form.setVisible(show);

        /* FIXME The following line should be uncommented when the CheckboxGroup
         * issue is resolved. The category CheckboxGroup throws an
         * IllegalArgumentException when the form is disabled.
         */
        //form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
