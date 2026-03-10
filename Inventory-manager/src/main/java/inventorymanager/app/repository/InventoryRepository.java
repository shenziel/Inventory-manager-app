package inventorymanager.app.repository;

import inventorymanager.app.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository  extends MongoRepository<Product, String> {
}
