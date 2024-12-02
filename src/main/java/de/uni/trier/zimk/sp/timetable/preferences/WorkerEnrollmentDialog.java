package de.uni.trier.zimk.sp.timetable.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import de.uni.trier.zimk.sp.timetable.DayShiftTimetableTableModel;
import de.uni.trier.zimk.sp.timetable.oo.Workday;
import de.uni.trier.zimk.sp.timetable.oo.Worker;
import de.uni.trier.zimk.sp.timetable.preferences.Preferences;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

public class WorkerEnrollmentDialog extends JPanel {

    private Color selectedColor = Color.WHITE;
    List<Preferences> preferences = new ArrayList<>();

    private void saveWorkerDetails(String name, String username, String password, boolean mutable,
            double debit, double ratio, Color color, List<Preferences> preferences) throws FileNotFoundException, IOException {
        String filePath = "src/main/resources/de/uni/trier/zimk/sp/timetable/io/configuration.json";
        File jsonFile = new File(filePath);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject newWorker = new JsonObject();

        // Add worker details to JSON object
        newWorker.addProperty("name", name);
        newWorker.addProperty("username", username);
        newWorker.addProperty("password", password);
        newWorker.addProperty("role", 2);
        newWorker.addProperty("mutable", mutable);
        newWorker.addProperty("debit", debit);
        newWorker.addProperty("ratio", ratio);

        // Add color as a JSON object
        JsonObject colorObject = new JsonObject();
        colorObject.addProperty("redValue", color.getRed());
        colorObject.addProperty("greenValue", color.getGreen());
        colorObject.addProperty("blueValue", color.getBlue());
        newWorker.add("color", colorObject);

        try {
            JsonObject root;
            JsonArray workers;

            if (jsonFile.exists()) {

                try (Reader reader = new FileReader(jsonFile)) {
                    root = gson.fromJson(reader, JsonObject.class);

                    // If "workers" key doesn't exist, create a new array
                    workers = root.has("workers") ? root.getAsJsonArray("workers") : new JsonArray();
                }
            } else {
                // Create new root object if file doesn't exist
                root = new JsonObject();
                workers = new JsonArray();
                root.add("workers", workers);
            }

            // Add new worker to the array
            workers.add(newWorker);

            // Write back to file
            try (Writer writer = new FileWriter(jsonFile)) {
                gson.toJson(root, writer);
            }

            // Simulate saving to database
            System.out.println("Worker Details Saved:");
            System.out.println("Name: " + name);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Debit (Hours): " + debit);
            System.out.println("Ratio: " + ratio);
            System.out.println("Color: " + selectedColor + "\n");

            JOptionPane.showMessageDialog(WorkerEnrollmentDialog.this,
                    "Worker successfully enrolled!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(WorkerEnrollmentDialog.this,
                    "Debit and Ratio must be numbers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public WorkerEnrollmentDialog() {

        setLayout(new GridLayout(11, 1, 50, 0));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel debitLabel = new JLabel("Debit (Working Hours):");
        JTextField debitField = new JTextField();

        JLabel ratioLabel = new JLabel("Ratio:");
        JTextField ratioField = new JTextField();

        JLabel workdayLabel = new JLabel("Workday:");
        JComboBox workdayComboBox = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});

        JLabel numberOfHoursLabel = new JLabel("Number of Hours:");
        JTextField hoursField = new JTextField();

        JLabel startTimeLabel = new JLabel("Start Time:");
        JTextField startField = new JTextField();

        JLabel endTimeLabel = new JLabel("End Time:");
        JTextField endField = new JTextField();

        JLabel colorLabel = new JLabel("Color:");
        JButton colorButton = new JButton("Choose Color");
        JLabel colorPreview = new JLabel();
        colorPreview.setOpaque(true);
        colorPreview.setBackground(selectedColor);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Adding components to the panel
        add(nameLabel);
        add(nameField);

        add(usernameLabel);
        add(usernameField);

        add(passwordLabel);
        add(passwordField);

        add(debitLabel);
        add(debitField);

        add(ratioLabel);
        add(ratioField);

        add(workdayLabel);
        add(workdayComboBox);

        add(numberOfHoursLabel);
        add(hoursField);

        add(startTimeLabel);
        add(startField);

        add(endTimeLabel);
        add(endField);

        add(colorLabel);
        JPanel colorPanel = new JPanel(new BorderLayout());
        colorPanel.add(colorButton, BorderLayout.WEST);
        colorPanel.add(colorPreview, BorderLayout.CENTER);
        add(colorPanel);

        add(saveButton);
        add(cancelButton);

        // Color chooser action
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(
                    WorkerEnrollmentDialog.this,
                    "Choose a Color",
                    selectedColor
            );
            if (newColor != null) {
                selectedColor = newColor;
                colorPreview.setBackground(newColor); // Update preview
            }
        });

        // Save button action
        saveButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String debit = debitField.getText();
            String ratio = ratioField.getText();
            String workdayName = (String) workdayComboBox.getSelectedItem();
            int workdayId = workdayComboBox.getSelectedIndex() + 1; // Assuming IDs start at 1
            int numberOfShifts = 7; 
            int numberOfHours = Integer.parseInt(hoursField.getText());
            int start = Integer.parseInt(startField.getText());
            int end = Integer.parseInt(endField.getText());
            
            preferences.add(new Preferences(new Workday(workdayId, workdayName, numberOfShifts), numberOfHours, workdayId, start, end));

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || debit.isEmpty() || ratio.isEmpty()) {
                JOptionPane.showMessageDialog(WorkerEnrollmentDialog.this,
                        "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double debitValue = Double.parseDouble(debit);
                    double ratioValue = Double.parseDouble(ratio);
                    saveWorkerDetails(name, username, password, true, debitValue, ratioValue, selectedColor,preferences);
                } catch (IOException ex) {
                    Logger.getLogger(WorkerEnrollmentDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> {

            nameField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            debitField.setText("");
            ratioField.setText("");
            workdayComboBox.setSelectedItem("Monday");
            hoursField.setText("");
            startField.setText("");
            endField.setText("");
            selectedColor = Color.WHITE;
            colorPreview.setBackground(selectedColor); // Reset color preview
        }
        );

    }
    // Override getPreferredSize to set a fixed size for the JPanel

    @Override
    public Dimension getPreferredSize() {
        // Specify the preferred size for the panel (independent of JFrame)
        return new Dimension(500, 300); // Adjust these values as needed
    }
}
