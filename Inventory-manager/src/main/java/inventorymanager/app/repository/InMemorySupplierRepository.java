package inventorymanager.app.repository;

import inventorymanager.app.model.Supplier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemorySupplierRepository implements SupplierRepository {

    private final Map<String, Supplier> store = new HashMap<>();

    @Override
    public Supplier save(Supplier supplier) {
        store.put(supplier.getId(), supplier);
        return supplier;
    }

    @Override
    public Supplier findById(String supplierId) {
        return store.get(supplierId);
    }

    @Override
    public boolean existsById(String supplierId) {
        return store.containsKey(supplierId);
    }

    @Override
    public List<Supplier> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(String supplierId) {
        store.remove(supplierId);
    }
}