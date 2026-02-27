package inventorymanager.app.service;

import inventorymanager.app.model.User;

public class AuthService {

    private  final UserService userService;
    public AuthService(UserService userService) {
        this.userService = userService;
    }

     public boolean login(String email, String password) {
         return userService.login(email, password);
     }

     public User registerAdmin(String email, String password) {
         userService.register(email, password);
         return null;
     }

     public boolean logout(String email) {
         return userService.logout(email);
     }

     public void deleteUser(Long id) {
         userService.deleteUser(id);
     }

    public User registerManager(String username, String password) {
        return null;
    }
}
