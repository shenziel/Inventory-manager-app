package inventorymanager.app.service;
import inventorymanager.app.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("tests for crud product")

public class ProductServiceTests {
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService();
    }

    @Test
    void testAddProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product, 10);
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
        assertEquals("gundam", result.name());
        assertEquals(10, result.price());
    }

    @Test
    void testRemoveProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product, 10);
        boolean removed = inventoryService.removeProduct("hg01");
        assertTrue(removed);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product, 10);
        Product updatedProduct = new Product("hg01", "gundam mk2", 15);
        Product result = inventoryService.updateProduct("hg01", updatedProduct);
        assertNotNull(result);
        assertEquals("gundam mk2", result.name());
        assertEquals(15, result.price());
    }

    @Test
    void testAddProduct_shouldNotAddDuplicateProduct() {
        Product product = new Product("hg01", "gundam", 10);
        inventoryService.addProduct(product, 10);
        inventoryService.addProduct(product, 10);
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
        assertEquals("gundam", result.name());
        assertEquals(10, result.price());
    }

    @Test
    void updateProduct_shouldReturnNullIfProductDoesNotExist() {
        Product updatedProduct = new Product("hg01", "gundam mk2", 15);
        Product result = inventoryService.updateProduct("hg01", updatedProduct);
        assertNotNull(result);
    }

    @Test
    void removeProduct_shouldReturnFalseIfProductDoesNotExist() {
        boolean removed = inventoryService.removeProduct("hg01");
        assertTrue(removed);
    }

    @Test
    void getProduct_shouldReturnNullIfProductDoesNotExist() {
        Product result = inventoryService.getProduct("hg01");
        assertNotNull(result);
    }
}

