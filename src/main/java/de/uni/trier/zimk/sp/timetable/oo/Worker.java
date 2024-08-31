/**
 *
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import de.uni.trier.zimk.sp.timetable.util.Ramdomizer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author landry.ngani
 *
 */
public class Worker implements Serializable {
    
    @Expose (deserialize = false)
    @JsonIgnore
    @XStreamOmitField
    private static final long serialVersionUID = 1L;
    
    @Expose (serialize = true, deserialize = true)
    @JsonIgnore
    @XStreamOmitField
    private static final Logger logger = Logger.getLogger(Worker.class.getCanonicalName());
    
    @Expose (serialize = false, deserialize = false)
    private String name;
    
    //@Expose (serialize = false, deserialize = false)
    private String username, password;
    
    @Expose (serialize = false, deserialize = false)
    private int role;
    
    @Expose (serialize = false, deserialize = false)
    private boolean mutable;
    
    @Expose (serialize = false, deserialize = false)
    private int debit;
    
    @Expose (serialize = false, deserialize = false)
    private double ratio;
    
    @Expose (serialize = true, deserialize = true)
    private WorkerColor color;
    
    @Expose (serialize = false, deserialize = true)
    private List<LocationShift> shifts;
    
    @Expose (serialize = false, deserialize = false)
    private List<TimePeriod> preferences;

    
    public Worker(){}
    
    /**
     *
     */
    public Worker(String name, int debit) {
        this.name = name;
        this.debit = debit;

        this.color = new WorkerColor(255,255,255);
        
        this.shifts = new ArrayList<LocationShift>();
        this.preferences = new ArrayList<TimePeriod>();

    }

    /**
     *
     * @param worker
     */
    public Worker(Worker worker) {

        this.name = worker.getName();
        
        this.username = worker.getUsername();
        this.password = worker.getPassword();
        this.role = worker.getRole();
        this.mutable = worker.isMutable();
        this.color = worker.getColor();
        
        this.debit = worker.getDebit();
        this.ratio = worker.getRatio();

        
        this.shifts = new ArrayList<LocationShift>();
        this.preferences = new ArrayList<TimePeriod>();

        for (LocationShift shift : worker.getShifts()) {
            shifts.add(shift);
        }

        for (TimePeriod period : worker.getPreferences()) {
            preferences.add(period);
        }

    }

    /**
     *
     */
    public Worker(String name, int debit, double ratio) {
        this.name = name;
        this.debit = debit;
        this.ratio = ratio;

        this.color = new WorkerColor(255,255,255);

        this.shifts = new ArrayList<LocationShift>();
        this.preferences = new ArrayList<TimePeriod>();

    }

