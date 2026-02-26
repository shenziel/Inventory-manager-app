package inventorymanager.app.service;

import inventorymanager.app.model.User;

import java.util.List;

public class UserService {

    private List<User> users;

    public boolean addUser(User user) {
        return false;
    }
    public List<User> getUsersList() {
        return users;
    }
    public void setUsers(List<User> users) {}
    public int getAllUsers() {
        return users.size();
    }
    public User getUser(Long id) {
        return null;
    }

    public boolean removeUser(Long id) {
        return false;
    }
}
