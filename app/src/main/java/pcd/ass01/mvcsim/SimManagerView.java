package pcd.ass01.mvcsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimManagerView extends JFrame {
    private JLabel stepsLabel;
    private JTextField stepsField;
    private JButton startButton;

    public SimManagerView() {
        setTitle("Simulation");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        stepsLabel = new JLabel("Number of steps:");
        stepsField = new JTextField(10);
        startButton = new JButton("Start");

        // Add components to content pane with BorderLayout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Panel to hold label and text field
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(stepsLabel);
        topPanel.add(stepsField);
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Add button to the bottom
        contentPane.add(startButton, BorderLayout.SOUTH);

        // Add action listener to start button
        startButton.addActionListener(e -> {
            String stepsText = stepsField.getText();
            if (!stepsText.isEmpty()) {
                int steps = Integer.parseInt(stepsText);
                // Perform action with the number of steps
                JOptionPane.showMessageDialog(SimManagerView.this, "Starting simulation with " + steps + " steps.");
            } else {
                JOptionPane.showMessageDialog(SimManagerView.this, "Please enter number of steps.");
            }
        });
    }
    public String getStepsText() {
        return stepsField.getText();
    }
    public void setStepsText(String text) {
        stepsField.setText(text);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimManagerView().setVisible(true));
    }
}