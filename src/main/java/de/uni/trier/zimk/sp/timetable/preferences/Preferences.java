/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.uni.trier.zimk.sp.timetable.preferences;

import com.google.gson.annotations.Expose;
import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.oo.Workday;

/**
 *
 * @author bryan
 */
public  class Preferences {
    @Expose (serialize = false, deserialize = false)
    private Workday workday;
    
    @Expose (serialize = false, deserialize = false)
    private int numberOfHours;
    private int workdayId;
    private int start;
    private int end; 

    public Preferences(Workday workday, int numberOfHours, int workdayId, int start, int end) {
        this.workday = workday;
        this.numberOfHours = numberOfHours;
        this.workdayId = workdayId;
        this.start = start;
        this.end = end;
    }

    public Workday getWorkday() {
        return workday;
    }

    public void setWorkday(Workday workday) {
        this.workday = workday;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public int getWorkdayId() {
        return workdayId;
    }

    public void setWorkdayId(int workdayId) {
        this.workdayId = workdayId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
    
     
    public boolean contains(LocationShift shift) {
        if(shift == null){
            return false;
        }
        
        if( this.workday.equals( shift.getWorkday() ) ){
            return ( this.getStart()<= shift.getStart() && this.getEnd() >= shift.getEnd()  );
        }
        return false;
    }
    
    
    
}
