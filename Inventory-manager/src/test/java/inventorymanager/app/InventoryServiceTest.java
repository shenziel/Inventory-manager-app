package inventorymanager.app;

import inventorymanager.app.repository.*;
import inventorymanager.app.services.*;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @Test
    void manager_can_check_product_quantity() {
        InventoryRepository repo = new InMemoryInventoryRepository();
        InventoryService service = new InventoryService(repo);

        User manager = new User("John", Role.MANAGER);
        UUID productId = UUID.randomUUID();

        repo.setQuantity(productId, 15);

        int quantity = service.getProductQuantity(productId, manager);

        assertEquals(15, quantity);
    }

    @Test
    void admin_cannot_check_product_quantity() {
        InventoryRepository repo = new InMemoryInventoryRepository();
        InventoryService service = new InventoryService(repo);

        User admin = new User("Anna", Role.ADMIN);
        UUID productId = UUID.randomUUID();

        assertThrows(ForbiddenException.class,
                () -> service.getProductQuantity(productId, admin));
    }
}