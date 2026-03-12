package inventorymanager.app.controller;

import inventorymanager.app.model.Product;
import inventorymanager.app.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final InventoryService inventoryService;

    public ProductController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam(required = false) LocalDate expiry) {
        try {
            inventoryService.addProduct(name, price, quantity, expiry);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Integer> getInventorySize() {
        try {
            int size = inventoryService.getInventorySize();
            return ResponseEntity.ok(size);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        try {
            Product product = inventoryService.getProduct(id);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeProduct(@PathVariable String id) {
        try {
            boolean removed = inventoryService.removeProduct(id);
            if (removed) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id,
            @RequestBody Product updated) {
        try {
            Product product = inventoryService.updateProduct(id, updated);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stock/low")
    public ResponseEntity<Boolean> isLowStock(@RequestParam String name) {
        try {
            boolean isLow = inventoryService.isLowStock(name);
            return ResponseEntity.ok(isLow);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/expiry")
    public ResponseEntity<Boolean> isAboutToExpire(@PathVariable String id) {
        try {
            boolean isExpiring = inventoryService.isAboutToExpire(id);
            return ResponseEntity.ok(isExpiring);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

