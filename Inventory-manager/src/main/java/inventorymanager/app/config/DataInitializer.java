package inventorymanager.app.config;

import inventorymanager.app.model.Product;
import inventorymanager.app.model.User;
import inventorymanager.app.service.InventoryService;
import inventorymanager.app.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final InventoryService inventoryService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService,
                           InventoryService inventoryService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.inventoryService = inventoryService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {
        if (userService.count() == 0) {
            userService.saveAll(List.of(
                    new User("admin", passwordEncoder.encode("admin123"), "ADMIN"),
                    new User("john", passwordEncoder.encode("pass123"), "USER")
            ));
            System.out.println("Users seeded.");
        }
    }

    private void seedProducts() {
        if (inventoryService.count() == 0) {
            inventoryService.saveAll(List.of(
                    new Product("Laptop", 999.99, 10),
                    new Product("Mouse", 29.99, 50),
                    new Product("Keyboard", 49.99, 30)
            ));
            System.out.println("Products seeded.");
        }
    }
}