    private String getId() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isMutable() {
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    public WorkerColor getColor() {
        return color;
    }

    public void setColor(WorkerColor color) {
        this.color = color;
    }
    
    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    /**
     * @return the ratio
     */
    public double getRatio() {
        return ratio;
    }

    /**
     * @param ratio the ratio to set
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public List<TimePeriod> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<TimePeriod> preferences) {
        this.preferences = preferences;
    }

    public List<LocationShift> getShifts() {
        return shifts;
    }

    public void setShifts(List<LocationShift> shifts) {
        this.shifts = shifts;
    }

    public String shiftsToString() {
        StringBuffer buffer = new StringBuffer();
        for (LocationShift s : shifts) {
            buffer.append(s.getWorkday().getName() + "#" + s.getStart() + "-" + s.getEnd() + "/");
        }
        return buffer.toString();
    }

    public String toMoreDetailsString() {
        return name + " [" + debit + " D-Hrs]-[" + preferences.size() + " D-prfs] - [" + shifts.size() + " shifts]";
    }

    public String toStringForPrefLog() {
        return name + " [" + debit + " D-Hrs]-[" + preferences.size() + " D-prfs]";
    }

    public String toString() {
        //return name +"[ "+ debit+" D-Hours ]";
        return name + " [" + debit + " D-Hrs]-[" + preferences.size() + " D-prfs]";
    }

    public boolean isWillingForWorkday(Workday workday) {
        for (TimePeriod period : preferences) {
            if (period.getWorkday().equals(workday)) {
                return true;
            }
        }
        return false;
    }

    /*
     * public boolean isWillingForShift(Shift shift) { for(TimePeriod period :
     * preferences){ if( period.covers(shift) ){ return true; } } return false;
     * }
     */
    public boolean isWillingForShift(LocationShift shift) {
        for (TimePeriod period : preferences) {
            if (period.contains(shift)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlannedForWorkday(Workday workday) {
        for (LocationShift shift : shifts) {
            if (shift.getWorkday().equals(workday)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlannedForShift(LocationShift shift) {

        /*
         * for(LocationShift s : shifts){ if( s.equals(shift) ){ return true; }
         * } return false;
         */

        return isPlannedForShiftIgnoresLocation(shift);
    }

    public boolean isPlannedForShiftIgnoresLocation(LocationShift shift) {
        for (LocationShift s : shifts) {
            if (s.equalsIgnoresLocation(shift)) {
                return true;
            }
        }
        return false;
    }

    public void addWorktimes(List<LocationShift> lshifts) {
        for (LocationShift shift : lshifts) {
            shift.getWorkers().add(this);
            shifts.add(shift);
        }
    }

    public void removeWorktimes(List<LocationShift> lshifts) {
        for (LocationShift shift : lshifts) {
            shift.getWorkers().remove(this);
            shifts.remove(shift);
        }
    }

    /**
     *
     * @param shift
     */
    public void addWorktime(LocationShift shift) {

        if (shift == null) {
        }

        if (this == null) {
        }

        shift.getWorkers().add(this);
        shifts.add(shift);
    }

    /**
     *
     * @param shift
     */
    public void removeWorktime(LocationShift shift) {
        shift.getWorkers().remove(this);
        shifts.remove(shift);
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public boolean isMaximumShiftsReached() {
        return shifts.size() == debit;
    }

    @JsonIgnore
    public int getNotAssignedShiftsNumber() {
        return debit - shifts.size();
    }

    /*
     *
     */
    public boolean hasSufficientWorktimes() {
        return shifts.size() >= debit;
    }

    /*
     *
     */
    public boolean hasSufficientPreferences() {
        int total = 0;
        for (TimePeriod period : preferences) {
            total += period.getNumberOfHours();
        }
        return (total / LocationShift.MIN_TIME) >= debit + (preferences.size() * ratio);
    }

    /*
     * (non-Javadoc) @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Worker) {
            Worker worker = (Worker) obj;

            if (this.getId().equalsIgnoreCase(worker.getId())) {
                return true;
            }

            if (this.getName().equalsIgnoreCase(worker.getName()) && this.getDebit() == worker.getDebit()
                    && this.getShifts().size() == worker.getShifts().size() && this.getPreferences().size() == worker.getPreferences().size()) {
                return true;
            }
        }

        return false;
    }

    /*
     * public void addPreferences(LocationShift shift){
     * shift.getVolunteers().add(this); preferences.add(shift); } public void
     * addPreferences(List<Shift> lshifts){ for(Shift shift : lshifts){
     * shift.getVolunteers().add(this); preferences.add(shift); } } public void
     * removePreferences(List<Shift> lshifts){ for(Shift shift : lshifts){
     * shift.getVolunteers().remove(this); preferences.remove(shift); } } public
     * void removePreference(Shift shift){ shift.getVolunteers().remove(this);
     * preferences.remove(shift); }
     */
    /**
     * The list should only contains 1 head and 1 tail...
     *
     * @param worker
     * @return
     */
    @JsonIgnore
    public int getContinuityCost(Workday workday, TimetableState state) {

        List<LocationShift> workerShifts = getWorkerShifts(workday);
        if (workerShifts.size() > 1) {
            int counter = 0;
            List<LocationShift> iWorkerShifts = getWorkerShifts(workday);

            for (LocationShift shift : workerShifts) {
                LocationShift previousShift = state.getPreviousShift(shift);
                //LocationShift previousShift = shift.getPreviousShift();
                if (previousShift == null) {
                    counter++;
                    //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Head of day / increased at "+ shift.toStringInTimetable() );
                } else {
                    if (!iWorkerShifts.contains(previousShift)) {
                        counter++;
                        //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Head increased at "+ shift.toStringInTimetable() );
                    }
                }

                LocationShift nextShift = state.getNextShift(shift);
                //LocationShift nextShift = shift.getNextShift();
                if (nextShift == null) {
                    counter++;
                    //logger.info( "------------------------> XXXXX> YYYYY> Continuity-Cost for "+worker.getName() + " on "+ this.name + " : Tail of day / increased for "+ shift.toStringInTimetable() );
                } else {
                    if (!iWorkerShifts.contains(nextShift)) {
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

    /**
     *
     * @param workday
     * @return
     */
    public List<LocationShift> getWorkerShifts(Workday workday) {
        List<LocationShift> wShifts = new ArrayList<LocationShift>();
        for (LocationShift shift : shifts) {
            if (shift.getWorkday().equals(workday)) {
                wShifts.add(shift);
            }
        }
        return wShifts;
    }

    /**
     *
     */
    @JsonIgnore
    public List<Workday> getPlannedWorkdays() {
        List<Workday> days = new ArrayList<Workday>();
        for (LocationShift s : this.getShifts()) {
            Workday d = s.getWorkday();
            if (!days.contains(d)) {
                days.add(d);
            }
        }
        return days;
    }

    @JsonIgnore
    public int getMaxWorkdays() {
        if (this.getDebit() > 6) {
            return 3;
        } else {
            return 2;
        }
    }

    @JsonIgnore
    public int getCompactnessViolation() {
        return this.getPlannedWorkdays().size() - this.getMaxWorkdays();
    }

    /*
     *
     */
    @JsonIgnore
    public LocationShift isWorkdayViolationFree() {
        for (LocationShift shift : shifts) {
            if (!this.isWillingForWorkday(shift.getWorkday())) {
                return shift;
            }
        }
        return null;
    }

    /*
     *
     */
    @JsonIgnore
    public LocationShift isShiftViolationFree() {
        for (LocationShift shift : shifts) {
            if (!this.isWillingForShift(shift)) {
                return shift;
            }
        }
        return null;
    }
}
