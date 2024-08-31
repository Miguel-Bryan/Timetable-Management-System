/**
 *
 */
package de.uni.trier.zimk.sp.timetable.configuration;

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

import javax.swing.*;

/**
 * @author landry.ngani
 *
 */
public class LocationShiftCellRenderer implements TableCellRenderer {

    private Timetable timetable;
    
    /*
     * private Timetable timetable;
     *
     * public ShiftCellRenderer(Timetable timetable){ this.timetable =
     * timetable; }
     */
    public LocationShiftCellRenderer(Timetable timetable) {
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
        
        LocationShift shift = (LocationShift) value;
        JLabel panel = new JLabel( shift.getStart() + " to " + shift.getEnd()+ "  -- [ "+ shift.getCapacity() + " ]" );
        panel.setFont( new Font("Arial", Font.PLAIN, 18) );
        panel.setHorizontalTextPosition( JLabel.CENTER );
        panel.setHorizontalAlignment( JLabel.CENTER);
        return panel;
    }
    
}
