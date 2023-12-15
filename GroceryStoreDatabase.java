import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GroceryStoreDatabase {
    private static final String DB_URL = "jdbc:sqlite:/Users/ericsoto/Documents/School Documents/Databases/Database Project/DatabaseFiles/inventory.db";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "pass";

    private JFrame loginFrame;
    private JFrame mainFrame;
    private JButton checkInventoryButton;
    private JButton updateStockButton;
    private JButton addProductButton;
    private JButton logoutButton;
    private JButton removeProductButton;
    private JTextArea productTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GroceryStoreDatabase().createLoginScreen());
    }

    public void createLoginScreen() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(1920, 1080);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.getContentPane().setBackground(new Color(173, 216, 230));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel titleLabel = new JLabel("Grocery Store Login");
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

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);

        return button;
    }

    private void setComponentStyles(JComponent component, int fontSize) {
        component.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
        component.setPreferredSize(new Dimension(300, 50));
    }

    private void setLargerComponentStyles(JComponent component, int fontSize, int preferredWidth) {
        component.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
        component.setPreferredSize(new Dimension(preferredWidth, 50));
    }

    private boolean authenticate(String username, String password) {
        return "user".equals(username) && "pass".equals(password);
    }

    private void createMainScreen() {
        mainFrame = new JFrame("Grocery Store Inventory");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1920, 1080);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.getContentPane().setBackground(new Color(173, 216, 230));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel title = new JLabel("Grocery Store Inventory");
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
        removeProductButton = createButton("Remove Product", new removeProductListener());
        mainFrame.add(removeProductButton, gbc);

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

    private class CheckInventoryListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try (Connection connection = createConnection())
            {
                String query = "SELECT DISTINCT p_name, p_price, quantity from product join stock where stock.p_code = product.p_code order by (quantity)";
                try (Statement statement = connection.createStatement())
                {
                    System.out.println("Connection Established");
                    ResultSet resultSet = statement.executeQuery(query);
                    StringBuilder productsInfo = new StringBuilder();

                    while (resultSet.next()) {
                        String productName = resultSet.getString("p_name");
                        double price = resultSet.getDouble("p_price");
                        int quantity = resultSet.getInt("quantity");

                        productsInfo.append("Product: ").append(productName)
                                .append("     |    Price: $").append(price)
                                .append("     |    Quantity: ").append(quantity).append("\n");
                    }
                    productTextArea.setText(productsInfo.toString());

                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "Error fetching inventory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class UpdateStockListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            createUpdateProductInterface();
        }
    }

    private void createUpdateProductInterface()
    {
        JFrame updateProductFrame = new JFrame("Update Inventory");
        updateProductFrame.setSize(1920, 1080);
        updateProductFrame.setLayout(new GridBagLayout());
        updateProductFrame.getContentPane().setBackground(new Color(173, 216, 230));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        setComponentStyles(nameLabel, 25);
        setLargerComponentStyles(nameField, 20, 500);

        gbc.gridy++;
        JLabel quantityLabel = new JLabel("Product Quantity:");
        JTextField quantityField = new JTextField();
        setComponentStyles(quantityLabel, 25);
        setLargerComponentStyles(quantityField, 20, 500);

        gbc.gridy++;
        JButton updateButton = new JButton("Update");
        setComponentStyles(updateButton, 30);

        updateButton.addActionListener(e -> {
            String productName = nameField.getText();
            String quantityStr = quantityField.getText();
            try
            {
                int quantity = Integer.parseInt(quantityStr);

                updateProductInDatabase(productName, quantity);
                updateProductFrame.setVisible(false);
            } catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(updateProductFrame, "Invalid price or quantity", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy++;
        updateProductFrame.add(nameLabel, gbc);
        gbc.gridy++;
        updateProductFrame.add(nameField, gbc);
        gbc.gridy++;
        updateProductFrame.add(quantityLabel, gbc);
        gbc.gridy++;
        updateProductFrame.add(quantityField, gbc);
        gbc.gridy++;
        updateProductFrame.add(updateButton, gbc);


        updateProductFrame.setLocationRelativeTo(mainFrame);
        updateProductFrame.setVisible(true);
    }

    private void updateProductInDatabase(String p_name, int quantity)
    {
        try (Connection connection = createConnection())
        {

            String checkQuery = "SELECT p_code FROM product WHERE p_name = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery))
            {
                checkStatement.setString(1, p_name);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next())
                {

                    int productCode = resultSet.getInt("p_code");
                    String updateQuery = "UPDATE stock SET quantity = ? WHERE p_code = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery))
                    {
                        updateStatement.setInt(1, quantity);
                        updateStatement.setInt(2, productCode);
                        int rowsAffected = updateStatement.executeUpdate();

                        if (rowsAffected > 0)
                        {
                            System.out.println("Quantity updated successfully");
                        }
                        else
                        {
                            System.out.println("Product not found or quantity not updated");
                        }
                    }
                }
                else
                {
                    System.out.println("Product not found");
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class AddProductListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            createAddProductInterface();
        }
    }

    private void createAddProductInterface()
    {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(1920, 1080);
        addProductFrame.setLayout(new GridBagLayout());
        addProductFrame.getContentPane().setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        setComponentStyles(nameLabel, 25);
        setLargerComponentStyles(nameField, 20, 500);

        gbc.gridy++;
        JLabel priceLabel = new JLabel("Product Price:");
        JTextField priceField = new JTextField();
        setComponentStyles(priceLabel, 25);
        setLargerComponentStyles(priceField, 20, 500);

        gbc.gridy++;
        JLabel quantityLabel = new JLabel("Product Quantity:");
        JTextField quantityField = new JTextField();
        setComponentStyles(quantityLabel, 25);
        setLargerComponentStyles(quantityField, 20, 500);

        gbc.gridy++;
        JLabel depLabel = new JLabel("Department:");
        JTextField depField = new JTextField();
        setComponentStyles(depLabel, 25);
        setLargerComponentStyles(depField, 20, 500);

        gbc.gridy++;
        JButton addButton = new JButton("Add");
        setComponentStyles(addButton, 30);

        addButton.addActionListener(e ->
        {
            String productName = nameField.getText();
            String priceStr = priceField.getText();
            String quantityStr = quantityField.getText();
            String depCode = depField.getText();


            try
            {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                int dep_code = Integer.parseInt(depCode);
                int v_code = Integer.parseInt(depCode);

                addProductToDatabase(productName, price, quantity, dep_code, v_code);
                addProductFrame.setVisible(false);
            }
            catch (NumberFormatException ex)
            {
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
        addProductFrame.add(depLabel, gbc);
        gbc.gridy++;
        addProductFrame.add(depField, gbc);
        gbc.gridy++;
        addProductFrame.add(addButton, gbc);


        addProductFrame.setLocationRelativeTo(mainFrame);
        addProductFrame.setVisible(true);
    }

    private void addProductToDatabase(String productName, double price, int quantity, int dep_code, int v_code)
    {
        try (Connection connection = createConnection())
        {
            String query = "INSERT INTO product (p_name, p_price, d_code, v_code) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                preparedStatement.setString(1, productName);
                preparedStatement.setDouble(2, price);
                preparedStatement.setInt(3, dep_code);
                preparedStatement.setInt(4, v_code);
                preparedStatement.executeUpdate();
            }

            try (Statement statement = connection.createStatement())
            {
                ResultSet resultSet = statement.executeQuery("SELECT last_insert_rowid()");
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    String query2 = "INSERT INTO stock (p_code, quantity) VALUES (?,?)";
                    try (PreparedStatement preparedStatement2 = connection.prepareStatement(query2))
                    {
                        preparedStatement2.setInt(1, id);
                        preparedStatement2.setInt(2, quantity);
                        preparedStatement2.executeUpdate();
                    }
                }
            }

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error adding product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class removeProductListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            createRemoveProductInterface();
        }
    }

    private void createRemoveProductInterface()
    {
        JFrame removeProductFrame = new JFrame("Remove Product");
        removeProductFrame.setSize(1920, 1080);
        removeProductFrame.setLayout(new GridBagLayout());
        removeProductFrame.getContentPane().setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        setComponentStyles(nameLabel, 25);
        setLargerComponentStyles(nameField, 20, 500);

        gbc.gridy++;
        JButton removeButton = new JButton("Remove");
        setComponentStyles(removeButton, 30);

        removeButton.addActionListener(e ->
        {
            String productName = nameField.getText();

            try
            {
                removeProductFromDatabase(productName);
                removeProductFrame.setVisible(false);
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(removeProductFrame, "Invalid price or quantity", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeProductFrame.add(nameLabel, gbc);
        gbc.gridy++;
        removeProductFrame.add(nameField, gbc);
        gbc.gridy++;
        removeProductFrame.add(removeButton, gbc);


        removeProductFrame.setLocationRelativeTo(mainFrame);
        removeProductFrame.setVisible(true);
    }

    public void removeProductFromDatabase(String productName)
    {
        try (Connection connection = createConnection())
        {

            String checkQuery = "SELECT p_code FROM product WHERE p_name = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery))
            {
                checkStatement.setString(1, productName);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next())
                {

                    int productCode = resultSet.getInt("p_code");
                    String deleteProductQuery = "DELETE FROM product WHERE p_code = ?";
                    String deleteStockQuery = "DELETE FROM stock WHERE p_code = ?";

                    try (PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery);
                         PreparedStatement deleteStockStatement = connection.prepareStatement(deleteStockQuery))
                    {
                        deleteProductStatement.setInt(1, productCode);
                        int productRowsAffected = deleteProductStatement.executeUpdate();

                        deleteStockStatement.setInt(1, productCode);
                        int stockRowsAffected = deleteStockStatement.executeUpdate();

                        if (productRowsAffected > 0 && stockRowsAffected > 0)
                        {
                            System.out.println("Product deleted successfully");
                        }
                        else
                        {
                            System.out.println("Product not found or deletion failed");
                        }
                    }
                }
                else
                {
                    System.out.println("Product not found");
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error deleting product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}