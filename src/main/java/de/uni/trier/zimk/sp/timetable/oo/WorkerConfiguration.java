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
public class WorkerConfiguration {
    @Expose (serialize = true, deserialize = true)
    private List<Workday> workdays;
    @Expose (serialize = true, deserialize = true)
    private List<Worker> workers;
    
    
    public WorkerConfiguration(List<Workday> workdays) {
        this.workers = new ArrayList<Worker>();
        this.workdays = workdays;
    }
    
    public WorkerConfiguration(List<Workday> workdays, List<Worker> workers) {
        this.workers = workers;
        this.workdays = workdays;
    }
    
    /**
     * 
     * @param organisationalConfiguration 
     */
    public WorkerConfiguration(WorkerConfiguration workerConfiguration) {
        
        this.workdays = workerConfiguration.getWorkdays();

        this.workers = new ArrayList<Worker>();
        for(Worker worker : workerConfiguration.getWorkers()){
            this.workers.add(new Worker(worker));
        }
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
        return "WorkerlConfiguration{" + "workers=" + workers + '}';
    }
   
    

}
