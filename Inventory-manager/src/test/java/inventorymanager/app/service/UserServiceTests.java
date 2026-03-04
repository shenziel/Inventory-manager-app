package inventorymanager.app.service;

import inventorymanager.app.model.User;
import static org.junit.jupiter.api.Assertions.*;

import inventorymanager.app.model.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(passwordEncoder);
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
        assertEquals(UserRoles.ADMIN, result.getRole());
    }

    @Test
    void testRegisterManager_shouldNotCreateDuplicateUser() {
        String username = "username1";
        String password = "password1";
        assertThrows(RuntimeException.class, () -> userService.registerManager(username, password));
        assertEquals(1, userService.getUsersCount());
    }

    @Test
    void testRegisterManager_shouldNotCreateUserWithNullEmailAndThrowException() {
        assertThrows(NullPointerException.class, () -> userService.registerManager(null, "password123"));
        assertEquals(0, userService.getUsersCount());
    }

    @Test
    void testRegisterManager_shouldNotCreateUserWithNullPasswordAndThrowException() {
        assertThrows(NullPointerException.class, () -> userService.registerManager("user1", null));
        assertEquals(0, userService.getUsersCount());
    }

    @Test
    void testGetUserById_ReturnsExistingUser() {
        User user = new User("user1", "password123");
        userService.registerManager("user1", "password123");
        User userReturned = userService.getUserById("1L");
        assertNotNull(userReturned);
        assertEquals(user, userReturned);
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
        userService.registerManager("user1", "password123");
        userService.registerManager("user1", "password123");
        boolean result = userService.removeUser("1L");
        assertTrue(result);
        assertEquals(1, userService.getUsersCount());
    }

    @Test
    void testRemoveUser_ReturnsFalseForNonExistingUser() {
        assertThrows(NullPointerException.class, () -> userService.removeUser("999L"));
    }

}
