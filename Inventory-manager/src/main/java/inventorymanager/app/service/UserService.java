package inventorymanager.app.service;

import inventorymanager.app.model.User;

import java.util.List;

public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public boolean addUser(User user) {
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

    public boolean removeUser(Long id) {
        return false;
    }
}
