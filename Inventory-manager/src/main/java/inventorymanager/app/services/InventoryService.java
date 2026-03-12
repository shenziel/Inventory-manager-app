package inventorymanager.app.services;

import java.util.UUID;
import inventorymanager.app.repository.*;
import inventorymanager.app.*;

public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public int getProductQuantity(UUID productId, User user) {
        if (user.getRole() != Role.MANAGER) {
            throw new ForbiddenException("Only managers can check product quantity");
        }

        return repository.getQuantity(productId);
    }
}