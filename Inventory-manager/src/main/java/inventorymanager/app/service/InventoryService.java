package inventorymanager.app.service;
import inventorymanager.app.exception.ForbiddenException;
import inventorymanager.app.model.Product;
import inventorymanager.app.model.User;
import inventorymanager.app.model.UserRoles;
import inventorymanager.app.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InventoryService {
    private final Map<String, Product> inventory = new HashMap<>();
    private int idCounter = 1;

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public void addProduct(String name, double price, int quantity, LocalDate expiry) {
        String id = "hg0" + idCounter;
        if (!inventory.containsKey(id)) {
            Product product = new Product(id, name, price);
            product.setQuantity(quantity);
            product.setExpiry(expiry);
            inventory.put(id, product);
            idCounter++;
        }
    }

    public int getInventorySize() {
        return inventory.size();
    }

   public Product getProduct(String id) {
        return inventory.get(id);
    }

    public boolean removeProduct(String id) {
        if (inventory.containsKey(id)) {
            inventory.remove(id);
            return true;
        }
        return false;
    }

    public Product updateProduct(String id, Product updated) {
        if (inventory.containsKey(id)) {
            inventory.put(id, updated);
            return updated;
        }
        return null;
    }

    public boolean isLowStock(String name) {
        for (Product product : inventory.values()) {
            if (product.getQuantity() <= 10) {
                return true;
            }
        }
        return false;
    }

    public boolean isAboutToExpire(String id) {
        LocalDate today = LocalDate.now();
        for (Product product : inventory.values()) {
            if (product.getExpiry() != null &&
                    product.getExpiry().isBefore(today.plusDays(3))) {
                return true;
            }
        }
        return false;
    }

    public int getProductQuantity(UUID productId, User user) {
        if (user.getRole() != UserRoles.MANAGER) {
            throw new ForbiddenException("Only managers can check product quantity");
        }

        return repository.getQuantity(productId);
    }

    public void clearInventory() {
        inventory.clear();
        idCounter = 1;
    }
}
