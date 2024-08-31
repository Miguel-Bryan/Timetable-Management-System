/**
 *
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.oo.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
public class ShiftPreferencesCellRenderer implements TableCellRenderer {

    private Timetable timetable;
    
    /*
     * private Timetable timetable;
     *
     * public ShiftCellRenderer(Timetable timetable){ this.timetable =
     * timetable; }
     */
    public ShiftPreferencesCellRenderer(Timetable timetable) {
        this.timetable = timetable; 
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        
        LocationShift shift = (LocationShift) value;
        List<Worker> volunteers = timetable.getTimetableState().getShiftVolunteers(shift);
        Collections.sort(volunteers, new WorkerComparable() );
        
        PreferenceCellPanel panel = new PreferenceCellPanel( row, shift, volunteers);
        return panel;
    }
}
