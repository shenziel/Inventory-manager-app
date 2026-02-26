package inventorymanager.app.services;

import inventorymanager.app.model.User;
import inventorymanager.app.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testAddUser_ReturnsTrueAndCreatesUser() {
        User user = new User(1L, "user1", "password123");
        boolean result = userService.addUser(user);
        assertTrue(result);
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testAddUser_shouldNotAddDuplicateUser() {
        User user = new User(1L, "user1", "password123");
        boolean addUser = userService.addUser(user);
        boolean addUser1 = userService.addUser(user);
        assertTrue(addUser);
        assertFalse(addUser1);
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testAddUser_shouldNotAddNullUserAndThrowException() {
        boolean result = userService.addUser(null);
        assertFalse(result);
        assertThrows(NullPointerException.class, () -> userService.addUser(null));
        assertEquals(0, userService.getAllUsers());
    }

    @Test
    void testGetUser_ReturnsUser() {
        User user = new User(1L, "user1", "password123");
        userService.addUser(user);
        User userReturned = userService.getUser(1L);
        assertNotNull(userReturned);
        assertEquals(user, userReturned);
    }

    @Test
    void testGetAllUsers_ReturnsAllUsers() {
        User user = new User(1L, "user1", "password123");
        userService.addUser(user);
        List<User> users = userService.getUsersList();
        assertNotNull(users);
    }

    @Test
    void testRemoveUser_ReturnsTrueAndRemovesUser() {
        User user = new User(1L, "user1", "password123");
        userService.addUser(user);
        User user1 = new User(2L, "user2", "password123");
        userService.addUser(user1);
        boolean result = userService.removeUser(1L);
        assertTrue(result);
        assertEquals(1, userService.getAllUsers());
    }

    @Test
    void testRemoveUser_ReturnsFalseForNonExistingUserAndThrowException() {
        boolean result = userService.removeUser(999L);
        assertThrows(NullPointerException.class, () -> userService.removeUser(999L));
        assertFalse(result);
    }

}
