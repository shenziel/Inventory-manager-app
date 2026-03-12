package inventorymanager.app.service;

import inventorymanager.app.exception.ForbiddenException;
import inventorymanager.app.exception.NotFoundException;
import inventorymanager.app.model.*;
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


    public boolean suppliesProduct(UUID supplierId, UUID productId){
        Supplier supplier = supplierRepository.findById(supplierId.toString());

        if (supplier == null) {
            throw new NotFoundException("Supplier not found");
        }
        // Check if the supplier's suppliedProducts list contains the productId
        return supplier.getSuppliedProducts().contains(productId);
    }

    public void addProductToSupplier(UUID supplierId, UUID productId) {
        Supplier supplier = supplierRepository.findById(supplierId.toString());

        if (supplier == null) {
            throw new NotFoundException("Supplier not found");
        }

        supplier.getSuppliedProducts().add(productId);
        supplierRepository.save(supplier);
    }

    public OrderReceipt orderProduct(UUID productId, UUID supplierId, int quantity, User user) {
        if (user.getRole() != UserRoles.MANAGER) {
            throw new ForbiddenException("Only managers can order products");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (!supplierRepository.existsById(supplierId.toString())) {
            throw new NotFoundException("Supplier not found");
        }

        if (!suppliesProduct(supplierId, productId)) {
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