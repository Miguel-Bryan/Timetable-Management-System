/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import de.uni.trier.zimk.sp.timetable.configuration.LocationShiftComparable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Landry Ngani
 */
public class Location {
    
    private int id;
    private String name, description;
    private boolean active;
    @Expose (serialize = true, deserialize = true)
    private List<LocationShift> shifts;

    public Location() {
    }

    
    public Location(int id, String name) {
        this.id = id;
        this.name = name;
        this.active = true;
        
        this.shifts = new ArrayList<LocationShift>(); 
    }

    /**
     * 
     * @param location 
     */
    public Location(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.description = location.getDescription();
        this.active = location.isActive();
        
        this.shifts = new ArrayList<LocationShift>(); 
        for(LocationShift shift : location.getShifts()){
            this.shifts.add( new LocationShift(shift) );
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public List<LocationShift> getShifts() {
        return shifts;
    }

    public void setShifts(List<LocationShift> shifts) {
        this.shifts = shifts;
    }

    @Override
    public String toString() {
        return "Location{" + "id=" + id + ", name=" + name + ", description=" + description + ", shifts=" + shifts + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location wd = (Location) obj;
            return (this.getId() == wd.getId() && this.getName().equalsIgnoreCase(wd.getName()));
        }
        return false;
    }
    
    /**
     * 
     * @param workday
     * @return 
     */
    public List<LocationShift> getShifts(Workday workday) {
        List<LocationShift> dayShifts = new ArrayList<LocationShift>();
        for(LocationShift shift : this.getShifts() ){
            if( shift.getWorkday().equals(workday) ){
               dayShifts.add(shift); 
            }
        }
        
        Collections.sort( dayShifts, new LocationShiftComparable() );
        return dayShifts;
    }
    public LocationShift getShift(Workday workday, int rowIndex) {
        List<LocationShift> dayShifts = getShifts(workday);
        if( dayShifts.size() > rowIndex ){
            return dayShifts.get( rowIndex );
        }
        return null;
    }
    
    public void setShift(int workdayIndex, int shiftIndex, LocationShift shift) {
        this.getShifts().get(shiftIndex);
    }

    public int getMaxShiftsLength(OrganisationalConfiguration configuration) {
        int maxLength = Integer.MIN_VALUE;
        List<Workday> workdays = configuration.getWorkdays();
        for( Workday workday : workdays ){
            int dayNumber = getShifts(workday).size(); 
            maxLength = (dayNumber > maxLength) ? dayNumber : maxLength;
        }
        
        return maxLength;
    }
    
    
    
    /**
     * 
     * @param workday
     * @return 
     */
    public List<Worker> getWorkers(Workday workday) {
        List<Worker> workers = new ArrayList<Worker>();
        List<LocationShift> dayShifts = this.getShifts(workday);
        for(LocationShift shift : dayShifts ){
            for(Worker worker : shift.getWorkers()){
                if(!workers.contains(worker)){
                    workers.add(worker);
                }
            }
        }
        
        Collections.sort( workers, new WorkerComparable() );
        return workers;
    }


    
}
