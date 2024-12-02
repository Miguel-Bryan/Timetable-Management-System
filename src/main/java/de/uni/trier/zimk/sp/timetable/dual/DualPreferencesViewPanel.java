/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.dual;

import de.uni.trier.zimk.sp.timetable.ShiftPreferencesCellRenderer;
import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.preferences.UsersAndPreferencesModel;
import de.uni.trier.zimk.sp.timetable.preferences.EditableDayShiftTimetableTableModel;
import de.uni.trier.zimk.sp.timetable.oo.Worker;
import de.uni.trier.zimk.sp.timetable.oo.WorkerComparable;
import de.uni.trier.zimk.sp.timetable.preferences.AddWorkerDialog;
import java.awt.Point;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author Landry Ngani
 */
public class DualPreferencesViewPanel extends javax.swing.JPanel {

    /**
     * Creates new form UsersAndPreferencesEditor
     * /
    public UsersAndPreferencesEditor() {
        initComponents();
    }
    */
    

    public DualPreferencesViewPanel(UsersAndPreferencesModel model, EditableDayShiftTimetableTableModel dayShiftTimetableTableModel) {
        this.model = model;
        this.dayShiftTimetableTableModel = dayShiftTimetableTableModel;
        
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        workerTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        workerTable.setModel(dayShiftTimetableTableModel);
        workerTable.setRowHeight(100);
        workerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                workerTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(workerTable);
        for(int i = 0; i<dayShiftTimetableTableModel.getColumnCount(); i++ ){
            workerTable.getColumnModel().getColumn(i).setCellRenderer(
                new ShiftPreferencesCellRenderer(model.getTimetable()) );
        }

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void workerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workerTableMouseClicked
        if( evt.isControlDown() ){
            
            Point p = evt.getPoint();
            int row = workerTable.rowAtPoint(p); 
            int column = workerTable.columnAtPoint(p); 
            
            EditableDayShiftTimetableTableModel tableModel = (EditableDayShiftTimetableTableModel) ((JTable)evt.getComponent()).getModel();
            LocationShift shift = (LocationShift)tableModel.getValueAt(row, column);
            
            if( shift != null ){
                
                List<Worker> volunteers = new AddWorkerDialog(model.getTimetable(), shift).getSelectedWorkers();
                Collections.sort(volunteers, new WorkerComparable());
                
                model.getTimetable().getTimetableState().updateVolunteers( shift , volunteers);
                model.getTimetable().fireStateChanged();
                
                
            }
        }
    }//GEN-LAST:event_workerTableMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable workerTable;
    // End of variables declaration//GEN-END:variables

    private EditableDayShiftTimetableTableModel dayShiftTimetableTableModel;
    private UsersAndPreferencesModel model;
    
    
    public void setTableModel(EditableDayShiftTimetableTableModel tableModel) {
        this.dayShiftTimetableTableModel = tableModel;
        workerTable.setModel(tableModel);
    }
}
