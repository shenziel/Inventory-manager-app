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

    @Test
    @DisplayName("isLowStock returns true when product is below threshold")
    void testStockAlert_returnsTrueWhenProductIsBelowStock() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct("Donut", 13.4, 10, LocalDate.now().plusDays(5));
        boolean stock = inventoryService.isLowStock(product.name());
        assertTrue(stock);
    }

    @Test
    @DisplayName("isAboutToExpire should be true for product expiring soon")
    void testExpiryAlert_returnsTrueWhenProductIsAboutToExpire() {
        Product product = new Product("exp01", "yogurt", 5);
        inventoryService.addProduct("Donut", 13.4, 10, LocalDate.now().plusDays(2));
        boolean alert = inventoryService.isAboutToExpire(product.name());
        assertTrue(alert);
    }

}