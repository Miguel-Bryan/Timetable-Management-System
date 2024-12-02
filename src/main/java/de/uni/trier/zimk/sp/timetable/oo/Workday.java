/**
 *
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author landry.ngani
 *
 */
public class Workday implements Serializable {
    
    @Expose (deserialize = false)
    @XStreamOmitField
    private static final long serialVersionUID = 1L;
    
    @Expose (deserialize = false)
    @XStreamOmitField
    private static Logger logger = Logger.getLogger(Workday.class);
    
    @Expose (serialize = true, deserialize = true)
    private int id;
    @Expose (serialize = true, deserialize = true)
    private String name;
    
    @Expose (serialize = true, deserialize = true)
    @XStreamOmitField
    private int numberOfShifts;
    
    
    public Workday() { }
            
    /**
     * 
     * @param name
     * @param locations 
     */
    public Workday(int id, String name, int numberOfShifts) {
        this.id = id;
        this.name = name;
       this.numberOfShifts = numberOfShifts;
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
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Workday) {
            Workday wd = (Workday) obj;
            return (this.getId() == wd.getId() && this.getName().equalsIgnoreCase(wd.getName()));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Workday{" + "id=" + id + ", name=" + name + ", numberOfShifts=" + numberOfShifts + '}';
    }

    
    
//    
//    public String toString() {
//        return " ID : "+ id + " [ " + name + " ]";
//    }
    
    /**
     *
     */
    public Workday(String name, int numberOfShifts) {
        this.name = name;
        this.numberOfShifts = numberOfShifts;

        /*
        this.shifts = new ArrayList<LocationShift>();
        this.locations = new ArrayList<Location>();
        */
        
        /*
        LocationShift previous = null, next = null;
        for (int i = 0; i < numberOfShifts; i++) {            
            LocationShift shift = new LocationShift(this, Shift.getAll().get(i).getStart(), Shift.getAll().get(i).getEnd(), Shift.getAll().get(i).getCapacity());
            
            if(previous != null ){
                previous.setNextShift(shift);
                shift.setPreviousShift(previous);
            }
            previous = shift;
                        
            shifts.add(shift);
        }
        */
                
    }

    /**
     *
     * @param day
     */
    public Workday(Workday day) {
        this(day.getName(), day.getNumberOfShifts());
    }
    public int getNumberOfShifts() {
        return numberOfShifts;
    }

    public void setNumberOfShifts(int numberOfShifts) {
        this.numberOfShifts = numberOfShifts;
    }

    
        

    /*
    @XStreamOmitField
    private List<Location> locations;
    @XStreamOmitField
    private List<LocationShift> shifts;
    */
    
    /*
    public String toMoreDetailsString() {
        return name + " [" + numberOfShifts + " Sfs]";
    }
    */
    
    /*
    public List<LocationShift> getShifts() {
        shifts = new ArrayList<LocationShift>();
          
            for(Location location: getLocations()){
                List<LocationShift> ishifts = location.getShifts(this);
                shifts.addAll( ishifts );
            }
        return shifts;
    }

    public void setShifts(List<LocationShift> shifts) {
        this.shifts = shifts;
    }

    public List<Location> getLocations() {
        if(locations == null){
            locations = new ArrayList<Location>();
        }
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public double getWorkdayHours() {
        double sum = 0;
        for (LocationShift shift : shifts) {
            sum += shift.getCapacity();
        }
        return sum;
    }
    */
    
    /*
    private static String[] DAY_NAMES = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private static int[] DAY_SHIFTS = {5, 5, 5, 5, 4, 0, 0};

    public static List<Workday> getAll() {
        List<Workday> workdays = new ArrayList<Workday>();
        int weekLength = 5;
        for (int i = 0; i < weekLength; i++) {
            Workday wd = new Workday(DAY_NAMES[i], DAY_SHIFTS[i]);
            workdays.add(wd);
        }
        return workdays;
    }

    public boolean isMaximumWorkersReached() {
        for (LocationShift shift : shifts) {
            if (!shift.isMaximumWorkersReached()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return the List of shifts that aren't completely taken.
     * /
    public List<LocationShift> getChargeableShifts() {
        List<LocationShift> chargeables_shifts = new ArrayList<LocationShift>();
        for (LocationShift shift : shifts) {
            if (!shift.isMaximumWorkersReached()) {
                chargeables_shifts.add(shift);
            }
        }
        return chargeables_shifts;
    }
    */

    /*
    public List<LocationShift> getExhaustiveIncompletesShifts() {
        List<LocationShift> ishifts = new ArrayList<LocationShift>();
        for (LocationShift shift : shifts) {
            if (!shift.isMaximumWorkersReached()) {
                ishifts.addAll(shift.getExhaustiveIncompletesShifts());
            }
        }

        return ishifts;
    }
    */
    
    /**
     *
     * @return
     * /
    public LocationShift isFullyInitialized() {
        for (LocationShift shift : getShifts()) {
            if (!shift.isMaximumWorkersReached()) {
                return shift;
            }
        }
        return null;
    }

    public LocationShift getFirstShift() {
        for (LocationShift shift : getShifts()) {
            if (shift.getPreviousShift() == null) {
                return shift;
            }
        }
        return null;
    }

    public LocationShift getLastShift() {
        for (LocationShift shift : getShifts()) {
            if (shift.getNextShift() == null) {
                return shift;
            }
        }
        return null;
    }

    public List<Worker> getWorkers() {
        List<Worker> workers = new ArrayList<Worker>();
        for (LocationShift shift : getShifts()) {
            for (Worker worker : shift.getWorkers()) {
                if (! workers.contains(worker)) {
                    workers.add(worker);
                }
            }
        }
        return workers;
    }

    /**
     *
     * @param worker
     * @return
     * /
    public List<LocationShift> getWorkerShifts(Worker worker) {
        List<LocationShift> shifts = new ArrayList<LocationShift>();
        if (!worker.isPlannedForWorkday(this)) {
            return shifts;
        }

        for (LocationShift shift : worker.getShifts()) {
            if (shift.getWorkday().equals(this)) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    /**
     *
     * @return
     * /
    public int getContinuityCost() {
        int cost = 0;

        List<Worker> workers = getWorkers();
        for (Worker worker : workers) {
            int workerCost = this.getContinuityCost(worker);
            cost += workerCost;
            
            //logger.info( "------------------------> XXXXX> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : "+ workerCost);
        }
        return cost;
    }

    /**
     * The list should only contains 1 head and 1 tail...
     * 
     * @param worker
     * @return 
     * /
    private int getContinuityCost(Worker worker) {

        List<LocationShift> workerShifts = getWorkerShifts(worker);
        if (workerShifts.size() > 1) {
            int counter = 0;
            List<LocationShift> iWorkerShifts = getWorkerShifts(worker);
            
            for (LocationShift shift : workerShifts) {
                LocationShift previousShift = shift.getPreviousShift();
                if (previousShift == null){
                    counter++;
                    //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Head of day / increased at "+ shift.toStringInTimetable() );
                }
                else {
                    if( !iWorkerShifts.contains(previousShift) ) {
                        counter++;
                        //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Head increased at "+ shift.toStringInTimetable() );
                    }
                }
                
                LocationShift nextShift = shift.getNextShift();
                if (nextShift == null ){
                    counter++;
                    //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Tail of day / increased for "+ shift.toStringInTimetable() );
                }
                else {
                    if( !iWorkerShifts.contains(nextShift)) {
                        counter++;
                        //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Tail increased at "+ shift.toStringInTimetable() );
                    }
                }
                    
                if (counter > 2) {
                    return TimetableState.CONTINUITY_COST;
                }
            }
        }
        
        return 0;
    }
    */
}
