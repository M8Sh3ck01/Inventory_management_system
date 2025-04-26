import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class AddItem {
    private JFrame frame;
    private JTextField itemField, quantityField, priceField;
    private JButton addButton;
    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<Integer> itemQuantities = new ArrayList<>();
    private ArrayList<Double> itemPrices = new ArrayList<>();

    public AddItem() {
        // ✅ Frame setup
        frame = new JFrame("Add New Item");
        frame.setSize(400, 250); // Adjusted height to make the window smaller
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        // ✅ Title label
        JLabel titleLabel = new JLabel("Add New Inventory Item", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        // ✅ Panel for input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Reduce spacing between components
        gbc.anchor = GridBagConstraints.WEST;

        // Item Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Item Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        inputPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        itemField = new RoundedTextField(20);
        itemField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(itemField, gbc);

        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        inputPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        quantityField = new RoundedTextField(20);
        quantityField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(quantityField, gbc);

        // Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger font size
        inputPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        priceField = new RoundedTextField(20);
        priceField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(priceField, gbc);

        // Add Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add Item");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(34, 177, 76));
        addButton.setForeground(Color.WHITE);
        inputPanel.add(addButton, gbc);

        frame.add(inputPanel, BorderLayout.CENTER);

        // ✅ Input validation
        addValidationToItemField();
        addEnterKeyNavigation(); // ⬅️ Added Enter Key Navigation

        // ✅ Button action
        addButton.addActionListener(e -> addItem());

        frame.setVisible(true);
    }

    // Custom JTextField with rounded borders
    private static class RoundedTextField extends JTextField {
        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false); // Make the text field transparent
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded border
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Rounded corners

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw border
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); // Rounded corners

            g2.dispose();
        }
    }

    private void addValidationToItemField() {
        // Prevent numbers but allow letters, spaces, backspace, and delete
        itemField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                // Allow Backspace, Delete, Space, and Enter
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == ' ' || c == KeyEvent.VK_ENTER) {
                    return;
                }

                // Block everything that is not a letter
                if (!Character.isLetter(c)) {
                    e.consume(); // Prevent input
                    Toolkit.getDefaultToolkit().beep(); // Provide feedback
                }

                // Limit to 50 characters
                if (itemField.getText().length() >= 50) {
                    e.consume();
                }
            }
        });

        // Ensure the text does not exceed 50 characters
        itemField.getDocument().addDocumentListener(new DocumentListener() {
            private void limitText() {
                if (itemField.getText().length() > 50) {
                    itemField.setText(itemField.getText().substring(0, 50));
                }
            }

            public void insertUpdate(DocumentEvent e) { limitText(); }
            public void removeUpdate(DocumentEvent e) { limitText(); }
            public void changedUpdate(DocumentEvent e) { limitText(); }
        });
    }

    private void addEnterKeyNavigation() {
        itemField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    quantityField.requestFocus(); // Move to Quantity field
                }
            }
        });

        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    priceField.requestFocus(); // Move to Price field
                }
            }
        });

        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addItem(); // Submit the form when Enter is pressed in the Price field
                }
            }
        });
    }

    private void addItem() {
        String name = itemField.getText().trim();
        int quantity;
        double price;

        if (!validateInputs(name)) return;

        // ✅ Check if the product already exists
        if (Database.productExists(name)) {
            JOptionPane.showMessageDialog(frame, "⚠️ Product already exists! Cannot add duplicate.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity < 0) {
                JOptionPane.showMessageDialog(frame, "Quantity cannot be negative.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid quantity. Enter a valid number.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(frame, "Price cannot be negative.", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid price. Enter a valid decimal number.", "Invalid Price", JOptionPane.ERROR_MESSAGE);
            return;
        }

        addToInventory(name, quantity, price);

        // ✅ Save to file
        Database.writeToFile(name, quantity, price);

        clearFields();

        // ✅ Show confirmation dialog and return to HomePage
        int choice = JOptionPane.showOptionDialog(frame, "Item details\n" + name + " - Quantity: " + quantity + ", Price: MWK" + price,
                "Success", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"}, "OK");

        if (choice == 0) {
            frame.dispose(); // Close the AddItem window
            new HomePage(); // Open the HomePage window
        }
    }

    private boolean validateInputs(String name) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Item name cannot be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(frame, "Item name can only contain letters and spaces.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void addToInventory(String name, int quantity, double price) {
        itemNames.add(name);
        itemQuantities.add(quantity);
        itemPrices.add(price);
    }

    private void clearFields() {
        itemField.setText("");
        quantityField.setText("");
        priceField.setText("");
        itemField.requestFocus(); // Reset focus to item name field
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddItem::new);
    }
}