import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Database {
    private static final String FILE_NAME = "inventory.txt";

    // Method to write a new item to the inventory file
    public static void writeToFile(String name, Integer quantity, Number price) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "❌ Error: Name must be a valid non-empty string.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        name = name.trim(); // Remove extra spaces
        if (quantity == null || quantity < 0) {
            JOptionPane.showMessageDialog(null, "❌ Error: Quantity must be a positive integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (price == null || price.doubleValue() < 0) {
            JOptionPane.showMessageDialog(null, "❌ Error: Price must be a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the product already exists
        if (productExists(name)) {
            JOptionPane.showMessageDialog(null, "⚠️ Product already exists! We failed to add.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generate a unique ID for the new item
        int id = generateUniqueID();

        // Format the price properly
        String formattedPrice = (price instanceof Integer) ?
                "K" + price.intValue() :
                "K" + String.format("%.2f", price.doubleValue());

        // Prepare the data format for the new item
        String data = "ID: " + id + "\n" +
                "PRODUCT NAME: " + name + "\n" +
                "QUANTITY: " + quantity + "\n" +
                "PRICE: " + formattedPrice + " each";

        // Write the new item to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(data);
            writer.newLine();
            writer.newLine(); // Separate entries
            JOptionPane.showMessageDialog(null, "✅ Item added successfully:\n" + name, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "❌ Error writing to file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to find an item by ID or name
    public static String findItem(String searchTerm) {
        StringBuilder itemDetails = new StringBuilder();
        boolean found = false; // To check if the item exists
        String lastID = null; // Store last encountered ID in case we search by product name

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for ID
                if (line.startsWith("ID:")) {
                    lastID = line; // Store the ID in case the next product name matches the search
                }

                // Check for Product Name
                if (line.startsWith("PRODUCT NAME:")) {
                    String productName = line.substring(13).trim(); // Extract product name

                    if (productName.equalsIgnoreCase(searchTerm)) {
                        found = true;

                        // Ensure the ID is included in the details
                        if (lastID != null) {
                            itemDetails.append(lastID).append("\n");
                        }

                        itemDetails.append(line).append("\n"); // Add Product Name
                        itemDetails.append(reader.readLine()).append("\n"); // Add Quantity
                        itemDetails.append(reader.readLine()).append("\n"); // Add Price
                        break; // Stop after finding the item
                    }
                }

                // Check for Product ID
                else if (line.startsWith("ID:")) {
                    String productID = line.substring(3).trim(); // Extract product ID

                    if (productID.equals(searchTerm)) {
                        found = true;
                        itemDetails.append(line).append("\n"); // Add ID
                        itemDetails.append(reader.readLine()).append("\n"); // Add Product Name
                        itemDetails.append(reader.readLine()).append("\n"); // Add Quantity
                        itemDetails.append(reader.readLine()).append("\n"); // Add Price
                        break; // Stop after finding the item
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }

        return found ? itemDetails.toString() : "❌ Item '" + searchTerm + "' not found.";
    }

    // Method to delete an item by ID or name
    public static boolean deleteItem(String searchTerm) {
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for ID first
                if (line.startsWith("ID:")) {
                    String productID = line.substring(3).trim(); // Extract ID

                    if (productID.equals(searchTerm)) {
                        // Found item by ID → Skip the next 3 lines (Product Name, Quantity, Price)
                        found = true;
                        reader.readLine(); // Skip Product Name line
                        reader.readLine(); // Skip Quantity line
                        reader.readLine(); // Skip Price line
                        continue; // Move to next loop iteration (skip writing to file)
                    }
                }
                // Check for Product Name
                else if (line.startsWith("PRODUCT NAME:")) {
                    String productName = line.substring(13).trim(); // Extract product name

                    if (productName.equalsIgnoreCase(searchTerm)) {
                        // Found item by Name → We must also skip the ID line before it
                        found = true;
                        // Remove the last added ID line (so it is not written to the file)
                        if (!updatedLines.isEmpty() && updatedLines.get(updatedLines.size() - 1).startsWith("ID:")) {
                            updatedLines.remove(updatedLines.size() - 1);
                        }
                        // Skip Quantity & Price lines
                        reader.readLine(); // Skip Quantity line
                        reader.readLine(); // Skip Price line
                        continue; // Move to next loop iteration
                    }
                }

                // If it's not the product we want to delete, keep it in the list
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
            return false;
        }

        if (found) {
            // Overwrite the file with updated content
            boolean action = overwrite(updatedLines);
            if (action) {
                System.out.println("✅ Item deleted successfully.");
            } else {
                System.out.println("❌ There was a problem deleting the item.");
                return false;
            }
        } else {
            System.out.println("❌ Item not found. Please enter the correct name or ID.");
        }

        return found;
    }

    // Method to update an item's details
    public static boolean updateFile(String searchTerm, String newName, int newQuantity, double newPrice) {
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        boolean isProductNameLine = false;
        String currentID = "";
        String currentName = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID:")) {
                    currentID = line.substring(line.indexOf(":") + 1).trim();
                    updatedLines.add(line); // Keep ID unchanged
                    continue;
                } else if (line.startsWith("PRODUCT NAME:")) {
                    currentName = line.substring(line.indexOf(":") + 1).trim();
                    isProductNameLine = true;
                }

                // Update only the first matching entry and ignore subsequent matches
                if (!found && (currentID.equalsIgnoreCase(searchTerm) || currentName.equalsIgnoreCase(searchTerm))) {
                    found = true; // Mark as found, preventing duplicate updates

                    if (isProductNameLine) {
                        updatedLines.add("PRODUCT NAME: " + newName); // Update name
                    } else {
                        updatedLines.add(line); // Keep unchanged product name
                    }
                    updatedLines.add("QUANTITY: " + newQuantity); // Update quantity
                    updatedLines.add("PRICE: K" + newPrice); // Update price

                    // Skip the next two lines (old quantity & price)
                    reader.readLine();
                    reader.readLine();
                    continue;
                }

                // If it's not the product we want to update, keep it in the list
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
            return false;
        }

        if (found) {
            // Overwrite the file with updated content
            boolean action = overwrite(updatedLines);
            if (action) {
                System.out.println("✅ File updated successfully");
            } else {
                System.out.println("❌ There was a problem updating the file");
                return false;
            }
        }
        return found;
    }

    // Method to overwrite the file with updated content
    private static boolean overwrite(List<String> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String updatedLines : items) {
                writer.write(updatedLines);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error writing file: " + e.getMessage());
            return false;
        }

        return true;
    }

    // Method to generate a unique ID for new items
    private static int generateUniqueID() {
        Set<Integer> existingIDs = new HashSet<>();
        int maxID = 0; // Track the highest assigned ID
        Set<Integer> allPossibleIDs = new TreeSet<>(); // Sorted set to store missing IDs

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            Pattern pattern = Pattern.compile("ID:\\s*(\\d+)"); // Regex to find "ID: X"

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int id = Integer.parseInt(matcher.group(1));
                    existingIDs.add(id);
                    maxID = Math.max(maxID, id); // Keep track of highest ID
                }
            }

            // Check for missing IDs (deleted ones)
            for (int i = 1; i <= maxID; i++) {
                if (!existingIDs.contains(i)) {
                    allPossibleIDs.add(i); // Store missing IDs
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ File not found or empty. Starting with ID 1.");
        }

        // Reuse the smallest available ID if any exist, else use maxID + 1
        return allPossibleIDs.isEmpty() ? maxID + 1 : allPossibleIDs.iterator().next();
    }

    // Method to check if a product already exists
    public static boolean productExists(String productName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PRODUCT NAME:")) {
                    String existingProduct = line.substring(14).trim(); // Extract product name
                    if (existingProduct.equalsIgnoreCase(productName)) {
                        return true; // Product already exists
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ File not found. Creating a new one...");
        }
        return false; // Product does not exist
    }

    // Method to read all entries from the inventory file
    public static ArrayList<List<String>> readAllEntries() {
        ArrayList<List<String>> inventoryList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            List<String> product = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID:")) {
                    // Start a new product entry
                    product = new ArrayList<>();
                    product.add(line.substring(3).trim()); // Add ID
                } else if (line.startsWith("PRODUCT NAME:")) {
                    product.add(line.substring(13).trim()); // Add Product Name
                } else if (line.startsWith("QUANTITY:")) {
                    product.add(line.substring(9).trim()); // Add Quantity
                } else if (line.startsWith("PRICE:")) {
                    product.add(line.substring(7).trim()); // Add Price
                    inventoryList.add(product); // Add completed product to list
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }

        return inventoryList;
    }
}