package inventorymanager.app.model;

public class User {
    private final Long id;
    private final String username;
    private final String password;
    private String email;
    private boolean loggedIn;
    private UserRoles role;


    public User(Long id, String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.loggedIn = false;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isLoggedIn() {
        return false;
    }
    public void setLoggedIn(boolean loggedIn) {}

    public UserRoles getRole() {
        return null;
    }
    public void setRole(UserRoles role) {}
}
