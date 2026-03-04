package inventorymanager.app.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class User {

    private String id;
    private String username;
    private String password;
    private String email;
    private boolean loggedIn;
    private UserRoles role;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.id = UUID.randomUUID().toString();
    }

}
