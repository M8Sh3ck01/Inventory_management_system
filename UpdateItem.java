import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UpdateItem extends JFrame {
    private JTextField searchField, nameField, quantityField, priceField;
    private JButton updateButton, cancelButton;

    public UpdateItem() {
        setTitle("Update Inventory Item");
        setSize(400, 250); // Adjusted height to make the window smaller
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ✅ Title Label
        JLabel titleLabel = new JLabel("Update Inventory Item", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // ✅ Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5)); // Use GridLayout for simplicity
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Label and Field
        JLabel searchLabel = new JLabel("Enter Product Name/ID:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        formPanel.add(searchLabel);

        searchField = new JTextField(20);
        formPanel.add(searchField);

        // New Name Label and Field
        JLabel nameLabel = new JLabel("New Product Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        formPanel.add(nameLabel);

        nameField = new JTextField(20);
        formPanel.add(nameField);

        // New Quantity Label and Field
        JLabel quantityLabel = new JLabel("New Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        formPanel.add(quantityLabel);

        quantityField = new JTextField(20);
        formPanel.add(quantityField);

        // New Price Label and Field
        JLabel priceLabel = new JLabel("New Price:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        formPanel.add(priceLabel);

        priceField = new JTextField(20);
        formPanel.add(priceField);

        add(formPanel, BorderLayout.CENTER);

        // ✅ Button Panel
        JPanel buttonPanel = new JPanel();
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ✅ Button Actions
        updateButton.addActionListener(e -> updateItem());
        cancelButton.addActionListener(e -> {
            dispose(); // Close the window
            new HomePage().setVisible(true); // Go back to HomePage
        });

        // ✅ Add Enter Key Navigation
        addEnterKeyNavigation();

        setVisible(true);
    }

    // Method to handle Enter key navigation
    private void addEnterKeyNavigation() {
        // Search Field → Name Field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    nameField.requestFocus(); // Move to Name Field
                }
            }
        });

        // Name Field → Quantity Field
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    quantityField.requestFocus(); // Move to Quantity Field
                }
            }
        });

        // Quantity Field → Price Field
        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    priceField.requestFocus(); // Move to Price Field
                }
            }
        });

        // Price Field → Submit Form
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateItem(); // Submit the form
                }
            }
        });
    }

    private void updateItem() {
        String searchTerm = searchField.getText().trim();
        String newName = nameField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        // ✅ Validate Search Term
        if (searchTerm.isEmpty()) {
            showError("❌ Product name or ID cannot be empty!", "Invalid Input");
            return;
        }

        // ✅ Validate Product Name
        if (newName.isEmpty()) {
            showError("❌ New product name cannot be empty!", "Invalid Input");
            return;
        }

        // ✅ Validate Quantity
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(quantityText);
            if (newQuantity < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError("❌ Invalid quantity! Enter a valid positive number.", "Error");
            return;
        }

        // ✅ Validate Price
        double newPrice;
        try {
            newPrice = Double.parseDouble(priceText);
            if (newPrice < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError("❌ Invalid price! Enter a valid positive number.", "Error");
            return;
        }

        // ✅ Call `updateFile` to modify the file
        boolean success = Database.updateFile(searchTerm, newName, newQuantity, newPrice);

        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the update window
            new HomePage().setVisible(true); // ✅ Redirect to HomePage
        } else {
            showError("❌ Product not found. Please check the name or ID.", "Update Failed");
        }
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateItem());
    }
}