import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MiniTextEditor extends JFrame {
    private JTextField textField;
    private JTextArea textArea;
    private JComboBox<String> fontStyleBox, fontFamilyBox;
    private JSpinner fontSizeSpinner;
    private JButton colorButton, saveButton, loadButton;
    private JLabel statusLabel;
    private Color currentColor = Color.BLACK;

    public MiniTextEditor() {
        setTitle("🔥 Ultimate Mini Text Editor");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // North Panel - Controls
        JPanel topPanel = new JPanel(new FlowLayout());

        textField = new JTextField(20);
        topPanel.add(new JLabel("Input:"));
        topPanel.add(textField);

        fontStyleBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic"});
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(16, 8, 72, 1));
        fontFamilyBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

        topPanel.add(new JLabel("Font:"));
        topPanel.add(fontFamilyBox);
        topPanel.add(fontStyleBox);
        topPanel.add(new JLabel("Size:"));
        topPanel.add(fontSizeSpinner);

        colorButton = new JButton("Color 🎨");
        topPanel.add(colorButton);

        JButton formatButton = new JButton("Format");
        topPanel.add(formatButton);

        JButton clearButton = new JButton("Clear");
        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        // Center - Text Area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // South Panel - File buttons + Status
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel filePanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Save 💾");
        loadButton = new JButton("Load 📂");

        filePanel.add(saveButton);
        filePanel.add(loadButton);

        statusLabel = new JLabel("Words: 0 | Characters: 0", SwingConstants.CENTER);
        bottomPanel.add(filePanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        formatButton.addActionListener(e -> formatText());
        clearButton.addActionListener(e -> {
            textField.setText("");
            textArea.setText("");
            updateStatus();
        });

        colorButton.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(null, "Pick Text Color", currentColor);
            if (chosen != null) {
                currentColor = chosen;
                formatText();
            }
        });

        saveButton.addActionListener(e -> saveToFile());
        loadButton.addActionListener(e -> loadFromFile());

        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                formatText();
            }
        });

        setVisible(true);
    }

    private void formatText() {
        String text = textField.getText();
        int style = fontStyleBox.getSelectedIndex();
        int size = (int) fontSizeSpinner.getValue();
        String fontName = (String) fontFamilyBox.getSelectedItem();

        Font font = new Font(fontName, style, size);
        textArea.setFont(font);
        textArea.setForeground(currentColor);
        textArea.setText(text);

        updateStatus();
    }

    private void updateStatus() {
        String content = textArea.getText();
        int words = content.trim().isEmpty() ? 0 : content.trim().split("\\s+").length;
        int chars = content.length();
        statusLabel.setText("Words: " + words + " | Characters: " + chars);
    }

    private void saveToFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(this, "Saved! ✅");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file ❌");
            }
        }
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                textField.setText(sb.toString().trim());
                formatText();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading file ❌");
            }
        }
    }

    public static void main(String[] args) {
        new MiniTextEditor();
    }
}
