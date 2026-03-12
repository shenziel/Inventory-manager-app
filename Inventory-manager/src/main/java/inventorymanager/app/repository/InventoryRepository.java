package inventorymanager.app.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository {
    void setQuantity(UUID productId, int quantity);

    int getQuantity(UUID productId);
}
