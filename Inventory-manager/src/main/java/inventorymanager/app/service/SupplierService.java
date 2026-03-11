package inventorymanager.app.service;

import inventorymanager.app.model.Supplier;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierService {
    // simple in-memory store used for tests
    private final Map<String, Supplier> store = new HashMap<>();

    void addSupplier(Supplier supplier){
        if (supplier == null) {
            throw new NullPointerException("supplier cannot be null");
        }
        String id = supplier.getId();
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("supplier id cannot be null or blank");
        }
        if (store.containsKey(id)) {
            throw new RuntimeException("supplier with id already exists");
        }
        // store the same instance so identity checks in tests pass
        store.put(id, supplier);
    }

    Supplier getSupplier(String id){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        return store.get(id);
    }

    boolean removeSupplier(String id){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (!store.containsKey(id)) {
            return false;
        }
        store.remove(id);
        return true;
    }

    Supplier updateSupplier(String id, Supplier updated){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (!store.containsKey(id)) {
            throw new RuntimeException("supplier not found");
        }
        Supplier existing = store.get(id);
        // preserve id, update name
        existing.setName(updated.getName());
        return existing;
    }

    List<Supplier> listSuppliers(){
        return new ArrayList<>(store.values());
    }
}

