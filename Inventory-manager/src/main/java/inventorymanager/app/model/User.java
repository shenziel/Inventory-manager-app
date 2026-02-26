package inventorymanager.app.model;

public class User {
    public Long id;
    public  String username;
    public String password;
    public String email;

    public User(Long id, String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
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
}
