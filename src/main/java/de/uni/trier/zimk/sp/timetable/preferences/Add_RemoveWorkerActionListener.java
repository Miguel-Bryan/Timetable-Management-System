/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.uni.trier.zimk.sp.timetable.preferences;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author bryan
 */
public class AddWorkerActionListener implements ActionListener {
    private JTextField nameField;
    private JSpinner workingHours;
    private UsersAndPreferencesEditor editor;
    
    private AddWorkerPanel panel;
    
    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getActionCommand().equalsIgnoreCase("ADD BUTTON")){
           
           JDialog bryanDialog = new JDialog();
           bryanDialog.setLayout(new BorderLayout());
           bryanDialog.setSize(500,300);
           
           panel = new AddWorkerPanel();
           panel.setVisible(true);
           
           bryanDialog.add(panel, BorderLayout.CENTER);
           bryanDialog.setLocationRelativeTo(null);
           bryanDialog.setAlwaysOnTop(true);
           
           if(bryanDialog.isVisible()){
                bryanDialog.setVisible(false);
           }else{
                bryanDialog.setVisible(true);
           }

       }else {
           
           if(e.getActionCommand().equalsIgnoreCase("REMOVE BUTTON")){
           
           }
       }
    }
    
}
