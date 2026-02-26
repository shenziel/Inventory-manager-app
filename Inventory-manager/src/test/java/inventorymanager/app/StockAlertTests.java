package inventorymanager.app.services;
import inventorymanager.app.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("tests for stock alert")

public class StockAlertTests {
    private InventoryService inventoryService;

    @Test
    void testStockAlert_returnsTrueWhenProductIsBelowStock() {
        Product product = new product("hg01", "gundam", 10);
       InventoryService inventoryService addProduct(product, 10);
        boolean stock = InventoryService.isLowStock(product);
        assertTrue(stock);
    }

    @Test
    void testStockAlert_returnsFalseWhenProductIsAboveStock() {
        Product product new product"hg02", "gundam age-FX", 15);
        inventoryService addProduct(product, 20);
        boolean stock = InventoryService.isLowStock(product);
        assertfalse(stock);
    }
}