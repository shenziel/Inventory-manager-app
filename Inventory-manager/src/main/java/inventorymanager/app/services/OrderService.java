package inventorymanager.app.services;

import inventorymanager.app.ForbiddenException;
import inventorymanager.app.NotFoundException;
import inventorymanager.app.Order;
import inventorymanager.app.OrderReceipt;
import inventorymanager.app.Role;
import inventorymanager.app.User;
import inventorymanager.app.repository.*;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class OrderService {

    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;
    private final OrderRepository orderRepository;
    private final Clock clock;

    public OrderService(
            InventoryRepository inventoryRepository,
            SupplierRepository supplierRepository,
            OrderRepository orderRepository,
            Clock clock) {
        this.inventoryRepository = inventoryRepository;
        this.supplierRepository = supplierRepository;
        this.orderRepository = orderRepository;
        this.clock = clock;
    }

    public OrderReceipt orderProduct(UUID productId, UUID supplierId, int quantity, User user) {
        if (user.getRole() != Role.MANAGER) {
            throw new ForbiddenException("Only managers can order products");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (!supplierRepository.existsById(supplierId)) {
            throw new NotFoundException("Supplier not found");
        }

        if (!supplierRepository.suppliesProduct(supplierId, productId)) {
            throw new IllegalArgumentException("Supplier does not provide this product");
        }

        int currentQuantity = inventoryRepository.getQuantity(productId);
        inventoryRepository.setQuantity(productId, currentQuantity + quantity);

        Order order = new Order(
                UUID.randomUUID(),
                productId,
                supplierId,
                quantity,
                Instant.now(clock));

        orderRepository.save(order);

        return new OrderReceipt(productId, supplierId, quantity);
    }
}