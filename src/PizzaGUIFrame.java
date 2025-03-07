import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDish;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;
    private JButton orderButton, clearButton, quitButton;

    private static final double TAX_RATE = 0.07;
    private static final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};
    private static final String[] SIZE_OPTIONS = {"Small", "Medium", "Large", "Super"};
    private static final String[] TOPPING_OPTIONS = {"Pepperoni", "Mushrooms", "Onions", "Bacon", "Pineapple", "Olives"};

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crust Panel
        JPanel crustPanel = new JPanel(new GridLayout(3, 1));
        crustPanel.setBorder(new TitledBorder("Crust Type"));
        ButtonGroup crustGroup = new ButtonGroup();
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-dish");
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDish);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDish);

        // Size Panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Size"));
        sizeBox = new JComboBox<>(SIZE_OPTIONS);
        sizePanel.add(sizeBox);

        // Toppings Panel
        JPanel toppingsPanel = new JPanel(new GridLayout(3, 2));
        toppingsPanel.setBorder(new TitledBorder("Toppings ($1 each)"));
        toppings = new JCheckBox[TOPPING_OPTIONS.length];
        for (int i = 0; i < TOPPING_OPTIONS.length; i++) {
            toppings[i] = new JCheckBox(TOPPING_OPTIONS[i]);
            toppingsPanel.add(toppings[i]);
        }

        // Receipt Panel
        JPanel receiptPanel = new JPanel();
        receiptPanel.setBorder(new TitledBorder("Order Summary"));
        receiptArea = new JTextArea(10, 30);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Adding components to the Frame
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(receiptPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> exitApplication());
    }

    private class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder receipt = new StringBuilder();
            double subtotal = 0;

            // Crust Selection
            if (thinCrust.isSelected()) receipt.append("Thin Crust\n");
            else if (regularCrust.isSelected()) receipt.append("Regular Crust\n");
            else if (deepDish.isSelected()) receipt.append("Deep-Dish Crust\n");
            else {
                JOptionPane.showMessageDialog(null, "Please select a crust type!");
                return;
            }

            // Size Selection
            int sizeIndex = sizeBox.getSelectedIndex();
            receipt.append("Size: ").append(SIZE_OPTIONS[sizeIndex]).append("\t$").append(SIZE_PRICES[sizeIndex]).append("\n");
            subtotal += SIZE_PRICES[sizeIndex];

            // Toppings Selection
            boolean hasToppings = false;
            receipt.append("Toppings:\n");
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    receipt.append("- ").append(topping.getText()).append("\t$1.00\n");
                    subtotal += 1;
                    hasToppings = true;
                }
            }
            if (!hasToppings) {
                JOptionPane.showMessageDialog(null, "Please select at least one topping!");
                return;
            }

            // Calculating totals
            double tax = subtotal * TAX_RATE;
            double total = subtotal + tax;

            // Formatting receipt
            receipt.append("=================================\n");
            receipt.append(String.format("Subtotal:\t$%.2f\n", subtotal));
            receipt.append(String.format("Tax (7%%):\t$%.2f\n", tax));
            receipt.append("---------------------------------\n");
            receipt.append(String.format("Total:\t$%.2f\n", total));
            receipt.append("=================================\n");

            receiptArea.setText(receipt.toString());
        }
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDish.setSelected(false);
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void exitApplication() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
