import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ViewReport {

    // Method to display both High Stock and Low Stock tables in one pop-up window
    public static void showStockTables() {
        // Read inventory data from the database file
        ArrayList<List<String>> inventoryList = Database.readAllEntries();

        // Create lists to classify items into high stock and low stock categories
        List<Inventory> highStock = new ArrayList<>();
        List<Inventory> lowStock = new ArrayList<>();

        // Iterate through each inventory entry from the database
        for (List<String> item : inventoryList) {
            String id = item.get(0); // Get item ID
            String name = item.get(1); // Get item name
            int quantity = Integer.parseInt(item.get(2)); // Convert quantity to integer

            // Remove currency symbols (e.g., "K" and "each") and convert price to double
            double price = Double.parseDouble(item.get(3).replace("K", "").replace(" each", "").trim());

            // Create an Inventory object for the item
            Inventory inventoryItem = new Inventory(id, name, quantity, price);

            // Categorize item based on quantity:
            // - High Stock: Items with quantity greater than 800
            // - Low Stock: Items with quantity of 800 or below
            if (quantity > 800) {
                highStock.add(inventoryItem);
            } else {
                lowStock.add(inventoryItem);
            }
        }

        // Create a JFrame (window) to display the stock report
        JFrame frame = new JFrame("Stock Report");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window when clicking "X"
        frame.setSize(600, 400); // Set window size

        // Create a tabbed pane to separate High Stock and Low Stock sections
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create JTable components for both stock categories
        JTable highStockTable = createTable(highStock);
        JTable lowStockTable = createTable(lowStock);

        // Wrap tables inside scroll panes for better navigation
        JScrollPane highStockScroll = new JScrollPane(highStockTable);
        JScrollPane lowStockScroll = new JScrollPane(lowStockTable);

        // Add tabs to the tabbed pane with titles
        tabbedPane.addTab("High Stock Items", highStockScroll);
        tabbedPane.addTab("Low Stock Items", lowStockScroll);

        // Add tabbed pane to the frame
        frame.add(tabbedPane);
        frame.setVisible(true); // Display the window
    }

    // Helper method to create a JTable from a list of inventory items
    private static JTable createTable(List<Inventory> stockList) {
        // Define column headers for the table
        String[] columnNames = {"ID", "Name", "Quantity", "Price"};

        // Create a 2D array to store table data
        Object[][] data = new Object[stockList.size()][4];

        // Populate the data array with inventory item details
        for (int i = 0; i < stockList.size(); i++) {
            Inventory item = stockList.get(i);
            data[i][0] = item.getId(); // Item ID
            data[i][1] = item.getName(); // Item Name
            data[i][2] = item.getQuantity(); // Item Quantity
            data[i][3] = "K" + String.format("%.2f", item.getPrice()); // Item Price formatted with currency
        }

        // Create and return a JTable with the data and column headers
        return new JTable(new DefaultTableModel(data, columnNames));
    }
}
