package inventorymanager.app.service;

import inventorymanager.app.model.User;
import static org.junit.jupiter.api.Assertions.*;

import inventorymanager.app.model.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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
        //setup
        String username = "user1";

        //Test
        User result = userService.registerManager(username, "password123");

        //Assert
        assertNotNull(result);
        assertEquals(UserRoles.MANAGER, result.getRole());
    }

    @Test
    void testRegisterAdmin_ReturnsTrueAndCreatesUserWithAdminRole() {
        //setup
        String username = "user1";
        String password = "password123";
        //Test
        User result = userService.registerAdmin(username, password);
        //Assert
        assertNotNull(result);
        assertNotNull(userService.getUserByUsername(username));
        assertEquals(UserRoles.ADMIN, result.getRole());
    }

    @Test
    void testRegisterManager_shouldNotCreateDuplicateUser() {
        //setup
        String username = "username1";
        String password = "password1";
        userService.registerManager(username, password);
        //Assert and Test
        assertThrows(RuntimeException.class, () -> userService.registerManager(username, password));
        assertEquals(1, userService.getUsersCount());
        assertNotNull(userService.getUserByUsername(username));
    }

    @Test
    void testRegisterManager_shouldNotCreateUserWithNullEmailAndThrowException() {
        //Assert and Test
        assertThrows(NullPointerException.class, () -> userService.registerManager(null, "password123"));
        assertEquals(0, userService.getUsersCount());
    }

    @Test
    void testRegisterManager_shouldNotCreateUserWithNullPasswordAndThrowException() {
        //Assert and Test
        assertThrows(NullPointerException.class, () -> userService.registerManager("user1", null));
    }

    @Test
    void testGetUserById_ReturnsExistingUser() {
        //setup
        User user = userService.registerManager("user1", "password123");
        User userReturned = userService.getUserById(user.getId());
        //Assert and Test
        assertNotNull(userReturned);
        assertEquals(user.getUsername(), userReturned.getUsername());
    }

    @Test
    void testGetUserById_ThrowsExceptionForNullUser() {
        //Assert and Test
        assertThrows(NullPointerException.class, () -> userService.getUserById(null));
    }

    @Test
    void testGetUserById_ReturnsNullForNonExistingUser() {
        //Assert and Test
        assertThrows(NullPointerException.class, () -> userService.getUserById("999L"));
    }

    @Test
    void testGetAllUsers_ReturnsUsersCount() {
        //setup
        userService.registerManager("user1", "password123");
        //Test
        int users = userService.getUsersCount();
        //Assert
        assertEquals(1, users);
    }

    @Test
    void testRemoveUser_ReturnsTrueAndRemovesUser() {
        //setup
        User user1 = userService.registerManager("user1", "password123");
        userService.registerManager("user2", "password123");
        //Test
        assertEquals(2, userService.getUsersCount());
        boolean result = userService.removeUser(user1.getId());
        //Assert
        assertTrue(result);
        assertEquals(1, userService.getUsersCount());
        assertNull(userService.getUserByUsername("user1"));
    }

    @Test
    void testRemoveUser_ReturnsFalseForNonExistingUser() {
        //Assert
        assertFalse(userService.removeUser("999L"));
    }

    @Test
    void testHasRole_ReturnsTrue() {
        //setup
        User user = userService.registerManager("user1", "password123");
        //Assert
        assertTrue(userService.hasRole(user.getId(), UserRoles.MANAGER));
    }

    @Test
    void testHasRole_ReturnsFalseForNonExistingUser() {
        //Assert
        assertFalse(userService.hasRole("999L", UserRoles.MANAGER));
    }

    @Test
    void testHasRole_ThrowsExceptionForNullUserId() {
        //Assert
        assertThrows(NullPointerException.class, () -> userService.hasRole(null, UserRoles.MANAGER));
    }

    @Test
    void testHasRole_ThrowsExceptionForNullUserRole() {
        //Assert
        assertThrows(NullPointerException.class, () -> userService.hasRole("999L", null));
    }

    @Test
    void testAssignRole_ReturnsTrueForTheCurrentRole() {
        //setup
        User user = userService.registerManager("user1", "password123");
        //Test
        userService.assignRole(user.getId(), UserRoles.ADMIN);
        //Assert
        assertFalse(userService.hasRole(user.getId(), UserRoles.MANAGER));
        assertTrue(userService.hasRole(user.getId(), UserRoles.ADMIN));
    }
}
