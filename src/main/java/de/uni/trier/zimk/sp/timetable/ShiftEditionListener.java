/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.oo.Shift;
import de.uni.trier.zimk.sp.timetable.oo.TimetableState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Landry Ngani
 */
class ShiftEditionListener implements ActionListener {

    private TimetableState state;
    private LocationShift shift;
    
    
    public ShiftEditionListener(TimetableState state, LocationShift shift) {
        this.state = state;
        this.shift = shift;
    }

    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Edit the cell "+ shift.toStringInTimetable() );
    }
    
}
