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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTests {
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

//    @Test
    void testUserLogin_shouldReturnUserWhenLoginSuccessful() {
        // Setup
        String username = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        User user = new User(username, encodedPassword, UserRoles.MANAGER);
        when(userService.checkUserCredentials(username, password)).thenReturn(user);

        // Act
        User result = authService.login(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

//    @Test
    void testUserLogin_shouldThrowWhenCredentialsInvalid() {
        // Setup
        String username = "username1";
        String password = "wrongpass";
        userService.registerManager(username, "rightPassword");

        // Act
        assertThrows(InputMismatchException.class, () -> authService.login(username, password));
    }

//    @Test
    void testUserLogin_shouldThrowWhenUsernameIsNull() {
        // Setup
        String password = "password123";
        assertThrows(IllegalArgumentException.class, () -> authService.login(null, password));
    }

//    @Test
    void testLogout_shouldReturnTrueWhenSuccessful() {
        // Setup
        String userId = "123";
        String username = "test@example.com";
        String encodedPassword = "encodedPassword";
        User user = new User(username, encodedPassword, UserRoles.MANAGER);
        user.setId(userId);
        user.setLoggedIn(true);
        when(userService.getUserById(userId)).thenReturn(user);

        // Act
        boolean result = authService.logout(userId);

        assertTrue(result);
        assertFalse(user.isLoggedIn());
    }

//    @Test
    void testLogout_shouldThrowWhenNotLoggedIn() {
        // Setup
        String username = "test@example.com";
        User notLoggedInUser = new User(username, "password123", UserRoles.MANAGER);
        notLoggedInUser.setLoggedIn(false);
        String userId = "123";
        notLoggedInUser.setId(userId);
        when(userService.getUserById(userId)).thenReturn(notLoggedInUser);

        // Act
        assertThrows(IllegalStateException.class, () -> authService.logout(userId));
    }
}
