package inventorymanager.app;

import java.util.UUID;

public record OrderReceipt(
        UUID productId,
        UUID supplierId,
        int quantity) {
}