import java.awt.*;
import javax.swing.*;

public class HomePage extends JFrame {
    private final RemoveItem removeItem; // Store RemoveItem instance

    public HomePage() {
        ImageIcon ico = new ImageIcon("icons/invent.png"); // Frame icon
        Database inventoryManager = new Database();
        removeItem = new RemoveItem(inventoryManager);

        // Set frame properties
        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage(ico.getImage()); // Set frame icon
        getContentPane().setBackground(new Color(200, 200, 200)); // Grey background

        // Header panel with gradient background (Blue)
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 102, 204), getWidth(), 0, new Color(0, 76, 153)); // Blue gradient
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel titleLabel = new JLabel("Inventory Management System", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE); // White text
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Left panel for buttons
        JPanel leftPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        leftPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] labels = {"Remove Item", "Add Item", "Update Item", "View Reports"};

        for (int i = 0; i < labels.length; i++) {
            JButton button = new JButton(labels[i]);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            button.setBackground(new Color(255, 255, 255)); // White background
            button.setForeground(new Color(51, 51, 51)); // Dark gray text
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Light gray border
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));

            // Add hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(230, 230, 230)); // Light gray hover
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(255, 255, 255)); // White background
                }
            });

            // Add action listeners
            if (labels[i].equals("View Reports")) {
                button.addActionListener(e -> ViewReport.showStockTables());
            } else if (labels[i].equals("Remove Item")) {
                button.addActionListener(e -> removeItem.showRemoveDialog()); // Show RemoveItem dialog
            } else if (labels[i].equals("Add Item")) {
                button.addActionListener(e -> new AddItem());
            } else if (labels[i].equals("Update Item")) {
                button.addActionListener(e -> new UpdateItem());
            }

            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            leftPanel.add(button);
        }

        // Right panel with search button
        JButton searchButton = new JButton("Search Item");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchButton.setPreferredSize(new Dimension(150, 50));
        searchButton.setBackground(new Color(0, 102, 204)); // Blue background
        searchButton.setForeground(Color.BLACK); // White text
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        searchButton.setFocusPainted(false);

        // Add hover effect
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0, 76, 153)); // Darker blue hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0, 102, 204)); // Blue background
            }
        });

        // Action listener to open the ItemSearchApp
        searchButton.addActionListener(e -> openItemSearchApp());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.add(searchButton);

        // Footer panel with Exit button (Blue)
        JPanel footerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 102, 204), getWidth(), 0, new Color(0, 76, 153)); // Blue gradient
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        footerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel footerLabel = new JLabel("© 2025 Inventory App", SwingConstants.CENTER);
        footerLabel.setForeground(Color.WHITE); // White text
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        exitButton.setForeground(Color.BLACK); // White text
        exitButton.setBackground(new Color(255, 51, 51)); // Bright red background
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exitButton.setFocusPainted(false);

        // Add hover effect
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(204, 0, 0)); // Darker red hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(255, 51, 51)); // Bright red background
            }
        });

        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    HomePage.this,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setOpaque(false);
        exitPanel.add(exitButton);
        footerPanel.add(exitPanel, BorderLayout.EAST);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Method to open the ItemSearchApp
    private void openItemSearchApp() {
        SwingUtilities.invokeLater(ItemSearch::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HomePage().setVisible(true);
        });
    }
}