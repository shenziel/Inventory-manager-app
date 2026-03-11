package inventorymanager.app.service;

import inventorymanager.app.model.User;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import inventorymanager.app.model.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(passwordEncoder);
        userService.setUsers(new ArrayList<>());
    }

    @Test
    void testRegisterManager_ReturnsTrueAndCreatesUserWithManagerRole() {
        String username = "user1";
        User result = userService.registerManager(username, "password123");
        assertNotNull(result);
        assertEquals(UserRoles.MANAGER, result.getRole());
    }

    @Test
    void testRegisterAdmin_ReturnsTrueAndCreatesUserWithAdminRole() {
        String username = "user1";
        String password = "password123";
        User result = userService.registerAdmin(username, password);
        assertNotNull(result);
        assertNotNull(userService.getUserByUsername(username));
        assertEquals(UserRoles.ADMIN, result.getRole());
    }

    @Test
    void testRegisterManager_shouldNotCreateDuplicateUser() {
        String username = "username1";
        String password = "password1";
        userService.registerManager(username, password);
        assertThrows(RuntimeException.class, () -> userService.registerManager(username, password));
        assertEquals(1, userService.getUsersCount());
        assertNotNull(userService.getUserByUsername(username));
    }

    @Test
    void testRegisterManager_shouldNotCreateUserWithNullEmailAndThrowException() {
        assertThrows(NullPointerException.class, () -> userService.registerManager(null, "password123"));
        assertEquals(0, userService.getUsersCount());
    }

    @Test
    void testRegisterManager_shouldNotCreateUserWithNullPasswordAndThrowException() {
        assertThrows(NullPointerException.class, () -> userService.registerManager("user1", null));
    }

    @Test
    void testGetUserById_ReturnsExistingUser() {
        User user = userService.registerManager("user1", "password123");
        User userReturned = userService.getUserById(user.getId());
        assertNotNull(userReturned);
        assertEquals(user.getUsername(), userReturned.getUsername());
    }

    @Test
    void testGetUserById_ThrowsExceptionForNullUser() {
        assertThrows(NullPointerException.class, () -> userService.getUserById(null));
    }

    @Test
    void testGetUserById_ReturnsNullForNonExistingUser() {
        assertThrows(NullPointerException.class, () -> userService.getUserById("999L"));
    }

    @Test
    void testGetAllUsers_ReturnsUsersCount() {
        userService.registerManager("user1", "password123");
        int users = userService.getUsersCount();
        assertEquals(1, users);
    }

    @Test
    void testRemoveUser_ReturnsTrueAndRemovesUser() {
        User user1 = userService.registerManager("user1", "password123");
        userService.registerManager("user2", "password123");
        assertEquals(2, userService.getUsersCount());
        boolean result = userService.removeUser(user1.getId());
        assertTrue(result);
        assertEquals(1, userService.getUsersCount());
        assertNull(userService.getUserByUsername("user1"));
    }

    @Test
    void testRemoveUser_ReturnsFalseForNonExistingUser() {
        assertFalse(userService.removeUser("999L"));
    }

}
