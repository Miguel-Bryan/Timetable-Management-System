/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.preferences;

import de.uni.trier.zimk.sp.timetable.oo.Worker;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.util.List;

/**
 *
 * @author Landry Ngani
 */
public class UsersAndPreferencesModel {

    private Timetable timetable;
    private List<Worker> workers;
    
    public UsersAndPreferencesModel(Timetable timetable) {
        this.timetable = timetable;
    }

    public List<Worker> getWorkers() {
        if(workers == null ){
            workers = timetable.getTimetableState().getWorkers();
        }
       return workers;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
    
    
    
    
    
}
