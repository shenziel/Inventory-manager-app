package inventorymanager.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import scala.Array;
import scala.collection.immutable.ArraySeq;

@SpringBootApplication
public class InventoryManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagerApplication.class, args);
    }
}
