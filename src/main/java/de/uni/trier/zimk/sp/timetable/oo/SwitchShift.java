/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import java.util.ArrayList;

/**
 *
 * @author Landry Ngani
 */
public class SwitchShift {
    
    private LocationShift shift;
    private LocationShift shiftTarget;
    private ArrayList<LocationShift> toExclude;

    
    
    public SwitchShift(LocationShift shift) {
        this.shift = shift;
        this.toExclude = new ArrayList<LocationShift>();
    }
    
    
    
    public LocationShift getShiftTarget() {
        return shiftTarget;
    }

    public void setShiftTarget(LocationShift shiftTarget) {
        this.shiftTarget = shiftTarget;
    }
 

    public LocationShift getShift() {
        return shift;
    }

    public void setShift(LocationShift shift) {
        this.shift = shift;
    }

    public ArrayList<LocationShift> getToExclude() {
        return toExclude;
    }

    public void setToExclude(ArrayList<LocationShift> toExclude) {
        this.toExclude = toExclude;
    }

    @Override
    public String toString() {
        return "SwitchShift{" + "shift=" + shift + ", shiftTarget=" + shiftTarget + ", toExclude=" + toExclude + '}';
    }
    
    

}
