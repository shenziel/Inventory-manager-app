package inventorymanager.app.service;
import inventorymanager.app.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTests {
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }
}
