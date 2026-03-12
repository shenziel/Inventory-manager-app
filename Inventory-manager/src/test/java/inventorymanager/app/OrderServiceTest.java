package inventorymanager.app;

import inventorymanager.app.repository.*;
import inventorymanager.app.services.OrderService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Test
    void manager_can_order_product_from_supplier_and_stock_increases() {
        // Arrange
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        SupplierRepository supplierRepo = mock(SupplierRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-01T10:00:00Z"), ZoneOffset.UTC);

        OrderService service = new OrderService(inventoryRepo, supplierRepo, orderRepo, fixedClock);

        User manager = new User("John", Role.MANAGER);
        UUID productId = UUID.randomUUID();
        UUID supplierId = UUID.randomUUID();

        when(supplierRepo.existsById(supplierId)).thenReturn(true);
        when(supplierRepo.suppliesProduct(supplierId, productId)).thenReturn(true);
        when(inventoryRepo.getQuantity(productId)).thenReturn(10);

        // Act
        OrderReceipt receipt = service.orderProduct(productId, supplierId, 5, manager);

        // Assert
        assertNotNull(receipt);
        assertEquals(productId, receipt.productId());
        assertEquals(supplierId, receipt.supplierId());
        assertEquals(5, receipt.quantity());

        verify(inventoryRepo).setQuantity(productId, 15);
        verify(orderRepo).save(any(Order.class));
        verifyNoMoreInteractions(orderRepo);
    }

    @Test
    void cannot_order_with_non_positive_quantity() {
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        SupplierRepository supplierRepo = mock(SupplierRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        OrderService service = new OrderService(inventoryRepo, supplierRepo, orderRepo, Clock.systemUTC());

        User manager = new User("John", Role.MANAGER);

        assertThrows(IllegalArgumentException.class,
                () -> service.orderProduct(UUID.randomUUID(), UUID.randomUUID(), 0, manager));
    }

    @Test
    void admin_cannot_order_product() {
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        SupplierRepository supplierRepo = mock(SupplierRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        OrderService service = new OrderService(inventoryRepo, supplierRepo, orderRepo, Clock.systemUTC());

        User admin = new User("Anna", Role.ADMIN);

        assertThrows(ForbiddenException.class,
                () -> service.orderProduct(UUID.randomUUID(), UUID.randomUUID(), 1, admin));
    }
}