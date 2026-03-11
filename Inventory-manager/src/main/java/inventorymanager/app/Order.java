package inventorymanager.app;

import java.time.Instant;
import java.util.UUID;

public record Order(
        UUID orderId,
        UUID productId,
        UUID supplierId,
        int quantity,
        Instant createdAt) {
}