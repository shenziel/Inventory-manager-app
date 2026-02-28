package inventorymanager.app.repository;

import inventorymanager.app.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository  extends MongoRepository<Supplier, String> {
}
