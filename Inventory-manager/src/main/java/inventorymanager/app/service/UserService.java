package inventorymanager.app.service;

import inventorymanager.app.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public boolean addUser(User user) {
        return false;
    }
    public Map<String, User> getUsersList() {
        return users;
    }
    public void setUsers(List<User> users) {}
    public int getAllUsers() {
        return users.size();
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
}
