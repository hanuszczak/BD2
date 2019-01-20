package com.example.test.backend;

import com.example.test.backend.data.Category;
import com.example.test.backend.data.Product;
import com.example.test.backend.dataServiceRentalAgency.UserDataService;
import com.example.test.backend.rentalAgencyData.User;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class MyDataService implements Serializable {

    public abstract Collection<User> getAllUsers();

    public abstract void updateUser(User u);

    public abstract void deleteUser(int userId);

    public abstract User getUserById(int userId);

    public static MyDataService get() {
        return UserDataService.getInstance();
    }

}
