package inventorymanager.app.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter

public class Product {

    private String id;
    private String name;
    private double price;
    private int quantity;
    private LocalDate expiry;

   public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}