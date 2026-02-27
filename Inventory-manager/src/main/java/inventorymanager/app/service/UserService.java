package inventorymanager.app.service;

import inventorymanager.app.model.User;
import inventorymanager.app.model.UserRoles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public boolean createUser(String email, String password) {
        return false;
    }
    public Map<Long, User> getUsersList() {
        return users;
    }
    public void setUsers(List<User> users) {}
    public int getAllUsers() {
        return users.size();
    }
    public User getUserById(Long id) {
        return null;
    }
    public User getUserByUsername(String username) {
        return null;
    }

    public boolean removeUser(Long id) {
        return false;
    }

    public boolean login(String email, String password) {
        return false;
    }

    public User register(String email, String password) {
        return null;
    }

    public boolean logout(String email) {
        return false;
    }

    public void deleteUser(Long id) {
    }

    public boolean validateUser(User user) {
        return false;
    }

    /**
     * Checks if a user has a specific role
     * @param userId the ID of the user
     * @param role the role to check
     * @return true if user has the specified role, false otherwise
     */
    public boolean hasRole(Long userId, UserRoles role) {
        UserRoles userRole = getRoleByUserId(userId);
        return userRole != null && userRole == role;
    }

    private UserRoles getRoleByUserId(Long userId) {
        return null;
    }

    /**
     * Assigns a role to a user by their ID
     * @param userId the ID of the user
     * @param role the role to assign
     * @return true if role was assigned successfully, false if user not found
     */
    public boolean assignRole(Long userId, UserRoles role) {
        User user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setRole(role);
        return true;
    }
}
