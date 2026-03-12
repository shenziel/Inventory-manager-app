package inventorymanager.app.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryInventoryRepository implements InventoryRepository {

    private final Map<UUID, Integer> quantities = new HashMap<>();

    @Override
    public void setQuantity(UUID productId, int quantity) {
        quantities.put(productId, quantity);
    }

    @Override
    public int getQuantity(UUID productId) {
        return quantities.getOrDefault(productId, 0);
    }
}