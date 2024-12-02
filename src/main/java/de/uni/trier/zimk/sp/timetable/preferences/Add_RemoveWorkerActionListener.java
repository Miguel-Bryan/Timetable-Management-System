package de.uni.trier.zimk.sp.timetable.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author bryan
 */
public class Add_RemoveWorkerActionListener implements ActionListener {

    private WorkerEnrollmentDialog panel;
    private EditableDayShiftTimetableTableModel dayShiftTimetableTableModel;
    private UsersAndPreferencesModel model;
    private boolean isFrameOpen = false;
    private JFrame frame = null;

    private boolean isWorkerPresent(String username) throws IOException {

        FileReader reader = new FileReader("src/main/resources/de/uni/trier/zimk/sp/timetable/io/configuration.json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        // Get the workers array
        JsonArray workersArray = jsonObject.getAsJsonArray("workers");
        // Iterate through workers to check for a match
        for (JsonElement element : workersArray) {
            JsonObject worker = element.getAsJsonObject();
            if (worker.get("username").getAsString().equals(username)) {
                return true;
            }
        }

        return false;
    }

    private void writeJsonToFile(JsonObject jsonObject, String filePath) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
    try (FileWriter writer = new FileWriter(filePath)) {
        gson.toJson(jsonObject, writer); 
    }
}

    
    private void removeWorker(String username) throws IOException {

        FileReader reader = new FileReader("src/main/resources/de/uni/trier/zimk/sp/timetable/io/configuration.json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

        JsonArray workersArray = jsonObject.getAsJsonArray("workers");
        JsonArray updatedWorkers = new JsonArray();

        // Filter workers
        for (JsonElement element : workersArray) {
            JsonObject worker = element.getAsJsonObject();
            if (!worker.get("username").getAsString().equals(username)) {
                updatedWorkers.add(worker); // Add workers that don't match the username
            }
        }

        // Update the JSON object with the new workers array
        jsonObject.add("workers", updatedWorkers);
        
        
        writeJsonToFile(jsonObject,"src/main/resources/de/uni/trier/zimk/sp/timetable/io/configuration.json");
        
    }
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equalsIgnoreCase("ADD BUTTON")) {

            if (isFrameOpen) { // Check if the frame is already open
                JOptionPane.showMessageDialog(null, "The Worker Registration Frame is already open!");
            } else {
                // Create and set up the JFrame
                frame = new JFrame("WORKER REGISTRATION");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new FlowLayout());
//                frame.setSize(555, 340);
                frame.setSize(595, 380);
                // Add the worker enrollment panel
                panel = new WorkerEnrollmentDialog(); 
                panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); 

                frame.add(panel, BorderLayout.CENTER);
                frame.setLocationRelativeTo(null); // Center the frame on screen
                frame.setVisible(true);

                // Set flag to true and handle frame closing
                isFrameOpen = true;
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        isFrameOpen = false; // Reset flag when frame is closed
                    }
                });
            }

        } else if (e.getActionCommand().equalsIgnoreCase("REMOVE BUTTON")) {
            // Handle remove button action
            String usernameToRemove = JOptionPane.showInputDialog(null, "Enter the username of the worker to remove:");
            if (usernameToRemove != null && !usernameToRemove.trim().isEmpty()) {
                try {
                    if (isWorkerPresent(usernameToRemove.trim())) {

                        int confirmation = JOptionPane.showConfirmDialog(
                                null,
                                "Are you sure you want to remove this worker?",
                                "Confirm Removal",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirmation == JOptionPane.YES_OPTION) {
                             removeWorker(usernameToRemove.trim());
                             UsersAndPreferencesEditor editor = new UsersAndPreferencesEditor(model,dayShiftTimetableTableModel);
                             editor.repaint();
                              JOptionPane.showMessageDialog(null, "Worker removed successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Worker not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!");
            }
        }
    }
}
