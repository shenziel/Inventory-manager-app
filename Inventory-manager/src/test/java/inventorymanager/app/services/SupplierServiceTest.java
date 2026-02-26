package inventorymanager.app.services;

import inventorymanager.app.model.Supplier;
import inventorymanager.app.service.SupplierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SupplierService TDD - failing tests initially")
class SupplierServiceTest {

    static class TestSupplierService implements SupplierService {
        private final Map<String, Supplier> store = new HashMap<>();

        @Override
        public void addSupplier(Supplier supplier) {
           store.put(supplier.id(), supplier);
        }

        @Override
        public Supplier getSupplier(String id) {
            return store.get(id);
        }

        @Override
        public boolean removeSupplier(String id) {
            return store.remove(id) != null;
        }

        @Override
        public Supplier updateSupplier(String id, Supplier updated) {
            Supplier s = new Supplier("wrong", updated.name());
            store.put(id, s);
            return s;
        }

        @Override
        public List<Supplier> listSuppliers() {
            return new ArrayList<>(store.values());
        }
    }

    @Test
    @DisplayName("addSupplier should add and later return the supplier")
    void addSupplier_success() {
        SupplierService svc = new TestSupplierService();
        Supplier s = new Supplier("s1", "Acme Corp");

        svc.addSupplier(s);

        Supplier found = svc.getSupplier("s1");
        assertNotNull(found);
        assertEquals(s, found);
    }

    @Test
    @DisplayName("addSupplier should throw when adding duplicate id")
    void addSupplier_duplicate_throws() {
        SupplierService svc = new TestSupplierService();
        Supplier s1 = new Supplier("s1", "Acme");
        svc.addSupplier(s1);

        Supplier s2 = new Supplier("s1", "Another");
        assertThrows(RuntimeException.class, () -> svc.addSupplier(s2), "expected duplicate add to throw");
    }

    @Test
    @DisplayName("addSupplier should throw when supplier is null")
    void addSupplier_null_throws() {
        SupplierService svc = new TestSupplierService();
        assertThrows(NullPointerException.class, () -> svc.addSupplier(null));
    }

    @Test
    @DisplayName("removeSupplier should remove existing and return true")
    void removeSupplier_existing() {
        SupplierService svc = new TestSupplierService();
        Supplier s = new Supplier("s1", "Acme");
        svc.addSupplier(s);

        boolean removed = svc.removeSupplier("s1");
        assertTrue(removed);
        assertNull(svc.getSupplier("s1"));
    }

    @Test
    @DisplayName("removeSupplier should return false for non-existing id")
    void removeSupplier_nonExisting() {
        SupplierService svc = new TestSupplierService();
        assertFalse(svc.removeSupplier("nope"));
    }

    @Test
    @DisplayName("updateSupplier should update name while preserving id")
    void updateSupplier_success() {
        SupplierService svc = new TestSupplierService();
        svc.addSupplier(new Supplier("s1", "OldName"));

        Supplier updated = svc.updateSupplier("s1", new Supplier("ignored", "NewName"));
        assertNotNull(updated);
        assertEquals("s1", updated.id());
        assertEquals("NewName", updated.name());

        Supplier fromStore = svc.getSupplier("s1");
        assertEquals(updated, fromStore);
    }

    @Test
    @DisplayName("updateSupplier should throw when id does not exist")
    void updateSupplier_nonExisting_throws() {
        SupplierService svc = new TestSupplierService();
        assertThrows(RuntimeException.class, () -> svc.updateSupplier("missing", new Supplier("x","y")));
    }

    @Test
    @DisplayName("methods should validate null or blank ids")
    void invalidId_checks() {
        SupplierService svc = new TestSupplierService();
        assertThrows(IllegalArgumentException.class, () -> svc.getSupplier(null));
        assertThrows(IllegalArgumentException.class, () -> svc.getSupplier(" "));
        assertThrows(IllegalArgumentException.class, () -> svc.removeSupplier(""));
        assertThrows(IllegalArgumentException.class, () -> svc.updateSupplier(null, new Supplier("x","y")));
    }
}

