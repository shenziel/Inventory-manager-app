package inventorymanager.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import inventorymanager.app.dto.RegisterRequest;
import inventorymanager.app.model.User;
import inventorymanager.app.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/users";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        // Clear users before each test
        userService.getUsersList().clear();
    }

    @Test
    void testRegisterAdmin() throws Exception {
        RegisterRequest request = new RegisterRequest("adminUser", "password");

        mockMvc.perform(post(BASE_URL + "/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("adminUser"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testRegisterManager() throws Exception {
        RegisterRequest request = new RegisterRequest("manager1", "password");

        mockMvc.perform(post(BASE_URL + "/register/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("manager1"))
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        userService.registerAdmin("testAdmin", "pass");

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = userService.registerAdmin("user1", "pass");

        mockMvc.perform(get(BASE_URL + "/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserByUsername() throws Exception {
        userService.registerManager("myUser", "pass");

        mockMvc.perform(get(BASE_URL + "/username/myUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("myUser"));
    }

    @Test
    void testGetUserByUsername_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/username/nope"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = userService.registerAdmin("deleteMe", "pass");

        mockMvc.perform(delete(BASE_URL + "/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUsersCount() throws Exception {
        userService.registerAdmin("a", "pass");
        userService.registerManager("b", "pass");

        mockMvc.perform(get(BASE_URL + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

    @Test
    void testHasRole() throws Exception {
        User user = userService.registerAdmin("roleGuy", "pass");

        mockMvc.perform(get(BASE_URL + "/" + user.getId() + "/hasRole/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}