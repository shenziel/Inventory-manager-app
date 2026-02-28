package inventorymanager.app.service;

import inventorymanager.app.model.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testCreateUser_ReturnsTrueAndCreatesUser() {
        User result = userService.registerManager("user1", "password123");
        assertNotNull(result);
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testAddUser_shouldNotCreateDuplicateUser() {
        User addUser = userService.registerManager("user1", "password123");
        User addUser1 = userService.registerManager("user1", "password123");
        assertNotNull(addUser);
        assertNull(addUser1);
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testAddUser_shouldNotCreateUserWithNullEmailAndThrowException() {
        assertThrows(NullPointerException.class, () -> userService.registerManager(null, "password123"));
        assertEquals(0, userService.getAllUsers());
    }

    @Test
    void testAddUser_shouldNotCreateUserWithNullPasswordAndThrowException() {
        assertThrows(NullPointerException.class, () -> userService.registerManager("user1", null));
        assertEquals(0, userService.getAllUsers());
    }

    @Test
    void testGetUserById_ReturnsExistingUser() {
        User user = new User(1L, "user1", "password123");
        userService.registerManager("user1", "password123");
        User userReturned = userService.getUserById(1L);
        assertNotNull(userReturned);
        assertEquals(user, userReturned);
    }

    @Test
    void testGetUserById_ThrowsExceptionForNullUser() {
        assertThrows(NullPointerException.class, () -> userService.getUserById(null));
    }

    @Test
    void testGetUserById_ReturnsNullForNonExistingUser() {
        assertThrows(NullPointerException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testGetAllUsers_ReturnsAllUsers() {
        userService.registerManager("user1", "password123");
        Map<Long, User> users = userService.getUsersList();
        assertEquals(1, users.size());
    }

    @Test
    void testRemoveUser_ReturnsTrueAndRemovesUser() {
        userService.registerManager("user1", "password123");
        userService.registerManager("user1", "password123");
        boolean result = userService.removeUser(1L);
        assertTrue(result);
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testRemoveUser_ReturnsFalseForNonExistingUser() {
        assertThrows(NullPointerException.class, () -> userService.removeUser(999L));
    }

}
