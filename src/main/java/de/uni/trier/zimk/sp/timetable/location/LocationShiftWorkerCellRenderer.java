/**
 *
 */
package de.uni.trier.zimk.sp.timetable.location;

import de.uni.trier.zimk.sp.timetable.*;
import de.uni.trier.zimk.sp.timetable.oo.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

/**
 * @author landry.ngani
 *
 */
public class LocationShiftWorkerCellRenderer implements TableCellRenderer {

    private Timetable timetable;
    
    /*
     * private Timetable timetable;
     *
     * public ShiftCellRenderer(Timetable timetable){ this.timetable =
     * timetable; }
     */
    public LocationShiftWorkerCellRenderer(Timetable timetable) {
        this.timetable = timetable; 
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if( value == null ){
            JLabel label = new JLabel( " CLOSED " );
            label.setFont( new Font("Arial", Font.PLAIN, 18) );
            label.setHorizontalAlignment( JLabel.CENTER);
            return label;
        }
        
        /*
        LocationShift shift = (LocationShift) value;
        StringBuilder stringValue = new StringBuilder();
        List<Worker> workers = shift.getWorkers();
        for(Worker worker : workers){
            stringValue.append( worker.getName() + "\n" );
        }
        
        JLabel panel = new JLabel( stringValue.toString() );
        panel.setFont( new Font("Arial", Font.PLAIN, 16) );
        panel.setHorizontalTextPosition( JLabel.CENTER );
        panel.setHorizontalAlignment( JLabel.CENTER);
        return panel;
        */
        
        LocationShift shift = (LocationShift) value;
        JPanel panel = new JPanel(new BorderLayout());

        JLabel sLabel = new JLabel(shift.toStringInTimetable());
        sLabel.setOpaque( true );
        sLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(sLabel, BorderLayout.NORTH);
        
        if (shift != null) {
            // Background & Foreground 
             if(shift.getStart() == 10 && shift.getEnd() == 11){
                panel.setBackground(Color.GREEN);
            }
            if (row % 2 == 0) {

                if (shift.getCapacity() == 0) {
                    panel.setBackground(Color.WHITE);
                    sLabel.setBackground(Color.BLACK);
                } else {
                    if (shift.getWorkers().isEmpty()) {
                        panel.setBackground(Color.WHITE);
                    } else {
                        if (! shift.isMatchingDayPrefences()) {
                            panel.setBackground(Color.WHITE);
                            sLabel.setBackground(Color.RED);
                        } else {
                            if ( ! shift.isMatchingShiftPrefences()) {
                                panel.setBackground(Color.WHITE);
                                sLabel.setBackground(Color.ORANGE);
                            } else {
                                panel.setBackground(Color.WHITE);
                            }
                        }
                    }
                }
                /*
                 * panel.setBackground(isSelected ? (
                 * !(block.matchingDayPrefences()) ? (
                 * !(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED
                 * ) : new Color(230,238,245) ) : Color.WHITE );
                 */

            } else {

                if (shift.getCapacity() == 0) {
                    panel.setBackground(new Color(212, 218, 222));
                    sLabel.setBackground(Color.BLACK);
                } else {
                    if (shift.getWorkers().isEmpty()) {
                        panel.setBackground(new Color(212, 218, 222));
                    } else {
                        if (! shift.isMatchingDayPrefences()) {
                            panel.setBackground(new Color(212, 218, 222));
                            sLabel.setBackground(Color.RED);
                        } else {
                            if (! shift.isMatchingShiftPrefences()) {
                                panel.setBackground(new Color(212, 218, 222));
                                sLabel.setBackground(Color.ORANGE);
                            } else {
                                panel.setBackground(new Color(212, 218, 222));
                            }
                        }
                    }
                }
                /*
                 * panel.setBackground(isSelected ? (
                 * !(block.matchingDayPrefences()) ?
                 * (!(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED
                 * ) : new Color(230,238,245)) : new Color(212, 218, 222) );
                 */
            }

            panel.setToolTipText(shift.toString());

            panel.setMinimumSize(new Dimension(10, 50));

            JPanel container = new JPanel();
            container.setLayout(new FlowLayout(FlowLayout.LEFT));
            
            List<Worker> fworkers = shift.getFixedWorkers();
            Collections.sort(fworkers, new WorkerComparable());
            for (Worker worker : fworkers) {
                JPanel ipanel = new JPanel(new BorderLayout());
                ipanel.setMinimumSize(new Dimension(100,20));
                ipanel.setPreferredSize(new Dimension(100,20));
                
                /*
                ipanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), 
                "F", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 0, 14), java.awt.Color.black)); // NOI18N
                */
                        
                JLabel workerLabel = new JLabel(worker.getName());
                workerLabel.setOpaque(true);
                workerLabel.setBackground( worker.getColor().getColor() );
                
                JLabel ilabel = new JLabel(" F ");
                ilabel.setFont( new java.awt.Font("Aharoni", 0, 14) );
                ilabel.setOpaque(true);
                ilabel.setPreferredSize(new Dimension(20,20));
                /*
                if (!worker.isWillingForShift(shift)) {
                    ilabel.setBackground(Color.RED);
                } else {
                    ilabel.setBackground(worker.getColor().getColor());
                }
                */
                ipanel.add(workerLabel, BorderLayout.CENTER);
                ipanel.add(ilabel, BorderLayout.EAST);

                container.add(ipanel);
            }
            
            List<Worker> workers = shift.getWorkers();
            Collections.sort(workers, new WorkerComparable());
            for (Worker worker : workers) {
                JPanel ipanel = new JPanel(new BorderLayout());
                ipanel.setMinimumSize(new Dimension(100,20));
                ipanel.setPreferredSize(new Dimension(100,20));
                
                JLabel workerLabel = new JLabel(worker.getName());
                workerLabel.setOpaque(true);
                workerLabel.setBackground( worker.getColor().getColor() );
                
                JLabel ilabel = new JLabel("   ");
                ilabel.setOpaque(true);
                ilabel.setPreferredSize(new Dimension(20,20));
                if (!worker.isWillingForShift(shift)) {
                    ilabel.setBackground(Color.RED);
                } else {
                    ilabel.setBackground(worker.getColor().getColor());
                }
                
                ipanel.add(workerLabel, BorderLayout.CENTER);
                ipanel.add(ilabel, BorderLayout.EAST);

                container.add(ipanel);
            }
            panel.add(container, BorderLayout.CENTER);
            container.setBackground(container.getParent().getBackground());

            JLabel statusLabel = new JLabel();
            if (shift.isChanged()) {
                statusLabel.setText("");
                statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                statusLabel.setOpaque(false);
                statusLabel.setBackground(Color.BLUE);
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/modified_icon.png")));
            }
            panel.add(statusLabel, BorderLayout.EAST);
            
        }


        return panel;
    }
    
}
