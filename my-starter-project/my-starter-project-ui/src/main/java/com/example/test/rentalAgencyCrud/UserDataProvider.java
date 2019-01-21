package com.example.test.rentalAgencyCrud;

import com.example.test.backend.DataService;
import com.example.test.backend.MyDataService;
import com.example.test.backend.rentalAgencyData.User;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

public class UserDataProvider extends ListDataProvider<User> {

    /** Text filter that can be changed separately. */
    private String filterText = "";

    public UserDataProvider() {
        super(MyDataService.get().getAllUsers());
    }


    public void save(User user) {
        MyDataService.get().updateUser(user);
        refreshItem(user);
    }

    /**
     * Delete given user from the backing data service.
     * 
     * @param user
     *            the user to be deleted
     */
    public void delete(User user) {
        MyDataService.get().deleteUser(user.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for user name, availability and category.
     * 
     * @param filterText
     *            the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();

        setFilter(user -> passesFilter(user.getName(), filterText)
                || passesFilter(user.getSurname(), filterText)
                || passesFilter(user.getUsername(), filterText)
                || passesFilter(user.getEmail(), filterText));
    }

    @Override
    public Integer getId(User user) {
        Objects.requireNonNull(user,
                "Cannot provide an id for a null user.");

        return user.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}
