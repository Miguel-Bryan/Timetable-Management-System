/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.google.gson.annotations.Expose;

/**
 *
 * @author Landry Ngani
 */
public class TimePeriod {
    
    @Expose (serialize = false, deserialize = false)
    private Workday workday;
    
    @Expose (serialize = false, deserialize = false)
    private int numberOfHours;
    
    private int workdayId;
    private int start, end;

    
    public TimePeriod() {}
    
    public TimePeriod(Workday workday, int start, int end){
        this.workday = workday;
        this.workdayId = workday.getId();
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Workday getWorkday() {
        return workday;
    }

    public void setWorkday(Workday workday) {
        this.workday = workday;
    }

    public int getWorkdayId() {
        return workdayId;
    }

    public void setWorkdayId(int workdayId) {
        this.workdayId = workdayId;
    }
    
    

    @Override
    public String toString() {
        return "TimePeriod{" + "workday=" + workday + ", start=" + start + ", end=" + end + '}';
    }

    
    boolean contains(LocationShift shift) {
        if(shift == null){
            return false;
        }
        
        if( this.workday.equals( shift.getWorkday() ) ){
            return ( this.getStart()<= shift.getStart() && this.getEnd() >= shift.getEnd()  );
        }
        return false;
    }
    
    
    public int getNumberOfHours(){
        numberOfHours = end-start;
        return numberOfHours;
    }
    
    
    /*
    boolean covers(LocationShift shift) {
        if( this.workday.equals( shift.getWorkday() ) ){
            return ( this.getStart()<= shift.getStart() && this.getEnd() >= shift.getEnd()  );
        }
        return false;
    }
    */
    
}
