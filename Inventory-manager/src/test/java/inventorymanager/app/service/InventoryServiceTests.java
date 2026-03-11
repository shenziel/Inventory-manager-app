package inventorymanager.app.service;
import inventorymanager.app.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@DisplayName("Tests for stock alert and expiry alert")

public class InventoryServiceTests {
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService();
    }

//    @Test
    @DisplayName("isLowStock returns true when product is below threshold")
    void testStockAlert_returnsTrueWhenProductIsBelowStock() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct("Donut", 13.4, 10, LocalDate.now().plusDays(5));
        boolean stock = inventoryService.isLowStock(product.getName());
        assertTrue(stock);
    }

//    @Test
    @DisplayName("isAboutToExpire should be true for product expiring soon")
    void testExpiryAlert_returnsTrueWhenProductIsAboutToExpire() {
        Product product = new Product("exp01", "yogurt", 5);
        inventoryService.addProduct(product.getName(), 5, 10, LocalDate.now().plusDays(2));
        boolean alert = inventoryService.isAboutToExpire(product.getName());
        assertTrue(alert);
    }


//    @Test
    void testAddProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
        assertEquals("gundam", result.getName());
        assertEquals(10, result.getPrice());
    }

//    @Test
    void testRemoveProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        boolean removed = inventoryService.removeProduct("hg01");
        assertTrue(removed);
    }

//    @Test
    void testUpdateProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        Product updatedProduct = new Product("hg01", "gundam mk2", 15);
        Product result = inventoryService.updateProduct("hg01", updatedProduct);
        assertNotNull(result);
        assertEquals("gundam mk2", result.getName());
        assertEquals(15, result.getPrice());
    }

//    @Test
    void testAddProduct_shouldNotAddDuplicateProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        inventoryService.addProduct(product.getName(), 10, 10, LocalDate.now().plusDays(2));
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
        assertEquals("gundam", result.getName());
        assertEquals(10, result.getPrice());
    }

}