package com.example.test.backend.dataServiceRentalAgency;

import com.example.test.backend.MyDataService;
import com.example.test.backend.connection.JDBCConnection;
import com.example.test.backend.rentalAgencyData.User;

import java.util.List;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class UserDataService extends MyDataService {

    private static UserDataService INSTANCE;

    private JDBCConnection jdbcConnection = new JDBCConnection();

    private List<User> users;

    private UserDataService() {
        users = getUsersFromDatabase();
    }

    public synchronized static UserDataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDataService();
        }
        return INSTANCE;
    }

    public synchronized List<User> getAllUsers() {
        return users;
    }


    public synchronized boolean updateUser(User u) {
        for (int i = 0; i < getAllUsers().size(); i++) {
            if (users.get(i).getId() == u.getId()) {
                users.set(i, u);
                return jdbcConnection.updateUserQuery(u);
            }
        }
        return false;
    }

    public synchronized User getUserById(int userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return users.get(i);
            }
        }
        return null;
    }

    public synchronized void deleteUser(int userId) {
        User u = getUserById(userId);
        if (u == null) {
            throw new IllegalArgumentException("User with id " + userId
                    + " not found");
        }
        jdbcConnection.deleteUserQuery(u);
        users.remove(u);
    }

    private List<User> getUsersFromDatabase() {
        return jdbcConnection.getAllUsersQuery();
    }
}
