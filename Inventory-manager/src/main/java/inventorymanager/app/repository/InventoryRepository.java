package inventorymanager.app.repository;

import java.util.UUID;
import inventorymanager.app.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<Product, String> {
    void setQuantity(UUID productId, int quantity);

    int getQuantity(UUID productId);
}
