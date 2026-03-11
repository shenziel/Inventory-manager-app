package inventorymanager.app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Product {
    //later when tests are done

    private String id;
    private String name;
    private int price;

   public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}