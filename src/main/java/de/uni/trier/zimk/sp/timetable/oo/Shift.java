/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author landry.ngani
 *
 */
public class Shift implements Serializable {

    
        /*
        @XStreamOmitField
	private static final long serialVersionUID = 1L;
        @XStreamOmitField
        private static Logger logger = Logger.getLogger( Shift.class.getCanonicalName() );
	
        //@XStreamOmitField
	private Workday workday;
        
        //@XStreamOmitField
        private Shift previousShift, nextShift;
        
	private int start, end;
	private int capacity;
        private boolean changed;
        
        //@XStreamOmitField
	private List<Worker> workers;
        private List<Worker> volunteers;
	
	
	public Shift(Workday workday, int start, int end, int capacity) {
		this.workday = workday;
		this.start = start;
		this.end = end;
		
		this.capacity = capacity;
		
		this.workers = new ArrayList<Worker>();
		this.volunteers = new ArrayList<Worker>();
	}

	/*
	public Shift(Shift shift) {
		this.workday = new Workday( shift.getWorkday() );
		this.start = shift.getStart();
		this.end = shift.getEnd();
		this.capacity = shift.getCapacity();

		this.workers = new ArrayList<Worker>();
		for(Worker worker : shift.getWorkers() ){
			workers.add( new Worker(worker) );
		}
		
		this.volunteers = new ArrayList<Worker>();
		for(Worker worker : shift.getVolunteers() ){
			volunteers.add( new Worker(worker) );
		}
	}
	* /
	
	
	public Shift(Shift shift, boolean clone) {
		
		this.workday = (clone ? shift.getWorkday() : new Workday( shift.getWorkday()) );
		
		this.start = shift.getStart();
		this.end = shift.getEnd();
		this.capacity = shift.getCapacity();

		this.workers = new ArrayList<Worker>();
		for(Worker worker : shift.getWorkers() ){
			workers.add( (clone?worker : new Worker(worker)) );
//			workers.add( new Worker(worker, clone) );
		}
		
		this.volunteers = new ArrayList<Worker>();
		for(Worker worker : shift.getVolunteers() ){
			volunteers.add( (clone?worker : new Worker(worker)) );
//			volunteers.add( new Worker(worker, clone) );
		}
	}

        
        public boolean isChanged() {
            return changed;
        }

        public void setChanged(boolean changed) {
            this.changed = changed;
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

        public Shift getNextShift() {
            return nextShift;
        }

        public void setNextShift(Shift nextShift) {
            this.nextShift = nextShift;
        }

        public Shift getPreviousShift() {
            return previousShift;
        }

        public void setPreviousShift(Shift previousShift) {
            this.previousShift = previousShift;
        }

        

	public void increaseCapacity() {
		capacity ++;
	}
	public void decreaseCapacity() {
		capacity --;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof Shift){
			Shift s = (Shift)obj;
			if( this.getWorkday().equals(s.getWorkday()) ){
				return (this.getStart() == s.getStart() && this.getEnd() == s.getEnd());
			}
		}
		return false;
	}
	
	public String toStringInTimetable(){
		if( workday != null ){
			return "["+ start + "-" + end +"]";
		}
		else {
			return "NO WD ["+ start + "-" + end +"]";
		}
	}
	
	public String toString(){
		if( workday != null ){
			return workday.getName() 
                                + " [ "+ start + " to " + end +" ] - [CAP. " + capacity+" VS " +workers.size()+ "]";
		}
		else {
			return " NO WORKDAY [ "+ start + " to " + end +" ] ";
		}
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

	public List<Worker> getVolunteers() {
            if(volunteers == null ){
                volunteers = new ArrayList<Worker>();
            }
            /*
            if(volunteers == null || volunteers.isEmpty() ){
                volunteers = new ArrayList<Worker>();
                for(Worker worker : getWorkers() ){
                    if(worker.isWillingForShift(this)){
                        volunteers.add(worker);
                    }
                }
            }
            * /
                    
            return volunteers;
	}

	public void setVolunteers(List<Worker> volunteers) {
		this.volunteers = volunteers;
	}

	public boolean hasMoreThanNeeded(){
		return volunteers.size() > capacity;
	}
	
	public boolean isMaximumWorkersReached(){
		return workers.size() == capacity;
	}
	
	public List<Shift> getExhaustiveIncompletesShifts() {
		int diff = capacity - workers.size();
		List<Shift> ishifts = new ArrayList<Shift>();
		while( diff > 0 ){
			ishifts.add( this );
			diff--;
			
			logger.info( "\nQQQQQQQQQQQQQQQQQQQQQQQ Came for the value"+ toString() +" =======> "+ diff );
		}
		
		return ishifts;
	}

        
        private static int [] CAPACITIES = { 2, 4, 4, 4, 4 };
        private static int [] CAPACITIES_ = { 3, 6, 6, 6, 6 };
        
	/*
	 * Create a simple List of Shifts ***
	 * /
	public static List<Shift> getAll() {
		List<Shift> shifts = new ArrayList<Shift>();
		
                int min = 8;
		int max = 18;
		int step = 2;
		
                Workday nullDay = null;
                Shift previous = null;
		Shift next = null;
                for( int i = min; i < max; i=i+step ){
			Shift shift = new Shift( nullDay, i , i+step, CAPACITIES[ (i-min)/step ] );
                        
                        if(previous != null ){
                            previous.setNextShift(shift);
                            shift.setPreviousShift(previous);
                        }
                        previous = shift;
			
                        shifts.add(shift);
		}
		return shifts;
	}

    public boolean isMatchingDayPrefences() {
        for(Worker worker : getWorkers() ){
            if(! worker.isWillingForWorkday(this.getWorkday())){
                return false;
            }
	}
        return true;
    }
    
    public boolean isMatchingShiftPrefences() {
        for(Worker worker : getWorkers() ){
            if(!worker.isWillingForShift(this)){
                return false;
            }
	}
        return true;
    }
    
    public boolean isExceedingShiftCapacities() {
        return (this.getCapacity() < this.getVolunteers().size());
    }
    
    public boolean isMatchingShiftCapacities() {
        return (this.getCapacity()==this.getVolunteers().size());
    }
    
    public boolean isLessThanShiftCapacities() {
        return (this.getCapacity()>this.getVolunteers().size());
    }

    */

}
