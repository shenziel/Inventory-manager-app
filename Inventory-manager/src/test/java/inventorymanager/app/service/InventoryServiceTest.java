package inventorymanager.app.service;

import inventorymanager.app.exception.ForbiddenException;
import inventorymanager.app.model.Product;

import inventorymanager.app.model.User;
import inventorymanager.app.model.UserRoles;
import inventorymanager.app.repository.InMemoryInventoryRepository;
import inventorymanager.app.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag("unit")
@DisplayName("Tests for stock alert and expiry alert")
class InventoryServiceTest {
    private InventoryService inventoryService;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(inventoryRepository);
    }

    @Test
    @DisplayName("isLowStock returns true when product is below threshold")
    void testStockAlert_returnsTrueWhenProductIsBelowStock() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct("Donut", 13.4, 10, LocalDate.now().plusDays(5));
        boolean stock = inventoryService.isLowStock(product.getName());
        assertTrue(stock);
    }

    @Test
    @DisplayName("isAboutToExpire should be true for product expiring soon")
    void testExpiryAlert_returnsTrueWhenProductIsAboutToExpire() {
        Product product = new Product("exp01", "yogurt", 5);
        inventoryService.addProduct(product.getName(), 5, 10, LocalDate.now().plusDays(2));
        boolean alert = inventoryService.isAboutToExpire(product.getName());
        assertTrue(alert);
    }

    @Test
    void testAddProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
        assertEquals("gundam", result.getName());
        assertEquals(10, result.getPrice());
    }

    @Test
    void testRemoveProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        boolean removed = inventoryService.removeProduct("hg01");
        assertTrue(removed);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        Product updatedProduct = new Product("hg01", "gundam mk2", 15);
        Product result = inventoryService.updateProduct("hg01", updatedProduct);
        assertNotNull(result);
        assertEquals("gundam mk2", result.getName());
        assertEquals(15, result.getPrice());
    }

    @Test
    void testAddProduct_shouldNotAddDuplicateProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
        assertEquals("gundam", result.getName());
        assertEquals(10, result.getPrice());
    }

    @Test
    void manager_can_check_product_quantity() {
        InventoryRepository repo = new InMemoryInventoryRepository();
        InventoryService service = new InventoryService(repo);

        User manager = new User("John", "password", UserRoles.MANAGER);
        UUID productId = UUID.randomUUID();

        repo.setQuantity(productId, 15);

        int quantity = service.getProductQuantity(productId, manager);

        assertEquals(15, quantity);
    }

    @Test
    void admin_cannot_check_product_quantity() {
        InventoryRepository repo = new InMemoryInventoryRepository();
        InventoryService service = new InventoryService(repo);

        User admin = new User("Anna", "password", UserRoles.ADMIN);
        UUID productId = UUID.randomUUID();

        assertThrows(ForbiddenException.class,
                () -> service.getProductQuantity(productId, admin));
    }

}