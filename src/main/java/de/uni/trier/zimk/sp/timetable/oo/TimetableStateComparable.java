/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import java.util.Comparator;

/**
 *
 * @author me
 */
public class TimetableStateComparable implements Comparator<TimetableState> {

    public int compare(TimetableState state1, TimetableState state2) {
        return (state1.getTotalCost() - state2.getTotalCost() );
    }
    
}
