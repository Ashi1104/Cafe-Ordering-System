package Cafe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

    public class CafeGUI extends JFrame {

        private MenuItem[] menuItems = {
                new MenuItem("Espresso", 400),
                new MenuItem("Americano", 300),
                new MenuItem("Cappuccino", 400),
                new MenuItem("Latte", 400),
                new MenuItem("Chocolate Chip Muffin", 300),
                new MenuItem("Iced Coffee", 330),
                new MenuItem("Chocolate Croissant", 220)
        };

        private Order order = new Order();

        private JComboBox<String> menuComboBox;
        private JSpinner quantitySpinner;
        private JTable orderTable;
        private DefaultTableModel tableModel;
        private JLabel totalLabel;

        public CafeGUI() {

            setTitle("Cafe Ordering & Billing System");
            setSize(850, 550);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel titleLabel = new JLabel(
                    "☕ CAFE ORDERING & BILLING SYSTEM",
                    SwingConstants.CENTER
            );

            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

            mainPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

            JLabel menuTitle = new JLabel("MENU");
            menuTitle.setFont(new Font("Arial", Font.BOLD, 20));
            menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            menuPanel.add(menuTitle);
            menuPanel.add(Box.createVerticalStrut(15));

            menuComboBox = new JComboBox<>();
            for (MenuItem item : menuItems) {
                menuComboBox.addItem(item.toString());
            }

            menuPanel.add(new JLabel("Select Item"));

            menuComboBox.setMaximumSize(new Dimension(260, 35));
            menuComboBox.setPreferredSize(new Dimension(260, 35));

            menuPanel.add(menuComboBox);

            menuPanel.add(Box.createVerticalStrut(15));

            menuPanel.add(new JLabel("Quantity: "));

            quantitySpinner = new JSpinner(
                    new SpinnerNumberModel(1, 1, 20, 1)
            );

            quantitySpinner.setMaximumSize(new Dimension(100, 35));
            quantitySpinner.setPreferredSize(new Dimension(100, 35));

            menuPanel.add(quantitySpinner);

            menuPanel.add(Box.createVerticalStrut(15));

            JButton addButton = new JButton("Add Order");

            menuPanel.add(addButton);
            menuPanel.setPreferredSize(new Dimension(280, 0));

            mainPanel.add(menuPanel, BorderLayout.WEST);

            JPanel orderPanel = new JPanel(new BorderLayout(10, 10));

            JLabel orderTitle = new JLabel("CURRENT ORDER");
            orderTitle.setFont(new Font("Arial", Font.BOLD, 20));

            orderPanel.add(orderTitle, BorderLayout.NORTH);

            String[] columns = {"Item", "Quantity", "Price"};

            tableModel = new DefaultTableModel(columns, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            orderTable = new JTable(tableModel);

            JScrollPane scrollPane = new JScrollPane(orderTable);

            orderPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
            bottomPanel.setBorder(
                    BorderFactory.createEmptyBorder(5, 0, 0, 0)
            );

            totalLabel = new JLabel("Total: Rs. 0.00");
            totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
            totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            bottomPanel.add(totalLabel);
            bottomPanel.add(Box.createVerticalStrut(8));

            JPanel buttonPanel = new JPanel(
                    new FlowLayout(FlowLayout.LEFT, 5, 2)
            );

            JButton receiptButton = new JButton("Generate Receipt");
            JButton deleteButton = new JButton("Delete Selected Item");
            JButton clearButton = new JButton("Clear Order");
            JButton exitButton = new JButton("Exit");

            receiptButton.setMargin(new Insets(5, 8, 5, 8));
            deleteButton.setMargin(new Insets(5, 8, 5, 8));
            clearButton.setMargin(new Insets(5, 8, 5, 8));
            exitButton.setMargin(new Insets(5, 8, 5, 8));

            buttonPanel.add(receiptButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(clearButton);
            buttonPanel.add(exitButton);

            buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            bottomPanel.add(buttonPanel);

            bottomPanel.add(buttonPanel, BorderLayout.CENTER);

            orderPanel.add(bottomPanel, BorderLayout.SOUTH);

            mainPanel.add(orderPanel, BorderLayout.CENTER);

            addButton.addActionListener(e -> addItemToOrder());

            deleteButton.addActionListener(e -> deleteSelectedItem());

            clearButton.addActionListener(e -> clearOrder());

            receiptButton.addActionListener(e -> generateReceipt());

            exitButton.addActionListener(e -> System.exit(0));

            add(mainPanel);
        }

        private void addItemToOrder() {

            int selectedIndex = menuComboBox.getSelectedIndex();
            int quantity = (Integer) quantitySpinner.getValue();
            MenuItem selectedItem = menuItems[selectedIndex];
            order.addItem(selectedItem, quantity);
            updateOrderTable();
        }

        private void updateOrderTable() {

            tableModel.setRowCount(0);

            for (int i = 0; i < order.getItems().size(); i++) {
                MenuItem item = order.getItems().get(i);
                int quantity = order.getQuantities().get(i);
                double price = item.getPrice() * quantity;

                tableModel.addRow(new Object[]{
                        item.getName(),
                        quantity,
                        String.format("Rs.%.2f", price)
                });
            }

            totalLabel.setText(
                    String.format(
                            "Total: Rs. %.2f",
                            order.calculateTotal()
                    )
            );
        }

        private void clearOrder() {
            order.clearOrder();
            updateOrderTable();
        }

        private void deleteSelectedItem() {

            int selectedRow = orderTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select an item to delete.",
                        "No Item Selected",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            order.removeItem(selectedRow);

            updateOrderTable();
        }

        private void generateReceipt() {

            if (order.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "The order is empty!",
                        "No Order",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            StringBuilder receipt = new StringBuilder();

            receipt.append("       CAFE RECEIPT\n");
            receipt.append("=============================\n");

            for (int i = 0; i < order.getItems().size(); i++) {

                MenuItem item = order.getItems().get(i);

                int quantity = order.getQuantities().get(i);

                double price = item.getPrice() * quantity;

                receipt.append(
                        String.format(
                                "%-22s x%d Rs. %.2f%n",
                                item.getName(),
                                quantity,
                                price
                        )
                );
            }

            receipt.append("=============================\n");

            receipt.append(
                    String.format(
                            "TOTAL: Rs. %.2f",
                            order.calculateTotal()
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    receipt.toString(),
                    "Receipt",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }