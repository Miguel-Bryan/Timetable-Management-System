/**
 *
 */
package de.uni.trier.zimk.sp.timetable.print;

import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.oo.Worker;
import de.uni.trier.zimk.sp.timetable.oo.WorkerComparable;
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
public class LocationShiftWorkerPrintCellRenderer implements TableCellRenderer {

    private Timetable timetable;
    
    /*
     * private Timetable timetable;
     *
     * public ShiftCellRenderer(Timetable timetable){ this.timetable =
     * timetable; }
     */
    public LocationShiftWorkerPrintCellRenderer(Timetable timetable) {
        this.timetable = timetable; 
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if( value == null ){
            JLabel label = new JLabel( " CLOSED " );
            label.setFont( new Font("Arial", Font.BOLD, 18) );
            label.setHorizontalAlignment( JLabel.CENTER);
            return label;
        }

        LocationShift shift = (LocationShift) value;
        JPanel panel = new JPanel(new BorderLayout());

        if (shift != null) {
            
            // Background & Foreground 
            if (row % 2 == 0) {
                panel.setBackground(Color.WHITE);
            } else {
                panel.setBackground(new Color(212, 218, 222));
            }

            panel.setToolTipText(shift.toString());
            panel.setMinimumSize(new Dimension(100, 40));
            panel.setPreferredSize(new Dimension(100, 40));

            JPanel container = new JPanel();
            List<Worker> allWorkers = shift.getAllWorkers();
            Collections.sort(allWorkers, new WorkerComparable());

            container.setLayout(new GridLayout(1, allWorkers.size()));
            if( ! allWorkers.isEmpty() ){
                for (Worker worker : allWorkers) {
                    JLabel workerLabel = new JLabel(worker.getName());
                    workerLabel.setFont( new Font("Arial", Font.BOLD, 16) );
                    workerLabel.setHorizontalAlignment( JLabel.CENTER);
                    workerLabel.setOpaque(true);
                    workerLabel.setBackground( worker.getColor().getColor() );
                    container.add(workerLabel);
                }    
            }

            panel.add(container, BorderLayout.CENTER);
            container.setBackground(container.getParent().getBackground());
                    
        }


        return panel;
    }
    
}
