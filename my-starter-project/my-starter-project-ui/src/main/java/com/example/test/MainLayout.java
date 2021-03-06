package com.example.test;

import com.example.test.authentication.CurrentUser;
import com.example.test.rental.AccountView;
import com.example.test.rental.HistoryView;
import com.example.test.rental.RentView;
import com.example.test.rental.ReturnView;
import com.example.test.rentalAgencyCrud.CrudView;
import com.example.test.rentalAgencyCrud.RaportView;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.example.test.about.AboutView;
import com.example.test.crud.SampleCrudView;

/**
 * The layout of the pages e.g. About and Inventory.
 */
@HtmlImport("css/shared-styles.html")
@Theme(value = Lumo.class)
public class MainLayout extends FlexLayout implements RouterLayout {
    private Menu menu;

    public MainLayout() {
        setSizeFull();
        setClassName("main-layout");

        menu = new Menu();
        if(CurrentUser.getRole().equals("user")) {
            menu.addView(RentView.class, RentView.VIEW_NAME,
                    VaadinIcon.STEP_FORWARD.create());
            menu.addView(ReturnView.class, ReturnView.VIEW_NAME,
                    VaadinIcon.STEP_BACKWARD.create());
            menu.addView(AccountView.class, AccountView.VIEW_NAME,
                    VaadinIcon.USER.create());
            menu.addView(HistoryView.class, HistoryView.VIEW_NAME,
                    VaadinIcon.LIST.create());
        }
        else {
            menu.addView(CrudView.class, CrudView.VIEW_NAME,
                    VaadinIcon.USERS.create());
            menu.addView(RaportView.class, RaportView.VIEW_NAME,
                    VaadinIcon.CLIPBOARD_TEXT.create());
        }
        add(menu);
    }
}
