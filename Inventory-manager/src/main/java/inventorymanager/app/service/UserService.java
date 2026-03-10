package inventorymanager.app.service;

import inventorymanager.app.model.User;
import inventorymanager.app.model.UserRoles;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, User> managerUsers = new HashMap<>();
    private final Map<String, User> adminUsers = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, User> getUsersList() {
        return users;
    }

    public User getUserById(String id) {
        return null;
    }

    public User getUserByUsername(String username) {
        return null;
    }

    public boolean removeUser(String id) {
        return false;
    }

    public User registerAdmin(String username, String password) {
        return null;
    }

    public User registerManager(String username, String password) {
        return null;
    }

    public int getUsersCount() {
        return -1;
    }

    /**
     * Checks if a user has a specific role
     * @param userId the ID of the user
     * @param role the role to check
     * @return true if user has the specified role, false otherwise
     */
    public boolean hasRole(String userId, UserRoles role) {
        return false;
    }

    private UserRoles getRoleByUserId(String userId) {
        return null;
    }

    /**
     * Assigns a role to a user by their ID
     * @param userId the ID of the user
     * @param role the role to assign
     * @return true if role was assigned successfully, false if user not found
     */
    public boolean assignRole(String userId, UserRoles role) {
        return false;
    }
}
