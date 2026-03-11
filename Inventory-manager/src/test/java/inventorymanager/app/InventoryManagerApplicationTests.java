package inventorymanager.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class InventoryManagerApplicationTests {
    // comment here
    @Test
    void contextLoads() {
    }

    @DisplayName("Test check for Isogram")
    @Test
    public void testCheckForIsogram() {
        assertEquals(false, InventoryManagerApplication.checkIsogram("GeekForGeek"));
        assertEquals(true, InventoryManagerApplication.checkIsogram("algorithm"));
        assertEquals(false, InventoryManagerApplication.checkIsogram("datastructures"));
        assertEquals(false, InventoryManagerApplication.checkIsogram("machinelearning"));
        assertEquals(true, InventoryManagerApplication.checkIsogram("importance"));
    }

    @Test
    void simpleArithmeticTest() {
        // TODO: [Delete]
        assertEquals(2, InventoryManagerApplication.addition());
    }

}
