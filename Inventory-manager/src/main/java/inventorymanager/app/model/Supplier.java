package inventorymanager.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Supplier{
    private String id;
    private String name;
    //list of product IDs this supplier provides
    private List<UUID> suppliedProducts = new ArrayList<>();

    public Supplier (String id, String name) {
        this.id = id;
        this.name = name;
    }
}
