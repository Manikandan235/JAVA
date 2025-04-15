import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Stack;

public class AnimatedCalculatorApp extends JFrame {
    private JTextField display;
    private double num1, num2, result;
    private String operator;
    private double memory;
    private Stack<String> history = new Stack<>();
    private DecimalFormat df = new DecimalFormat("#.########");

    public AnimatedCalculatorApp() {
        // Frame setup
        setTitle("Animated Calculator");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display area
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(new Color(28, 28, 28)); // Dark background for the display
        display.setForeground(new Color(255, 255, 255)); // White text
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 4, 10, 10));
        panel.setBackground(new Color(40, 40, 40)); // Dark background for the button panel
        add(panel, BorderLayout.CENTER);

        // Button setup (including advanced functions)
        String[] buttons = {
            "7", "8", "9", "/", "sqrt", "^2", "%",
            "4", "5", "6", "*", "1/x", "(", ")",
            "1", "2", "3", "-", "sin", "cos", "tan",
            "0", ".", "=", "+", "C", "M+", "M-",
            "MR", "History", "+/-", "Exit"
        };

        // Add buttons to the panel with stylish effects
        for (String text : buttons) {
            JButton button = createStyledButton(text);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        // Add keyboard support
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                if ((key >= '0' && key <= '9') || key == '+' || key == '-' || key == '*' || key == '/' || key == '.' || key == '=' || key == '\n') {
                    handleKeyPress(key);
                }
            }
        });

        setFocusable(true);  // To listen to keyboard events
        setVisible(true);
    }

    // Button click event handler
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            handleAction(command);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setForeground(new Color(255, 255, 255));  // White text
        button.setBackground(new Color(60, 63, 65)); // Dark background for buttons
        button.setFocusPainted(false);  // Remove default focus outline
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        button.setBorderPainted(false); // Remove the border
        button.setOpaque(true);  // Make it opaque to apply color
        button.setToolTipText(text);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 92, 94)); // Lighter gray on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 63, 65)); // Original color when not hovered
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(120, 123, 125)); // Darker color on press for animation effect
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 92, 94)); // Revert back after release
            }
        });

        return button;
    }

    private void handleAction(String command) {
        switch (command) {
            case "=":
                num2 = Double.parseDouble(display.getText());
                performCalculation();
                break;
            case "C":
                display.setText("");
                num1 = num2 = result = 0;
                operator = "";
                break;
            case "+/-":
                display.setText(String.valueOf(-Double.parseDouble(display.getText())));
                break;
            case "sqrt":
                result = Math.sqrt(Double.parseDouble(display.getText()));
                display.setText(formatResult(result));
                break;
            case "^2":
                result = Math.pow(Double.parseDouble(display.getText()), 2);
                display.setText(formatResult(result));
                break;
            case "%":
                num1 = Double.parseDouble(display.getText());
                operator = "%";
                display.setText("");
                break;
            case "1/x":
                result = 1 / Double.parseDouble(display.getText());
                display.setText(formatResult(result));
                break;
            case "sin":
                result = Math.sin(Math.toRadians(Double.parseDouble(display.getText())));
                display.setText(formatResult(result));
                break;
            case "cos":
                result = Math.cos(Math.toRadians(Double.parseDouble(display.getText())));
                display.setText(formatResult(result));
                break;
            case "tan":
                result = Math.tan(Math.toRadians(Double.parseDouble(display.getText())));
                display.setText(formatResult(result));
                break;
            case "(":
            case ")":
            case "+":
            case "-":
            case "*":
            case "/":
                if (!display.getText().isEmpty()) {
                    num1 = Double.parseDouble(display.getText());
                    operator = command;
                    display.setText("");
                }
                break;
            case "Exit":
                System.exit(0);
                break;
            case "M+":
                memory += Double.parseDouble(display.getText());
                break;
            case "M-":
                memory -= Double.parseDouble(display.getText());
                break;
            case "MR":
                display.setText(formatResult(memory));
                break;
            case "History":
                showHistory();
                break;
            default:
                display.setText(display.getText() + command);
                break;
        }
    }

    private void handleKeyPress(char key) {
        if (key >= '0' && key <= '9') {
            display.setText(display.getText() + key);
        } else if (key == '+' || key == '-' || key == '*' || key == '/' || key == '.' || key == '=' || key == '\n') {
            handleAction(String.valueOf(key));
        } else if (key == KeyEvent.VK_BACK_SPACE) {
            String text = display.getText();
            if (text.length() > 0) {
                display.setText(text.substring(0, text.length() - 1));
            }
        }
    }

    private void performCalculation() {
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    display.setText("Error");
                    return;
                }
                break;
            case "%":
                result = num1 * num2 / 100;
                break;
            default:
                return;
        }
        display.setText(formatResult(result));
        history.push(num1 + " " + operator + " " + num2 + " = " + result);
        operator = "";
    }

    private String formatResult(double value) {
        return df.format(value);
    }

    private void showHistory() {
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No history available.", "History", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder historyText = new StringBuilder();
            for (String entry : history) {
                historyText.append(entry).append("\n");
            }
            JOptionPane.showMessageDialog(this, historyText.toString(), "History", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AnimatedCalculatorApp();
    }
}
