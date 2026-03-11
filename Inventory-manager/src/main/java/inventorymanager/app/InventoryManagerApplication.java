package inventorymanager.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class InventoryManagerApplication {

	public static void main(String[] args) {


		SpringApplication.run(InventoryManagerApplication.class, args);
	}

    // TODO: A trivial static method to demonstrate JUnit usage [Delete]
    public static int addition() {
        return 1 + 1;
    }

    public static boolean checkIsogram(String inputString)
    {
        // always it is good to convert the string in either
        // lower/upper case because for isogram irrespective
        // of the case we need to check that no alphabet
        // should be occurred more than once.
        inputString = inputString.toLowerCase();
        int stringLength = inputString.length();

        char characterArray[] = inputString.toCharArray();
        // This will help to sort the alphabets
        Arrays.sort(characterArray);
        for (int i = 0; i < stringLength - 1; i++) {
            if (characterArray[i] == characterArray[i + 1])
                return false;
        }
        return false;
    }

}
