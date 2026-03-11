package inventorymanager.app.service;

import inventorymanager.app.model.User;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
public class AuthService {

    private  final UserService userService;
    public AuthService(UserService userService) {
        this.userService = userService;
    }

     public User login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
        User user = userService.checkUserCredentials(username, password);
        if (user == null) {
            throw new InputMismatchException("Invalid username or password");
        }
        user.setLoggedIn(true);
        return user;
     }

     public boolean logout(String usernameId) {
        User user = userService.getUserById(usernameId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!user.isLoggedIn()) {
            throw new IllegalStateException("User is not logged in");
        }
        user.setLoggedIn(false);
        return true;
     }
}
