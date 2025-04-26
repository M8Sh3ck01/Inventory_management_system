import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ItemSearch {
    // JFrame to hold the application window
    private JFrame frame;
    // Text field where the user will input the product ID or name
    private JTextField productIdField;
    // Label to display the search results
    private JLabel resultLabel;
    // Database object that interacts with the database to search for products
    private Database database;

    // Constructor to initialize the database and user interface
    public ItemSearch() {
        database = new Database();  // Initialize the database
        initializeUI();  // Initialize the user interface components
    }

    // Method to set up the user interface
    private void initializeUI() {
        // Create a new JFrame with the title "Item Search App"
        frame = new JFrame("Search Item");
        // Set the default close operation to dispose the frame (not closing the whole application)
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the size of the window (maximum width of 10 cm and height of 8 cm in pixels)
        frame.setSize(378, 302);
        // Set the layout manager of the window to GridLayout with 3 rows and 1 column
        frame.setLayout(new GridLayout(3, 1));

        // Create a panel to hold the label and text field
        JPanel inputPanel = new JPanel();
        // Create a label prompting the user to enter a product ID or name
        JLabel label = new JLabel("Enter Product ID or Name:");
        // Create a text field where the user can input the product ID or name
        productIdField = new JTextField(10);
        // Add the label and text field to the input panel
        inputPanel.add(label);
        inputPanel.add(productIdField);

        // Add a KeyListener to the text field to detect when the "Enter" key is pressed
        productIdField.addKeyListener(new KeyAdapter() {
            // Override the keyPressed method to check for the "Enter" key
            @Override
            public void keyPressed(KeyEvent e) {
                // If the "Enter" key is pressed, perform the search
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProduct();
                }
            }
        });

        // Create a search button with the label "Search"
        JButton searchButton = new JButton("Search");
        // Set the preferred size of the search button
        searchButton.setPreferredSize(new Dimension(100, 30));  // Adjust button size for better appearance
        // Add an ActionListener to the button so that when clicked, the searchProduct method is called
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct();  // Perform the search when the button is clicked
            }
        });

        // Create a panel to hold the search button
        JPanel buttonPanel = new JPanel();
        // Add the search button to the button panel
        buttonPanel.add(searchButton);

        // Create a label to display the result of the search
        resultLabel = new JLabel("", SwingConstants.CENTER);

        // Add the input panel, button panel, and result label to the main frame
        frame.add(inputPanel);
        frame.add(buttonPanel);  // Add the panel containing the search button
        frame.add(resultLabel);  // Add the result label to display search results

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Make the frame non-resizable
        frame.setResizable(false);

        // Make the frame visible to the user
        frame.setVisible(true);
    }

    // Method to search for a product based on the input from the user
    private void searchProduct() {
        // Get the text entered by the user in the product ID or name field
        String query = productIdField.getText().trim();

        // Use the findItem method from the Database class to search for the item
        String result = database.findItem(query);

        // Check if the result is not null (meaning a product was found)
        if (result != null) {
            // If the product is found, display its details in the result label
            resultLabel.setText("<html>" + result.replaceAll("\n", "<br>") + "</html>");
        } else {
            // If no product is found, display a "Product not found" message
            resultLabel.setText("Product not found.");
        }
    }}

