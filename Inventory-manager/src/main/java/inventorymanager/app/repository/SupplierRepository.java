package inventorymanager.app.repository;

import java.util.UUID;
import inventorymanager.app.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
    boolean existsById(UUID supplierId);

    boolean suppliesProduct(UUID supplierId, UUID productId);
}
