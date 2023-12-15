import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InventoryManagementSystem
{
    private static final String DB_URL = "jdbc:sqlite:/Users/ericsoto/Documents/School Documents/Databases/Database Project/DatabaseFiles/inventory.db";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "pass";

    private JFrame loginFrame;
    private JFrame mainFrame;
    private JButton checkInventoryButton;
    private JButton updateStockButton;
    private JButton addProductButton;
    private JButton logoutButton;
    private JTextArea productTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryManagementSystem().createLoginScreen());
    }

    private void createLoginScreen() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(1920, 1080);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.getContentPane().setBackground(new Color(173, 216, 230)); // Subtle blue background


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel titleLabel = new JLabel("Store Inventory");
        titleLabel.setFont(new Font("Helvetica", Font.PLAIN, 40));
        loginFrame.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        setComponentStyles(usernameLabel, 25);
        setComponentStyles(usernameField, 20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        setComponentStyles(passwordLabel, 25);
        setComponentStyles(passwordField, 20);

        JButton loginButton = new JButton("Login");
        setComponentStyles(loginButton, 30);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                loginFrame.setVisible(false);
                createMainScreen();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.add(usernameLabel, gbc);
        gbc.gridy++;
        loginFrame.add(usernameField, gbc);
        gbc.gridy++;
        loginFrame.add(passwordLabel, gbc);
        gbc.gridy++;
        loginFrame.add(passwordField, gbc);
        gbc.gridy++;
        loginFrame.add(loginButton, gbc);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        label.setFont(new Font("Helvetica", Font.PLAIN, 14));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setOpaque(true);
        textField.setBackground(new Color(240, 240, 240));
        textField.setFont(new Font("Helvetica", Font.PLAIN, 14));
        return textField;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);

        return button;
    }
    private void setComponentStyles(JComponent component, int fontSize) {
        component.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
        component.setPreferredSize(new Dimension(300, 50)); // Set component size
    }
    private void setLargerComponentStyles(JComponent component, int fontSize, int preferredWidth)
    {
        component.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
        component.setPreferredSize(new Dimension(preferredWidth, 50));
    }
    private boolean authenticate(String username, String password) {
        return "user".equals(username) && "pass".equals(password);
    }

    private void createMainScreen() {
        mainFrame = new JFrame("Inventory Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1920, 1080);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.getContentPane().setBackground(new Color(173, 216, 230)); // Subtle blue background


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel title = new JLabel("Inventory Management");
        title.setFont(new Font("Helvetica", Font.PLAIN, 65));


        mainFrame.add(title, gbc);

        gbc.gridy++;
        checkInventoryButton = createButton("Check Inventory", new CheckInventoryListener());
        mainFrame.add(checkInventoryButton, gbc);

        gbc.gridy++;
        updateStockButton = createButton("Update Stock", new UpdateStockListener());
        mainFrame.add(updateStockButton, gbc);

        gbc.gridy++;
        addProductButton = createButton("Add Product", new AddProductListener());
        mainFrame.add(addProductButton, gbc);

        gbc.gridy++;
        logoutButton = createButton("Logout", e -> System.exit(0));
        mainFrame.add(logoutButton, gbc);

        gbc.gridy++;
        productTextArea = new JTextArea();
        productTextArea.setEditable(false);
        productTextArea.setFont(new Font("Helvetica", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(productTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        mainFrame.add(scrollPane, gbc);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private class CheckInventoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try (Connection connection = createConnection())
            {
                String query = "SELECT * FROM Product";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    StringBuilder productsInfo = new StringBuilder(); // Initialize productsInfo here

                    while (resultSet.next()) {
                        String productName = resultSet.getString("p_name");
                        double price = resultSet.getDouble("p_price");
                        int quantity = resultSet.getInt("p_code");

                        productsInfo.append("Product: ").append(productName)
                                .append("     |    Price: $").append(price)
                                .append("     |    Quantity: ").append(quantity).append("\n");
                    }
                    productTextArea.setText(productsInfo.toString());

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "Error fetching inventory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class UpdateStockListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Method for wanting to edit the quantity for a product
        }
    }

    private class AddProductListener implements ActionListener
    {
        //Allows the user to add a product into the database
        @Override
        public void actionPerformed(ActionEvent e)
        {
            createAddProductInterface();
        }
    }
    private void createAddProductInterface() {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(1000, 700);
        addProductFrame.setLayout(new GridBagLayout());
        addProductFrame.getContentPane().setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        setComponentStyles(nameLabel, 25);
        setLargerComponentStyles(nameField, 20,400);

        gbc.gridy++;
        JLabel priceLabel = new JLabel("Product Price:");
        JTextField priceField = new JTextField();
        setComponentStyles(priceLabel, 25);
        setLargerComponentStyles(priceField, 20,400);

        gbc.gridy++;
        JLabel quantityLabel = new JLabel("Product Quantity:");
        JTextField quantityField = new JTextField();
        setComponentStyles(quantityLabel, 25);
        setLargerComponentStyles(quantityField, 20,400);

        gbc.gridy++;
        JButton addButton = new JButton("Add");
        setComponentStyles(addButton, 30);

        addButton.addActionListener(e -> {
            String productName = nameField.getText();
            String priceStr = priceField.getText();
            String quantityStr = quantityField.getText();

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                addProductToDatabase(productName, price, quantity);
                addProductFrame.setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addProductFrame, "Invalid price or quantity", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addProductFrame.add(nameLabel, gbc);
        gbc.gridy++;
        addProductFrame.add(nameField, gbc);
        gbc.gridy++;
        addProductFrame.add(priceLabel, gbc);
        gbc.gridy++;
        addProductFrame.add(priceField, gbc);
        gbc.gridy++;
        addProductFrame.add(quantityLabel, gbc);
        gbc.gridy++;
        addProductFrame.add(quantityField, gbc);
        gbc.gridy++;
        addProductFrame.add(addButton, gbc);

        addProductFrame.setLocationRelativeTo(mainFrame);
        addProductFrame.setVisible(true);
    }

    private void addProductToDatabase(String productName, double price, int quantity) {
        try (Connection connection = createConnection()) {
            String query = "INSERT INTO product (p_name, p_price) VALUES (?, ?)";
            String query2 = "INSERT INTO stock (quantity) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productName);
                preparedStatement.setDouble(2, price);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}