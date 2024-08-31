/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.oo.Worker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Landry Ngani
 */
public class PreferenceCellPanel extends javax.swing.JPanel {

    private int row;
    private LocationShift shift;
    private List<Worker> volunteers;
    
    /**
     * Creates new form PreferenceCellPanel
     */
    public PreferenceCellPanel( int row, LocationShift shift, List<Worker> volunteers) {
    
        this.row = row;
        this.shift = shift; 
        this.volunteers = volunteers;
        
        initComponents();
        
        initContent();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void initContent() {
        if (shift != null) {
            // Background & Foreground 
            if (row % 2 == 0) {

                if (volunteers.isEmpty()) {
                    this.setBackground(Color.WHITE);
                } else {
                    
                    this.setBackground(Color.BLUE);
                    /*
                    if ( shift.isExceedingShiftCapacities() ) {
                        this.setBackground(Color.GREEN);
                    } else {
                        if ( shift.isLessThanShiftCapacities() ) {
                            this.setBackground(Color.ORANGE);
                        } else {
                            this.setBackground(Color.WHITE);
                        }
                    }
                    */
                }
                /*
                 * panel.setBackground(isSelected ? (
                 * !(block.matchingDayPrefences()) ? (
                 * !(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED
                 * ) : new Color(230,238,245) ) : Color.WHITE );
                 */

            } else {

                if (volunteers.isEmpty()) {
                    this.setBackground(new Color(212, 218, 222));
                } else {
                    
                    this.setBackground(Color.BLUE);
                    /*
                    if ( shift.isExceedingShiftCapacities()) {
                        this.setBackground(Color.GREEN);
                    } else {
                        if ( shift.isLessThanShiftCapacities() ) {
                            this.setBackground(Color.ORANGE);
                        } else {
                            this.setBackground(new Color(212, 218, 222));
                        }
                    }
                    */
                            
                }
                /*
                 * panel.setBackground(isSelected ? (
                 * !(block.matchingDayPrefences()) ?
                 * (!(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED
                 * ) : new Color(230,238,245)) : new Color(212, 218, 222) );
                 */
            }

            //TODO 
            this.setToolTipText(shift.toString());
            this.setMinimumSize(new Dimension(100, 100));

            JPanel container = new JPanel();
            container.setLayout(new FlowLayout(FlowLayout.LEFT));
            for (Worker worker : volunteers) {
                JPanel ipanel = new JPanel();
                JLabel ilabel = new JLabel(worker.getName());
                ipanel.add(ilabel);

                container.add(ipanel);
                if (!worker.isWillingForShift(shift)) {
                    ipanel.setBackground(Color.GRAY);
                } else {
                    ipanel.setBackground(ipanel.getParent().getBackground());
                }
            }
            this.add(container, BorderLayout.CENTER);
            container.setBackground(container.getParent().getBackground());
            
            /*
            JButton editButton = new JButton("Edit");
            editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/modified_icon.png")));
            EditableDayShiftTimetableTableModel tableModel = (EditableDayShiftTimetableTableModel)table.getModel();
            editButton.addActionListener( new ShiftEditionListener(tableModel.getValueList(), shift) );
            
            panel.add(editButton, BorderLayout.SOUTH);
            */
            
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public LocationShift getShift() {
        return shift;
    }

    public void setShift(LocationShift shift) {
        this.shift = shift;
    }

    public List<Worker> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Worker> volunteers) {
        this.volunteers = volunteers;
    }
    
    
}