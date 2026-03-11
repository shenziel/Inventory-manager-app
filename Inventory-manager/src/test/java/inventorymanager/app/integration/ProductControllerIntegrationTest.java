package inventorymanager.app.integration;

import inventorymanager.app.model.Product;
import inventorymanager.app.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@DisplayName("Integration Tests for ProductController")
public class ProductControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private InventoryService inventoryService;

    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/products";

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the web application context
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Initialize ObjectMapper
        objectMapper = new ObjectMapper();

        // Clear the inventory before each test by creating a new InventoryService instance
        // Since the service uses an in-memory HashMap, we need to reset the state
        inventoryService = new InventoryService();
    }

    // ========== POST /api/products ==========

    @Test
    @DisplayName("Should add a product successfully")
    void testAddProduct_Success() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .param("name", "Laptop")
                .param("price", "1000.50")
                .param("quantity", "15")
                .param("expiry", LocalDate.now().plusDays(30).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should add a product without expiry date")
    void testAddProduct_WithoutExpiry() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .param("name", "Monitor")
                .param("price", "299.99")
                .param("quantity", "20"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should add product with low stock quantity")
    void testAddProduct_WithLowStock() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .param("name", "USB Cable")
                .param("price", "5.99")
                .param("quantity", "5")
                .param("expiry", LocalDate.now().plusDays(60).toString()))
                .andExpect(status().isCreated());
    }

    // ========== GET /api/products ==========

    @Test
    @DisplayName("Should retrieve inventory size")
    void testGetInventorySize_Empty() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(0)));
    }

    @Test
    @DisplayName("Should retrieve inventory size after adding products")
    void testGetInventorySize_WithProducts() throws Exception {
        // Add products first
        inventoryService.addProduct("Product1", 100.0, 10, LocalDate.now().plusDays(30));
        inventoryService.addProduct("Product2", 200.0, 5, LocalDate.now().plusDays(45));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(2)));
    }

    // ========== GET /api/products/{id} ==========

    @Test
    @DisplayName("Should retrieve a product by ID")
    void testGetProduct_Success() throws Exception {
        // Add a product
        inventoryService.addProduct("Mouse", 25.50, 30, LocalDate.now().plusDays(90));

        mockMvc.perform(get(BASE_URL + "/hg01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("hg01")))
                .andExpect(jsonPath("$.name", is("Mouse")))
                .andExpect(jsonPath("$.price", is(25.50)))
                .andExpect(jsonPath("$.quantity", is(30)));
    }

    @Test
    @DisplayName("Should return 404 for non-existent product")
    void testGetProduct_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/hg999"))
                .andExpect(status().isNotFound());
    }

    // ========== DELETE /api/products/{id} ==========

    @Test
    @DisplayName("Should delete a product successfully")
    void testDeleteProduct_Success() throws Exception {
        // Add a product first
        inventoryService.addProduct("Keyboard", 75.00, 10, LocalDate.now().plusDays(60));

        mockMvc.perform(delete(BASE_URL + "/hg01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName("Should return false when deleting non-existent product")
    void testDeleteProduct_NotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/hg999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    @DisplayName("Should not find product after deletion")
    void testDeleteProduct_VerifyDeletion() throws Exception {
        // Add a product
        inventoryService.addProduct("Monitor", 299.99, 8, LocalDate.now().plusDays(90));

        // Delete it
        mockMvc.perform(delete(BASE_URL + "/hg01"))
                .andExpect(status().isOk());

        // Verify it's deleted
        mockMvc.perform(get(BASE_URL + "/hg01"))
                .andExpect(status().isNotFound());
    }

    // ========== PUT /api/products/{id} ==========

    @Test
    @DisplayName("Should update a product successfully")
    void testUpdateProduct_Success() throws Exception {
        // Add a product first
        inventoryService.addProduct("OldName", 100.0, 10, LocalDate.now().plusDays(30));

        Product updatedProduct = new Product("hg01", "NewName", 150.0);
        updatedProduct.setQuantity(20);
        updatedProduct.setExpiry(LocalDate.now().plusDays(60));

        mockMvc.perform(put(BASE_URL + "/hg01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("hg01")))
                .andExpect(jsonPath("$.name", is("NewName")))
                .andExpect(jsonPath("$.price", is(150.0)))
                .andExpect(jsonPath("$.quantity", is(20)));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent product")
    void testUpdateProduct_NotFound() throws Exception {
        Product product = new Product("hg999", "NonExistent", 100.0);

        mockMvc.perform(put(BASE_URL + "/hg999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }

    // ========== GET /api/products/stock/low ==========

    @Test
    @DisplayName("Should detect low stock correctly")
    void testIsLowStock_True() throws Exception {
        // Add a product with low stock (quantity <= 10)
        inventoryService.addProduct("LowStockItem", 50.0, 5, LocalDate.now().plusDays(30));

        mockMvc.perform(get(BASE_URL + "/stock/low")
                .param("name", "LowStockItem"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName("Should return false for products with sufficient stock")
    void testIsLowStock_False() throws Exception {
        // Add a product with sufficient stock
        inventoryService.addProduct("HighStockItem", 50.0, 50, LocalDate.now().plusDays(30));

        mockMvc.perform(get(BASE_URL + "/stock/low")
                .param("name", "HighStockItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    @DisplayName("Should return false when no products exist")
    void testIsLowStock_NoProducts() throws Exception {
        mockMvc.perform(get(BASE_URL + "/stock/low")
                .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    // ========== GET /api/products/{id}/expiry ==========

    @Test
    @DisplayName("Should detect product expiring soon")
    void testIsAboutToExpire_True() throws Exception {
        // Add a product that expires soon (within 3 days)
        inventoryService.addProduct("SoonToExpire", 30.0, 10, LocalDate.now().plusDays(2));

        mockMvc.perform(get(BASE_URL + "/hg01/expiry"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName("Should return false for product not expiring soon")
    void testIsAboutToExpire_False() throws Exception {
        // Add a product that expires later (more than 3 days)
        inventoryService.addProduct("FreshProduct", 40.0, 15, LocalDate.now().plusDays(10));

        mockMvc.perform(get(BASE_URL + "/hg01/expiry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    @DisplayName("Should return false for product with no expiry date")
    void testIsAboutToExpire_NoExpiry() throws Exception {
        // Add a product without expiry date
        inventoryService.addProduct("NoExpiryProduct", 25.0, 20, null);

        mockMvc.perform(get(BASE_URL + "/hg01/expiry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    @DisplayName("Should return false when no products exist")
    void testIsAboutToExpire_NoProducts() throws Exception {
        mockMvc.perform(get(BASE_URL + "/hg999/expiry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("Integration: Complete product lifecycle")
    void testCompleteProductLifecycle() throws Exception {
        // 1. Add a product
        mockMvc.perform(post(BASE_URL)
                .param("name", "Tablet")
                .param("price", "500.00")
                .param("quantity", "12")
                .param("expiry", LocalDate.now().plusDays(45).toString()))
                .andExpect(status().isCreated());

        // 2. Verify inventory size increased
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));

        // 3. Retrieve the product
        mockMvc.perform(get(BASE_URL + "/hg01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tablet")))
                .andExpect(jsonPath("$.price", is(500.0)));

        // 4. Update the product
        Product updatedProduct = new Product("hg01", "Premium Tablet", 550.00);
        updatedProduct.setQuantity(15);
        updatedProduct.setExpiry(LocalDate.now().plusDays(60));

        mockMvc.perform(put(BASE_URL + "/hg01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Premium Tablet")));

        // 5. Delete the product
        mockMvc.perform(delete(BASE_URL + "/hg01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        // 6. Verify product is deleted
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(0)));
    }

    @Test
    @DisplayName("Integration: Multiple products management")
    void testMultipleProductsManagement() throws Exception {
        // Add multiple products
        mockMvc.perform(post(BASE_URL)
                .param("name", "Product1")
                .param("price", "100.0")
                .param("quantity", "5")
                .param("expiry", LocalDate.now().plusDays(20).toString()))
                .andExpect(status().isCreated());

        mockMvc.perform(post(BASE_URL)
                .param("name", "Product2")
                .param("price", "200.0")
                .param("quantity", "25")
                .param("expiry", LocalDate.now().plusDays(90).toString()))
                .andExpect(status().isCreated());

        mockMvc.perform(post(BASE_URL)
                .param("name", "Product3")
                .param("price", "150.0")
                .param("quantity", "8")
                .param("expiry", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isCreated());

        // Verify inventory size
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(3)));

        // Check low stock alerts
        mockMvc.perform(get(BASE_URL + "/stock/low")
                .param("name", "Product1"))
                .andExpect(jsonPath("$", is(true))); // quantity 5 is low

        // Check expiry alerts
        mockMvc.perform(get(BASE_URL + "/hg03/expiry"))
                .andExpect(jsonPath("$", is(true))); // expiring in 1 day
    }
}
