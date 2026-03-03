package inventorymanager.app.service;
import inventorymanager.app.model.Product;
import java.time.LocalDate;

public class InventoryService {
    //later when tests are done
    public void addProduct(Product product, int quantity) {

    }

    public void addProduct(Product product, int quantity, LocalDate expiry) {}

    Product getProduct(String id) {
        return null;
    }

    boolean removeProduct(String id) {
        return false;
    }

    Product updateProduct(String id, Product updated) {
        return null;
    }

    public boolean isLowStock(Product product) {
        return false;
    }
}