package inventorymanager.app.repository;

import java.util.UUID;

public interface InventoryRepository {
    void setQuantity(UUID productId, int quantity);

    int getQuantity(UUID productId);
}
