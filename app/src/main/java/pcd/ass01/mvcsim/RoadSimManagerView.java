package pcd.ass01.mvcsim;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class RoadSimManagerView extends JFrame {

    private final RoadSimController controller;
    private JLabel stepsLabel;
    private JTextField stepsField;
    private JLabel carsLabel;
    private JTextField carsField;
    private JLabel environmentLabel;
    private JComboBox<String> environmentComboBox;
    private JCheckBox displayCheckBox;
    private JButton startButton;

    public RoadSimManagerView(RoadSimController controller) {
        setTitle("Simulation");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize components
        stepsLabel = new JLabel("Number of steps:");
        stepsField = new JTextField(10);
        carsLabel = new JLabel("Number of cars:");
        carsField = new JTextField(10);
        environmentLabel = new JLabel("Environment:");
        String[] environments = controller.getEnvironmentNames();
        environmentComboBox = new JComboBox<>(environments);
        displayCheckBox = new JCheckBox("Display");
        startButton = new JButton("Start");
        this.controller = controller;

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(stepsLabel);
        inputPanel.add(stepsField);
        inputPanel.add(carsLabel);
        inputPanel.add(carsField);
        inputPanel.add(environmentLabel);
        inputPanel.add(environmentComboBox);
        inputPanel.add(displayCheckBox);
        contentPane.add(inputPanel, BorderLayout.NORTH);

        contentPane.add(startButton, BorderLayout.SOUTH);

        // Call to controller
        startButton.addActionListener(e -> {
            if (controller.simulationIsRunning()) {
                System.out.println("stopping");
                stopSimulation();
            } else {
                System.out.println("starting");
                startSimulation();
            }
        });
        controller.onSimulationStart(() -> SwingUtilities.invokeLater(() -> startButton.setText("Stop")));
        controller.onSimulationStop(() -> SwingUtilities.invokeLater(() -> startButton.setText("Start")));
    }

    private void stopSimulation() {
        controller.stopSimulation();
    }

    private void startSimulation() {
        Optional<Integer> steps = getSteps();
        Optional<Integer> cars = getCars();
        String environment = (String) environmentComboBox.getSelectedItem();
        boolean display = displayCheckBox.isSelected();

        if (steps.isPresent() && cars.isPresent()) {
            controller.startSimulation(steps.get(), cars.get(), environment, display);
        } else {
            JOptionPane.showMessageDialog(RoadSimManagerView.this, "Please enter number of steps/cars.");
        }
    }

    private Optional<Integer> getSteps() {
        try {
            return Optional.of(Integer.parseInt(stepsField.getText()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    private Optional<Integer> getCars() {
        try {
            return Optional.of(Integer.parseInt(carsField.getText()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
