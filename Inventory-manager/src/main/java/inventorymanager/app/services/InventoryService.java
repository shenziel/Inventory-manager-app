package inventorymanager.app.services;
import inventorymanager.app.services;
import inventorymanager.app.Product;

public interface InventoryService {
//later when tests are done
    void addProduct(Product product, int quantity);
    Product getProduct(String id);
    boolean removeProduct(String id);
    Product updateProduct(String id, Product updated);
    boolean isLowStock(Product product);
}