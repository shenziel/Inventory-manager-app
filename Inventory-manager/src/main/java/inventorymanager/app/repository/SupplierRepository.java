package inventorymanager.app.repository;

import java.util.UUID;

public interface SupplierRepository {
    boolean existsById(UUID supplierId);

    boolean suppliesProduct(UUID supplierId, UUID productId);
}
