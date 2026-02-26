package invetorymanager.app.Product;
import inentorymanager.app.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("tests for stock alert")

public class StockAlertTests {
    private InventoryService inventoryService;

    @Test
    void testStockAlert_returnsTrueWhenProductIsBelowStock() {
        Product product = new product("hg01", "gundam", 10);
        inventoryService addProduct(product, 10);
        boolean stock = InventoryService.isLowStock(product);
        assertTrue(stock);
    }
}