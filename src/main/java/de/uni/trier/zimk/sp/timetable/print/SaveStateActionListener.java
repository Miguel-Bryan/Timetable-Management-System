/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.uni.trier.zimk.sp.timetable.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uni.trier.zimk.sp.timetable.oo.OrganisationalConfiguration;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import de.uni.trier.zimk.sp.timetable.views.PlannerView;
import de.uni.trier.zimk.sp.timetable.print.PrintActionListener;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JTabbedPane;

/**
 *
 * @author bryan
 */
public class SaveStateActionListener implements ActionListener {

    private PlannerView plannerView;
    private OrganisationalConfiguration configuration;
    private Timetable timetable;
    private PrintDialog printDialog;

    public SaveStateActionListener(PrintDialog printDialog, PlannerView plannerView, OrganisationalConfiguration configuration, Timetable timetable) {
        this.printDialog = printDialog;
        this.plannerView = plannerView;
        this.configuration = configuration;
        this.timetable = timetable;
    }

     @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equalsIgnoreCase(PrintStringConst.SAVE_STATE)) {
            
             //this.printDialog = new PrintDialog(plannerView, configuration, timetable);
             System.out.println("SAVE COMMAND ");

            JTabbedPane pane = printDialog.getLocationPrintTabView().getTabbedPane();

            for (Component component : pane.getComponents()) {
                if (component instanceof LocationPrintTimetableView) {
                    LocationPrintTimetableView panelToSave = ((LocationPrintTimetableView) component);
                    
                    String filePath = System.getProperty("user.home") + File.separator + panelToSave.getLocationAwarePrintTableModel().getValueList().getName() + ".json";
                    try {
                        
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(panelToSave);
                        System.out.println("Json String : " + json);
                        
                        FileWriter fileWriter = new FileWriter(filePath);
                        ObjectMapper mapper = new ObjectMapper();
                        fileWriter.write(json);

                        System.out.println("Current state successfully saved at: " + filePath);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        
                              
                    }
//                            document.open();
//                            File fileContent = mapper.readValue(filePath, File.class);

                }else{
                    System.out.println("No LocationPrintTabView ");
                }
            }
        }
        else {
             System.out.println("NO SAVE COMMAND ");
        }
        
        

    }
}
