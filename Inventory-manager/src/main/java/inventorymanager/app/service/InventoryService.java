package inventorymanager.app.service;
import inventorymanager.app.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {
    //later when tests are done
    private Map<String, Product> inventory = new HashMap<>();
    private int idCounter = 1;

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

    Product getProduct(String id) {
        return inventory.get(id);
    }

    boolean removeProduct(String id) {
        if (inventory.containsKey(id)) {
            inventory.remove(id);
            return true;
        }
        return false;
    }

    Product updateProduct(String id, Product updated) {
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
}
