package inventorymanager.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import inventorymanager.app.model.Supplier;
import inventorymanager.app.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Tag("integration")
@SpringBootTest
@DisplayName("Integration Tests for SupplierController")
class SupplierControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private SupplierService supplierService;

    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/suppliers";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        if (supplierService != null) {
            supplierService.clear();
        }
    }

    @Test
    @DisplayName("Should add a supplier successfully")
    void testAddSupplier_Success() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .param("id", "s1")
                .param("name", "Acme Corp")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should retrieve suppliers size")
    void testGetSuppliers_Empty() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(0)));
    }

    @Test
    @DisplayName("Should retrieve a supplier by ID")
    void testGetSupplier_Success() throws Exception {
        supplierService.addSupplier(new Supplier("s1", "Acme Corp"));

        mockMvc.perform(get(BASE_URL + "/s1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("s1")))
                .andExpect(jsonPath("$.name", is("Acme Corp")));
    }

    @Test
    @DisplayName("Should return 404 for non-existent supplier")
    void testGetSupplier_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete a supplier successfully")
    void testDeleteSupplier_Success() throws Exception {
        supplierService.addSupplier(new Supplier("s1", "Acme Corp"));

        mockMvc.perform(delete(BASE_URL + "/s1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName("Should return false when deleting non-existent supplier")
    void testDeleteSupplier_NotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    @DisplayName("Should update a supplier successfully")
    void testUpdateSupplier_Success() throws Exception {
        supplierService.addSupplier(new Supplier("s1", "OldName"));

        Supplier updated = new Supplier("ignored", "NewName");

        mockMvc.perform(put(BASE_URL + "/s1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("s1")))
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent supplier")
    void testUpdateSupplier_NotFound() throws Exception {
        Supplier updated = new Supplier("s999", "NoOne");

        mockMvc.perform(put(BASE_URL + "/s999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }
}



