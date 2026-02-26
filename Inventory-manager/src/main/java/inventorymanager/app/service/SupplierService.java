package inventorymanager.app.service;

import inventorymanager.app.model.Supplier;

import java.util.List;

    // TODO: After test
public interface SupplierService {
    void addSupplier(Supplier supplier);
    Supplier getSupplier(String id);
    boolean removeSupplier(String id);
    Supplier updateSupplier(String id, Supplier updated);

    List<Supplier> listSuppliers();
}

