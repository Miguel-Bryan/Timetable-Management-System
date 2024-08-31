/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.configuration;

import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import java.util.Comparator;

/**
 *
 * @author me
 */
public class LocationShiftComparable implements Comparator<LocationShift> {

    public int compare(LocationShift shift1, LocationShift shift2) {
        return (shift1.getStart() - shift2.getStart());
    }
    
}
