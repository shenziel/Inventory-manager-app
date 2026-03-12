package inventorymanager.app.repository;

import inventorymanager.app.model.Supplier;

import java.util.List;

public interface SupplierRepository {

    Supplier save(Supplier supplier);

    Supplier findById(String supplierId);

    boolean existsById(String supplierId);

    List<Supplier> findAll();

    void deleteById(String supplierId);


}
