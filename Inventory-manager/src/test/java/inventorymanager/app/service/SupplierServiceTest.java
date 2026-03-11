package inventorymanager.app.service;

import inventorymanager.app.model.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SupplierService TDD - failing tests initially")
class SupplierServiceTest {

    private SupplierService svc;

    @BeforeEach
    void setUp() {
        svc = new SupplierService();
    }


    @Test
    @DisplayName("addSupplier should add and later return the supplier")
    void addSupplier_success() {
        Supplier s = addsupplier();

        Supplier found = svc.getSupplier("s1");
        assertNotNull(found);
        assertEquals(s, found);
    }

    @Test
    @DisplayName("addSupplier should throw when adding duplicate id")
    void addSupplier_duplicate_throws() {
        addsupplier();

        Supplier s2 = new Supplier("s1", "Another");
        assertThrows(RuntimeException.class, () -> svc.addSupplier(s2), "expected duplicate add to throw");
    }

    @Test
    @DisplayName("addSupplier should throw when supplier is null")
    void addSupplier_null_throws() {
        assertThrows(NullPointerException.class, () -> svc.addSupplier(null));
    }

    @Test
    @DisplayName("removeSupplier should remove existing and return true")
    void removeSupplier_existing() {
        addsupplier();

        boolean removed = svc.removeSupplier("s1");
        assertTrue(removed);
        assertNull(svc.getSupplier("s1"));
    }

    @Test
    @DisplayName("removeSupplier should return false for non-existing id")
    void removeSupplier_nonExisting() {
        addsupplier();
        // try removing an id that does not exist
        boolean removed = svc.removeSupplier("missing");
        assertFalse(removed);
    }

    @Test
    @DisplayName("updateSupplier should update name while preserving id")
    void updateSupplier_success() {
        svc.addSupplier(new Supplier("s1", "OldName"));

        Supplier updated = svc.updateSupplier("s1", new Supplier("ignored", "NewName"));
        assertNotNull(updated);
        assertEquals("s1", updated.getId());
        assertEquals("NewName", updated.getName());

        Supplier fromStore = svc.getSupplier("s1");
        assertEquals(updated, fromStore);
    }

    @Test
    @DisplayName("updateSupplier should throw when id does not exist")
    void updateSupplier_nonExisting_throws() {
        assertThrows(RuntimeException.class, () -> svc.updateSupplier("missing", new Supplier("x","y")));
    }

    @Test
    @DisplayName("methods should validate null or blank ids")
    void invalidId_checks() {
        assertThrows(IllegalArgumentException.class, () -> svc.getSupplier(null));
        assertThrows(IllegalArgumentException.class, () -> svc.getSupplier(" "));
        assertThrows(IllegalArgumentException.class, () -> svc.removeSupplier(""));
        assertThrows(IllegalArgumentException.class, () -> svc.updateSupplier(null, new Supplier("x","y")));
    }

    private Supplier addsupplier() {
        Supplier s = new Supplier("s1", "Acme Corp");

        svc.addSupplier(s);
        return s;
    }
}
