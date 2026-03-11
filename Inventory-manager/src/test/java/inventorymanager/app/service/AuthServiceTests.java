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

   // @Test
    void testUserLogin_shouldReturnUserWhenLoginSuccessful() {
        // Setup
        String username = "test@example.com";
        String password = "password123";
        String encriptedPassword = "password123encripted";

        // Act
        boolean result = authService.login(username, password);

        User userResult = userService.getUserByUsername(username);
        assertTrue(result);
        assertEquals(username, userResult.getUsername());
        assertNotEquals(password, encriptedPassword);
    }

   //@Test
    void testUserLogin_shouldThrowWhenCredentialsInvalid() {
        // Setup
        String username = "username1";
        String password = "wrongpass";

        // Act
        assertThrows(Exception.class, () -> authService.login(username, password));
        boolean result = authService.login(username, password);

        assertFalse(result);
    }

    //@Test
    void testUserLogin_shouldThrowWhenUsernameIsNull() {
        // Setup
        String password = "password123";
        assertThrows(NullPointerException.class, () -> authService.login(null, password));
    }

    //@Test
    void testRegisterManager_shouldCreateNewManagerUserIfNonExisting() {
        // Setup
        String username = "newuser@example.com";
        String password = "newpassword";

        User manager = authService.registerManager(username, password);

        assertNotNull(manager);
        assertEquals(UserRoles.MANAGER, manager.getRole());
        assertEquals(1, userService.getUsersCount());
    }

    //@Test
    void testRegisterAdmin_shouldCreateNewAdminUserIfNonExisting() {
        // Setup
        String username = "newuser@example.com";
        String password = "newpassword";

        User admin = authService.registerAdmin(username, password);

        assertNotNull(admin);
        assertEquals(UserRoles.ADMIN, admin.getRole());
        assertEquals(1, userService.getUsersCount());
    }

    //@Test
    void testRegister_shouldNotCreateUserIfAlreadyExistsAndThrow() {
        // Setup
        String username = "user1";
        String password = "password123";
        User existingUser = new User(username, password, UserRoles.MANAGER);
        when(userService.getUserByUsername(username)).thenReturn(existingUser);
        assertThrows(NullPointerException.class, () -> authService.registerManager(username, password));
        assertEquals(1, userService.getUsersCount());
        assertEquals(existingUser.getUsername(), userService.getUserByUsername(username).getUsername());
    }

    //@Test
    void testDeleteUser_shouldRemoveUserWhenSuccessful() {
        // Setup
        String id = "42L";
        User user = new User("user1", "password123", UserRoles.MANAGER);
        User user1 = new User("user2", "password123", UserRoles.ADMIN);
        Map<String, User> users = new HashMap<>();
        users.put(id, user);
        users.put("41L", user1);
        when(userService.getUsersList()).thenReturn(users);
        // Act
        authService.deleteUser(id);

        // Assert
        assertNotEquals(users, userService.getUsersList());
        assertEquals(1, userService.getUsersCount());
    }

    //@Test
    void testLogout_shouldReturnTrueWhenSuccessful() {
        // Setup
        String username = "test@example.com";

        // Act
        boolean result = authService.logout(username);

        assertTrue(result);
    }

    //@Test
    void testLogout_shouldThrowWhenNotLoggedIn() {
        // Setup
        String username = "test@example.com";
        User notLoggedInUser = new User(username, "password123", UserRoles.MANAGER);
        notLoggedInUser.setLoggedIn(false);
        when(userService.getUserByUsername(username)).thenReturn(notLoggedInUser);

        // Act
        assertThrows(NullPointerException.class, () -> authService.logout(username));
    }
}
