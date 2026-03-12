package inventorymanager.app.integration;

import inventorymanager.app.model.Product;
import inventorymanager.app.repository.InventoryRepository;
import inventorymanager.app.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration Tests for ProductController")
class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private InventoryService inventoryService;

    private static final String BASE_PATH = "/api/products";


    @BeforeEach
    void setUp() {
        inventoryService.clearInventory();
    }

    private String url(String path) {
        return "http://localhost:" + port + BASE_PATH + path;
    }

    @Test
    @DisplayName("Add product and verify inventory size")
    void addProduct_and_verifySize() {
        String expiry = LocalDate.now().plusDays(10).toString();
        String url = "http://localhost:" + port + BASE_PATH + "?name=TestProduct&price=10.5&quantity=5&expiry=" + expiry;

        ResponseEntity<Void> resp = restTemplate.postForEntity(url, null, Void.class);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(1, inventoryService.getInventorySize());
    }

    @Test
    @DisplayName("Get product by id")
    void getProductById() {
        inventoryService.addProduct("ProdA", 5.0, 3, LocalDate.now().plusDays(5));

        ResponseEntity<Product> resp = restTemplate.getForEntity(url("/hg01"), Product.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Product p = resp.getBody();
        assertNotNull(p);
        assertEquals("hg01", p.getId());
        assertEquals("ProdA", p.getName());
    }

    @Test
    @DisplayName("Delete product and verify")
    void deleteProduct() {
        inventoryService.addProduct("ProdDel", 12.0, 2, LocalDate.now().plusDays(2));

        ResponseEntity<Boolean> resp = restTemplate.exchange(url("/hg01"), HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(Boolean.TRUE.equals(resp.getBody()));
        assertEquals(0, inventoryService.getInventorySize());
    }

    @Test
    @DisplayName("Update product")
    void updateProduct() {
        inventoryService.addProduct("Old", 1.0, 1, LocalDate.now().plusDays(1));

        Product updated = new Product("hg01", "Old", 1.0);
        updated.setName("NewName");
        updated.setPrice(9.99);
        updated.setQuantity(10);
        updated.setExpiry(LocalDate.now().plusDays(20));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> req = new HttpEntity<>(updated, headers);

        ResponseEntity<Product> resp = restTemplate.exchange(url("/hg01"), HttpMethod.PUT, req, Product.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Product p = resp.getBody();
        assertNotNull(p);
        assertEquals("hg01", p.getId());
        assertEquals("NewName", p.getName());
    }

    @Test
    @DisplayName("Low stock detection")
    void lowStockDetection() {
        inventoryService.addProduct("Low", 5.0, 5, LocalDate.now().plusDays(5));

        ResponseEntity<Boolean> resp = restTemplate.getForEntity("http://localhost:" + port + BASE_PATH + "/stock/low?name=Low", Boolean.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(Boolean.TRUE.equals(resp.getBody()));
    }

    @Test
    @DisplayName("Expiry detection")
    void expiryDetection() {
        inventoryService.addProduct("Soon", 3.0, 1, LocalDate.now().plusDays(2));

        ResponseEntity<Boolean> resp = restTemplate.getForEntity(url("/hg01/expiry"), Boolean.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(Boolean.TRUE.equals(resp.getBody()));
    }
}
