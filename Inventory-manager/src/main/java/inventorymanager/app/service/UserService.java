package inventorymanager.app.service;

import inventorymanager.app.model.User;
import inventorymanager.app.model.UserRoles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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
    public void setUsers(List<User> users) {}
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }
    public User getUserById(String id) {
        if (id == null) {
            throw new NullPointerException("User ID cannot be null");
        }
        if (!users.containsKey(id)) {
            throw new NullPointerException("User not found");
        }
        return users.get(id);
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean removeUser(String id) {
        if (id == null) {
            throw new NullPointerException("User ID cannot be null");
        }
        if (!users.containsKey(id)) {
            return false;
        }
        users.remove(id);
        return true;
    }

    public User registerAdmin(String username, String password) {
        if (username == null || password == null) {
            throw new NullPointerException("Username and password cannot be null");
        }
        User user = new User(username, passwordEncoder.encode(password), UserRoles.ADMIN);
        if (getUserByUsername(username) != null) {
            throw new RuntimeException("User already exists");
        }
        users.put(user.getId(), user);
        adminUsers.put(user.getId(), user);
        return user;
    }

    public User registerManager(String username, String password) {
        if (username == null || password == null) {
            throw new NullPointerException("Username and password cannot be null");
        }
        User user = new User(username, passwordEncoder.encode(password), UserRoles.MANAGER);
        if (getUserByUsername(username) != null) {
            throw new RuntimeException("User already exists");
        }
        users.put(user.getId(), user);
        managerUsers.put(user.getId(), user);
        return user;
    }

    public int getUsersCount() {
        return users.size();
    }

    /**
     * Checks if a user has a specific role
     * @param userId the ID of the user
     * @param role the role to check
     * @return true if user has the specified role, false otherwise
     */
    public boolean hasRole(String userId, UserRoles role) {
        UserRoles userRole = getRoleByUserId(userId);
        return userRole != null && userRole == role;
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
        User user = users.get(userId);
        if (user == null) {
            return false;
        }
        user.setRole(role);
        return true;
    }

    public User checkUserCredentials(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new NullPointerException("User not found");
        }
        return passwordEncoder.matches(password, user.getPassword())? user: null;
    }
}
