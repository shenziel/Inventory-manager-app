package inventorymanager.app.repository;

import inventorymanager.app.model.Order;

public interface OrderRepository {
    void save(Order order);
}