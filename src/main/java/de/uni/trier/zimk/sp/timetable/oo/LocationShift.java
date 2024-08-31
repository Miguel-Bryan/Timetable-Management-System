/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author landry.ngani
 *
 */
public class LocationShift implements Serializable {
        
        @Expose (deserialize = false)
        @JsonIgnore
        @XStreamOmitField
	private static final long serialVersionUID = 1L;
        
        @Expose (deserialize = false)
        @JsonIgnore
        @XStreamOmitField
        private static Logger logger = Logger.getLogger( LocationShift.class.getCanonicalName() );
        
        public static final int MIN_TIME = 2;
	
        private Workday workday;
        
        @Expose (deserialize = false)
        @JsonIgnore
	private Location location;
        
        
        private LocationShift previousShift, nextShift;
        
	private int start, end;
	private int capacity;
        @Expose (deserialize = true)       
        private List<Worker> workers;
        @Expose (deserialize = false)
	private List<Worker> fixedWorkers;
        @Expose (deserialize = false)
        private boolean changed;
        
        //@XStreamOmitField
        //private LocationShift previousShift, nextShift;
        //private List<Worker> volunteers;
	
        
        public LocationShift(){}
        
        
        /**
         * 
         * @param workday
         * @param location
         * @param start
         * @param end
         * @param capacity 
         */
        public LocationShift(Workday workday, Location location, int start, int end, int capacity) {
		this.workday = workday;
                this.location = location;
                
		this.start = start;
		this.end = end;
		this.capacity = capacity;
		this.changed = false;
                
		this.workers = new ArrayList<Worker>();
                this.fixedWorkers = new ArrayList<Worker>();
	}
        
        /**
         * 
         * @param shift 
         */
        public LocationShift(LocationShift shift) {
		
		this.workday = shift.getWorkday();
		this.location = shift.getLocation();
                
		this.start = shift.getStart();
		this.end = shift.getEnd();
		this.capacity = shift.getCapacity();
                
                //this.changed = shift.isChanged();

                /*
                this.previousShift = shift.getPreviousShift();
                this.nextShift = shift.getNextShift();
                */
                
		this.workers = new ArrayList<Worker>();
		for(Worker worker : shift.getWorkers() ){
                    workers.add( worker );
		}
                
                this.fixedWorkers = new ArrayList<Worker>();
		for(Worker worker : shift.getFixedWorkers() ){
                    fixedWorkers.add( worker );
		}
		
	}

        
        public boolean isChanged() {
            return changed;
        }

        public void setChanged(boolean changed) {
            this.changed = changed;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

	public Workday getWorkday() {
		return workday;
	}

	public void setWorkday(Workday workday) {
		this.workday = workday;
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

        public List<Worker> getWorkers() {
            if(workers == null ){
                workers = new ArrayList<Worker>();
            }
		return workers;
	}

	public void setWorkers(List<Worker> workers) {
		this.workers = workers;
	}

        public List<Worker> getFixedWorkers() {
            if(fixedWorkers == null ){
                fixedWorkers = new ArrayList<Worker>();
            }
            return fixedWorkers;
        }

        public void setFixedWorkers(List<Worker> fixedWorkers) {
            this.fixedWorkers = fixedWorkers;
        }
        
        /**
         * 
         * @return 
         */
        public List<Worker> getAllWorkers() {
            ArrayList allWorkers = new ArrayList<Worker>();
            allWorkers.addAll( this.getFixedWorkers() );
            allWorkers.addAll( this.getWorkers() );
            return allWorkers;
        }
	
        @JsonIgnore
	public boolean isMaximumWorkersReached(){
		return workers.size() == capacity;
	}
        
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof LocationShift){
                    LocationShift s = (LocationShift)obj;
                    if( this.getWorkday().equals(s.getWorkday()) && this.getLocation().equals(s.getLocation()) ){
                        return (this.getStart() == s.getStart() && this.getEnd() == s.getEnd());
                    }
		}
            return false;
	}
        
        
	public boolean equalsIgnoresLocation(Object obj) {
		if( obj instanceof LocationShift){
                    LocationShift s = (LocationShift)obj;
                    if( this.getWorkday().equals(s.getWorkday()) ){
                        return (this.getStart() == s.getStart() && this.getEnd() == s.getEnd());
                    }
		}
            return false;
	}
	
	public String toStringInTimetable(){
            return "["+ start + "-" + end +"]";
	}
	
	public String toString(){
		if( workday != null ){
                    //return workday.getName()+ " AT " + location.getName() +" [ "+ start + " to " + end +" ] - [CAP. " + capacity+" VS " +workers.size()+ "]";
                    return workday.getName()+ " AT "+" [ "+ start + " to " + end +" ] - [CAP. " + capacity+" VS " +workers.size()+ "]";
                }
		else {
                    return " NO WORKDAY [ "+ start + " to " + end +" ] ";
		}
	}
        
        
        @JsonIgnore
        public boolean isMatchingDayPrefences() {
            for(Worker worker : getWorkers() ){
                if(! worker.isWillingForWorkday(this.getWorkday())){
                    return false;
                }
            }
            return true;
        }

        @JsonIgnore
        public boolean isMatchingShiftPrefences() {
            for(Worker worker : getWorkers() ){
                if(!worker.isWillingForShift(this)){
                    return false;
                }
            }
            return true;
        }
        	

}
