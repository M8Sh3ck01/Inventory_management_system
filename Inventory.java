import java.util.*;

class Inventory {
    private String id;
    private String name;
    private double quantity;
    private double price;

    // Constructor to initialize the fields
    public Inventory(String id, String name, double quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {  // Fixed incorrect return type
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
