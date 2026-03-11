package inventorymanager.app.service;
import inventorymanager.app.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InventoryService {
    //later when tests are done
    public void addProduct(String name, double price, int quantity, LocalDate expiry) {}

    Product getProduct(String id) {
        return null;
    }

    boolean removeProduct(String id) {
        return false;
    }

    Product updateProduct(String id, Product updated) {
        return null;
    }

    public boolean isLowStock(String name) {
        return false;
    }

    public boolean isAboutToExpire(String id) {
        return false;
    }
}