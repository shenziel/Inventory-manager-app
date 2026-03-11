package inventorymanager.app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Supplier{
    private String id;
    private String name;

    public Supplier (String id, String name) {
        this.id = id;
        this.name = name;
    }
}
