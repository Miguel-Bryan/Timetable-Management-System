/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Landry Ngani
 */
public class UserShiftBlock {

    private List<LocationShift> shifts;
    private Worker worker;
    private Workday workday;

    private boolean matchingPreferences;
    

    public UserShiftBlock(Worker worker, Workday workday) {
        this.worker = worker;
        this.workday = workday;
        
        shifts = new ArrayList<LocationShift>();
    }

    public boolean isLastModified() {
        for( LocationShift shift : shifts ){
            if( shift.isChanged() ){
                return true;
            }
        }
        return false;
    }

    public boolean isMatchingPreferences() {
        return matchingPreferences;
    }

    public void setMatchingPreferences(boolean matchingPreferences) {
        this.matchingPreferences = matchingPreferences;
    }

    public List<LocationShift> getShifts() {
        return shifts;
    }

    public void setShifts(List<LocationShift> shifts) {
        this.shifts = shifts;
    }

    public Workday getWorkday() {
        return workday;
    }

    public void setWorkday(Workday workday) {
        this.workday = workday;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * 
     * @return 
     */
    public boolean unMatchingDayPrefences(){
        return !worker.isWillingForWorkday(workday);
    }
    
     /**
     * 
     * @return 
     */
    public boolean unMatchingShiftPrefences(){
        for( LocationShift shift : shifts ){
            if( ! worker.isWillingForShift(shift) ){
                return true;
            }
        }
        return false;
    }
    
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for( LocationShift shift : shifts ){
            if( shift.isChanged() ){
                buffer.append("/").append( "**"+shift.toStringInTimetable()+"**");
            }
            else {
                buffer.append("/").append( shift.toStringInTimetable());
            }
        }
        return buffer.toString();
    }
    
    
    
    
}
