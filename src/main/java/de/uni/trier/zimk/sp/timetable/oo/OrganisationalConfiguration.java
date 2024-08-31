/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Landry Ngani
 */
public class OrganisationalConfiguration {
    
    private long validityStart, validityEnd;
    
    @Expose (serialize = true, deserialize = true)
    private List<Workday> workdays;
    
    @Expose (serialize = false, deserialize = false)
    private List<Location> locations;
    
    @Expose (serialize = true, deserialize = true)
    private List<Worker> workers;

    
    public OrganisationalConfiguration() {
        this.workdays = new ArrayList<Workday>();
        this.locations = new ArrayList<Location>();
        this.workers = new ArrayList<Worker>();
    }
    
    public OrganisationalConfiguration(List<Workday> workdays, List<Location> locations, List<Worker> workers) {
        this.workdays = workdays;
        this.locations = locations;
        this.workers = workers;
    }
    
    /**
     * 
     * @param organisationalConfiguration 
     */
    public OrganisationalConfiguration(OrganisationalConfiguration organisationalConfiguration) {
        
        this.validityStart = organisationalConfiguration.getValidityStart();
        this.validityEnd = organisationalConfiguration.getValidityEnd();
        
        this.workdays = organisationalConfiguration.getWorkdays();
        
        this.locations = new ArrayList<Location>();
        for(Location location : organisationalConfiguration.getLocations()){
            this.locations.add(new Location(location));
        }
        
        this.workers = new ArrayList<Worker>();
        for(Worker worker : organisationalConfiguration.getWorkers()){
            this.workers.add(new Worker(worker));
        }
    }

    public List<Location> getLocations() {
        List<Location> aLocations = new ArrayList<Location>();
        for(Location location : locations){
            if(location.isActive()){
                aLocations.add(location);
            }
        }
        return aLocations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Workday> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(List<Workday> workdays) {
        this.workdays = workdays;
    }
    
    public List<Worker> getWorkers() {
        return workers;
    }

    public long getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(long validityEnd) {
        this.validityEnd = validityEnd;
    }

    public long getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(long validityStart) {
        this.validityStart = validityStart;
    }
    
    public List<Worker> getMutableWorkers() {
        List<Worker> mutableWorkers = new ArrayList<Worker>();
        for(Worker worker : workers){
            if(worker.isMutable()){
                mutableWorkers.add(worker);
            }
        }
        return mutableWorkers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
    
    @Override
    public String toString() {
        return "OrganisationalConfiguration{" + "workdays=" + workdays + ", locations=" + locations + '}';
    }

    
}
