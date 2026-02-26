package inventorymanager.app;
import inventorymanager.app.Product;
import inventorymanager.app.services.InventoryService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("tests for stock alert")

public class StockAlertTests {
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService();
    }

    @Test
    void testStockAlert_returnsTrueWhenProductIsBelowStock() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product, 10);
        boolean stock = inventoryService.isLowStock(product);
        assertTrue(stock);
    }

    @Test
    void testStockAlert_returnsFalseWhenProductIsAboveStock() {
        Product product = new Product("hg02", "gundam age-FX", 15);
        inventoryService.addProduct(product, 20);
        boolean stock = inventoryService.isLowStock(product);
        assertFalse(stock);
    }
}