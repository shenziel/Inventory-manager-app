package inventorymanager.app.service;

import inventorymanager.app.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupplierService {
    private final Map<String, Supplier> store = new HashMap<>();

    public void addSupplier(Supplier supplier){
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
        store.put(id, supplier);
    }

    public Supplier getSupplier(String id){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        return store.get(id);
    }

    public boolean removeSupplier(String id){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (!store.containsKey(id)) {
            return false;
        }
        store.remove(id);
        return true;
    }

    public Supplier updateSupplier(String id, Supplier updated){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or blank");
        }
        if (!store.containsKey(id)) {
            throw new RuntimeException("supplier not found");
        }
        Supplier existing = store.get(id);
        existing.setName(updated.getName());
        return existing;
    }

    public List<Supplier> listSuppliers(){
        return new ArrayList<>(store.values());
    }

    // helper to clear state between tests
    public void clear() {
        store.clear();
    }
}

