package inventorymanager.app.service;

import inventorymanager.app.model.User;

public class AuthService {

    private  final UserService userService;
    public AuthService(UserService userService) {
        this.userService = userService;
    }

     public boolean login(String username, String password) {
         return false;
     }

     public User registerAdmin(String username, String password) {
         return null;
     }

    public User registerManager(String username, String password) {
        return null;
    }

     public boolean logout(String username) {
         return false;
     }

     public void deleteUser(String id) {
         //userService.deleteUser(id);
     }
}
