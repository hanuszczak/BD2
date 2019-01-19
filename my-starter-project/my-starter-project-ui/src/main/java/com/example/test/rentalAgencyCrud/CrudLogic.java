package com.example.test.rentalAgencyCrud;

import com.example.test.authentication.AccessControlFactory;
import com.example.test.backend.MyDataService;
import com.example.test.backend.rentalAgencyData.User;
import com.vaadin.flow.component.UI;

import java.io.Serializable;


public class CrudLogic implements Serializable {

    private CrudView view;

    public CrudLogic(CrudView CrudView) {
        view = CrudView;
    }

    public void init() {
        editUser(null);
    }

    public void cancelUser() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String userId) {
        String fragmentParameter;
        if (userId == null || userId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = userId;
        }

        UI.getCurrent().navigate(CrudView.class, fragmentParameter);
    }

    public void enter(String userId) {
        if (userId != null && !userId.isEmpty()) {
            if (userId.equals("new")) {
                newUser();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    int pid = Integer.parseInt(userId);
                    User user = findUser(pid);
                    view.selectRow(user);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            //view.showForm(false);
        }
    }

    private User findUser(int userId) {
        return MyDataService.get().getUserById(userId);
    }

    public void saveUser(User user) {
        view.clearSelection();
        view.updateUser(user);
        setFragmentParameter("");
        view.showSaveNotification(user.idToString()
                + " modified");
    }

    public void deleteUser(User user) {
        view.clearSelection();
        view.removeUser(user);
        setFragmentParameter("");
        view.showSaveNotification(user.idToString() + " removed");
    }

    public void editUser(User user) {
        if (user == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(user.idToString());
        }
        view.editUser(user);
    }

    public void newUser() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editUser(new User());
    }

    public void rowSelected(User user) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRoleOfAdmin()
                || AccessControlFactory.getInstance().createAccessControl().isUserInRoleOfWorker()) {
            editUser(user);
        }
    }
}
