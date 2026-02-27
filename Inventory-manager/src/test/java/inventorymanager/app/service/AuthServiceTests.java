package inventorymanager.app.service;

import inventorymanager.app.model.User;
import inventorymanager.app.model.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTests {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService);
    }

    @Test
    void testUserLogin_shouldReturnUserWhenLoginSuccessful() {
        // Setup
        String username = "test@example.com";
        String password = "password123";

        // Act
        boolean result = authService.login(username, password);

        User userResult = userService.getUserByUsername(username);
        assertTrue(result);
        assertEquals(username, userResult.getUsername());
    }

    @Test
    void testUserLogin_shouldThrowWhenCredentialsInvalid() {
        // Setup
        String username = "username1";
        String password = "wrongpass";

        // Act
        assertThrows(Exception.class, () -> authService.login(username, password));
        boolean result = authService.login(username, password);

        assertFalse(result);
    }

    @Test
    void testUserLogin_shouldThrowWhenUsernameIsNull() {
        // Setup
        String password = "password123";
        assertThrows(NullPointerException.class, () -> authService.login(null, password));
    }

    @Test
    void testRegisterManager_shouldCreateNewManagerUserIfNonExisting() {
        // Setup
        String username = "newuser@example.com";
        String password = "newpassword";

        User manager = authService.registerManager(username, password);

        assertNotNull(manager);
        assertEquals(UserRoles.MANAGER, manager.getRole());
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testRegisterAdmin_shouldCreateNewAdminUserIfNonExisting() {
        // Setup
        String username = "newuser@example.com";
        String password = "newpassword";

        User admin = authService.registerAdmin(username, password);

        assertNotNull(admin);
        assertEquals(UserRoles.ADMIN, admin.getRole());
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testRegister_shouldNotCreateUserIfAlreadyExistsAndThrow() {
        // Setup
        String username = "user1";
        String password = "password123";
        User existingUser = new User(1L, username, password);
        when(userService.getUserByUsername(username)).thenReturn(existingUser);
        when(userService.validateUser(existingUser)).thenReturn(true);
        assertThrows(NullPointerException.class, () -> authService.registerManager(username, password));
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testLogout_shouldReturnTrueWhenSuccessful() {
        // Setup
        String username = "test@example.com";

        // Act
        boolean result = authService.logout(username);

        assertTrue(result);
        verify(userService, times(1)).logout(username);
    }

    @Test
    void testDeleteUser_shouldCallUserServiceDelete() {
        // Setup
        Long id = 42L;
        User user = new User(id, "user1", "password123");
        User user1 = new User(41L, "user2", "password123");
        Map<Long, User> users = new HashMap<>();
        users.put(id, user);
        users.put(41L, user1);
        when(userService.getUsersList()).thenReturn(users);
        // Act
        authService.deleteUser(id);

        // Assert
        assertNotEquals(users, userService.getUsersList());
        assertEquals(1, userService.getAllUsers());
    }
}
