package inventorymanager.app.config;

import inventorymanager.app.model.Product;
import inventorymanager.app.model.User;
import inventorymanager.app.service.InventoryService;
import inventorymanager.app.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final InventoryService inventoryService;

    public DataInitializer(UserService userService, InventoryService inventoryService) {
        this.userService = userService;
        this.inventoryService = inventoryService;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {
        if (userService.getUsersCount() == 0) {
            userService.registerManager("manager", "admin123");
            userService.registerAdmin("admin", "admin123");
            System.out.println("Users seeded.");
        }
    }

    private void seedProducts() {
        if (inventoryService.getInventorySize() == 0) {
            inventoryService.addProduct("Croissants", 999.99, 10, LocalDate.now().plusDays(10));
            inventoryService.addProduct("Milk", 29.99, 50, LocalDate.now().plusDays(20));
            inventoryService.addProduct("Eggs", 49.99, 30, LocalDate.now().plusDays(30));
            System.out.println("Products seeded.");
        }
    }
}
