/**
 *
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author bryan
 *
 */
public class TimetableState {

    @XStreamOmitField
    private static Logger logger = Logger.getLogger(TimetableState.class);
    //@XStreamOmitField
    
    /*
    private List<Workday> workdays;
    private List<Worker> workers;
    */
    
    @XStreamOmitField
    private LocationShift prePermuted, postPermuted;
    @XStreamOmitField
    private OrganisationalConfiguration organisationalConfiguration;
    
    //@XStreamOmitField
    //private WorkerConfiguration workerConfiguration;
    
    //@XStreamOmitField
    public final static int DAY_COST = 5;
    public final static int SHIFT_COST = 3;
    public final static int CONTINUITY_COST = 1;
    public final static int LOCATION_CONSISTENCY_COST = 2;
    public final static int SHIFT_COMPACTNESS_COST = 2;
    
    
    @XStreamOmitField
    private String[][] shiftsValues;

    /**
     *
     */
    public TimetableState(OrganisationalConfiguration organisationalConfiguration, List<Workday> workdays, List<Worker> workers) {
        //this.organisationalConfiguration = organisationalConfiguration;
        this.organisationalConfiguration = new OrganisationalConfiguration(organisationalConfiguration);
        //this.workerConfiguration = workerConfiguration;
    }

    /**
     * 
     * @param state 
     */
    public TimetableState(TimetableState state) {
        this.organisationalConfiguration = new OrganisationalConfiguration(state.getOrganisationalConfiguration());
        //this.workerConfiguration = new WorkerConfiguration( state.getWorkerConfiguration() );
    }

    public void duplicateWorkersProperties(TimetableState state) {

        for (Worker worker : state.getWorkers()) {

            List<LocationShift> lShifts = worker.getShifts();
            for (LocationShift shift : lShifts) {
                LocationShift stateShift = getShiftInCurrentState(shift);
                Worker stateWorker = getWorkerInCurrentState(worker);

                stateWorker.addWorktime(stateShift);
            }


            // TODO
//                        
//              for(TimePeriod shift : worker.getPreferences() ){ 
//                  Shift stateShift = getShiftInCurrentState( shift ); Worker stateWorker
//              = getWorkerInCurrentState( worker );
//             
//              stateWorker.addPreferences( shift ); }
//             
        }
    }

    private LocationShift getShiftInCurrentState(LocationShift shift) {
        for (Workday day : organisationalConfiguration.getWorkdays()) {
            if (day.equals(shift.getWorkday())) {
                
                for(Location location : getLocations() ){
                    if (location.equals(shift.getLocation()) ) {
                        
                        for (LocationShift s : location.getShifts(day)) {
                            if (s.equals(shift)) {
                                return s;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Worker getWorkerInCurrentState(Worker worker) {
        for (Worker w : getWorkers() ) {
            if (w.equals(worker)) {
                return w;
            }
        }
        return null;
    }

    public OrganisationalConfiguration getOrganisationalConfiguration() {
        return organisationalConfiguration;
    }

    public void setOrganisationalConfiguration(OrganisationalConfiguration organisationalConfiguration) {
        this.organisationalConfiguration = organisationalConfiguration;
    }

    /*
    public WorkerConfiguration getWorkerConfiguration() {
        return workerConfiguration;
    }

    public void setWorkerConfiguration(WorkerConfiguration workerConfiguration) {
        this.workerConfiguration = workerConfiguration;
    }
    */

    /**
     * @return the workdays
     */
    public List<Workday> getWorkdays() {
        //return workdays;
        return organisationalConfiguration.getWorkdays();
    }

    /**
     * @return the workers
     */
    public List<Worker> getWorkers() {
        //return workers;
        return organisationalConfiguration.getWorkers();
    }
    
     /**
     * @return the workers
     */
    public List<Worker> getMutableWorkers() {
        return organisationalConfiguration.getMutableWorkers();
    }


    /**
     * 
     * @return 
     */
    public List<Location> getLocations() {
        return organisationalConfiguration.getLocations();
    }
    
    /**
     * @param workers the workers to set
     * /
    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
    */

    /**
     *
     * @param worker
     * @return
     */
    public Worker getWorker(Worker worker) {
        for (Worker w : getWorkers()) {
            if (w.equals(worker)) {
                return w;
            }
        }
        return null;
    }

    public LocationShift getPostPermuted() {
        return postPermuted;
    }

    public LocationShift getPrePermuted() {
        return prePermuted;
    }

    public void setPostPermuted(LocationShift postPermuted) {
        this.postPermuted = postPermuted;
        postPermuted.setChanged(true);
    }

    public void setPrePermuted(LocationShift prePermuted) {
        this.prePermuted = prePermuted;
        prePermuted.setChanged(true);
    }

    public void performSwitch(SwitchHelper helper, LocationShift iShift) throws DuplicateBookingException {

        Worker rWorker = this.getWorkerInCurrentState(helper.getRandomWorker());
        Worker tWorker = this.getWorkerInCurrentState(helper.getTargetWorker());

        LocationShift randomShift = this.getShiftInCurrentState(helper.getShift());
        LocationShift targetShift = this.getShiftInCurrentState(iShift);

        if (rWorker == null) {
            throw new IllegalStateException("Permutation isn't possible with no RandomWorker defined : null");
        }

        if (tWorker == null) {
            throw new IllegalStateException("Permutation isn't possible with no TargetWorker defined : null");
        }

        if ((!rWorker.isPlannedForShift(targetShift)) && (!tWorker.isPlannedForShift(randomShift))) {
            rWorker.addWorktime(targetShift);
            rWorker.removeWorktime(randomShift);

            tWorker.addWorktime(randomShift);
            tWorker.removeWorktime(targetShift);

            // Save operation
            this.setPrePermuted(randomShift);
            this.setPostPermuted(targetShift);
        } else {
            //throw new DuplicateBookingException("RandomWorker " + rWorker.getName() + " already booked for Shift : " + targetShift.toString()
            //        + "\n ===========> OR TargetWorker " + tWorker.getName() + " already booked for Shift : " + randomShift.toString());
        }
    }

    /**
     * @return
     * /
    public String[][] getRowData(boolean reset) {

        if (reset) {

            shiftsValues = new String[workers.size() + 1][workdays.size() + 1];
            for (int i = 0; i < workers.size() + 1; i++) {
                for (int j = 0; j < workdays.size() + 1; j++) {

                    if (shiftsValues[i][j] == null) {
                        shiftsValues[i][j] = new String();
                    }

                    // Workdays names columns ***
                    if (i == 0 && j != 0) {
                        shiftsValues[i][j] += workdays.get(j - 1).getName();
                    } else {

                        // Workers names columns ***
                        if (j == 0 && i != 0) {
                            shiftsValues[i][j] = workers.get(i - 1).toStringForPrefLog();
                        } else {
                            // All other boxes ***
                            if (i != 0 && j != 0) {
                                shiftsValues[i][j] += "";
                            } // First box ***
                            else {
                                shiftsValues[i][j] = "Wkers/Wds";
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < workers.size(); i++) {
                for (int j = 0; j < workdays.size(); j++) {
                    Worker worker = workers.get(i);
                    Workday workday = workdays.get(j);

                    if (worker.isPlannedForWorkday(workday)) {

                        for (int k = 0; k < workday.getShifts().size(); k++) {
                            LocationShift shift = workday.getShifts().get(k);

                            if (worker.isPlannedForShift(shift)) {
                                shiftsValues[i + 1][j + 1] += " / "
                                        + shift.toStringInTimetable();
                            }
                        }
                    }
                }
            }
        }


        return shiftsValues;
    }    
    
    public boolean dayInPreference(boolean selected, int row, int column) {

        if (selected) {
            String[][] rowData = getRowData(true);
            Worker worker = getWorkerFromRow(rowData[row][0]);
            Workday day = getWorkdayFromRow(rowData[0][column]);


            if (worker != null && day != null) {
                System.out.println("ROW VALUES ==  " + worker.getName() + " Day == " + day.getName());

                return worker.isWillingForWorkday(day);
            }
        }

        return false;
    }

    public boolean shiftInPreference(boolean selected, int row, int column) {

        if (selected) {
            String[][] rowData = getRowData(true);
            Worker worker = getWorkerFromRow(rowData[row][0]);
            Workday day = getWorkdayFromRow(rowData[0][column]);


            if (worker != null && day != null) {

                if (!rowData[row][column].trim().equals("")) {
                    LocationShift shift = getShiftFromRow(day, rowData[row][column]);

                    if (shift != null) {
                        System.out.println("ROW VALUES ==  " + worker.getName() + " Day == " + day.getName() + " Shift = " + shift.toStringInTimetable());
                        return worker.isWillingForShift(shift);
                    }
                }

            }
        }

        return false;
    }
    * /

    private Worker getWorkerFromRow(String string) {
        for (Worker w : getWorkers()) {
            if (w.toStringForPrefLog().trim().equals(string.trim())) {
                return w;
            }
        }
        return null;
    }

    private Workday getWorkdayFromRow(String string) {
        for (Workday w : getWorkdays()) {
            if (w.getName().trim().equals(string.trim())) {
                return w;
            }
        }
        return null;
    }

    private LocationShift getShiftFromRow(Workday day, String string) {
        
        for (LocationShift w : day.getShifts()) {
            if (string.contains(w.toStringInTimetable().trim())) {
                return w;
            }
        }
        return null;
    }

    /**
     *
     * @return
     * /
    public Workday isFullyInitialized() {
        for (Workday workday : getWorkdays()) {
            if (!workday.isMaximumWorkersReached()) {
                return workday;
            }
        }
        return null;
    }
    */

    public Worker getSuitableWorkerForShift(LocationShift shift) {
        logger.info("\n***********************");
        int summe = 0;
        for (Worker w : getMutableWorkers()) {
            if (!w.isMaximumShiftsReached()) {
                if (!w.isPlannedForShift(shift)) {
                    return w;
                }
            }

            summe += w.getDebit();
            logger.info(this.getClass().getSimpleName() + " --> Worker " + w.getName() + " : checked --> " + summe);
        }
        logger.info("***********************\n");

        return null;
    }

    /**
     * 
     * @param workday
     * @return 
     * /
    public Worker getSuitableWorkerForDay(Workday workday) {
        logger.info("\n***********************");
        int summe = 0;
        for (Worker w : getWorkers()) {
            if (!w.isMaximumShiftsReached()) {

                for (LocationShift shift : workday.getShifts()) {
                    if (!w.isPlannedForShift(shift)) {
                        return w;
                    }
                }
            }

            summe += w.getDebit();
            logger.info(this.getClass().getSimpleName() + " --> Worker " + w.getName() + " : checked --> " + summe);
        }
        logger.info("***********************\n");

        return null;
    }
    */

    /**
     * 
     * @param worker
     * @param switchShift
     * @return 
     */
    public SwitchShift getSwitchableShift(Worker worker, SwitchShift switchShift) {
        
        for ( Workday workday : getWorkdays() ) {
            
            if (!switchShift.getShift().getWorkday().equals(workday)) {

                for (Location location : getLocations()) {

                    List<LocationShift> shifts = location.getShifts(workday);
                    for (LocationShift shift : shifts ) {
                        
                        if (!worker.isPlannedForShift(shift)) {
                            if (!shift.getWorkers().isEmpty()) {

                                //Not in the exclusion list.
                                if (!switchShift.getToExclude().contains(shift)) {
                                    switchShift.setShiftTarget(shift);

                                    return switchShift;
                                }
                            }
                        }
                    }
                }
                                
            }
        }
        
        /*
        for (Workday workday : getWorkdays()) {
            if (!switchShift.getShift().getWorkday().equals(workday)) {
                for (LocationShift shift : workday.getShifts()) {
                    if (!worker.isPlannedForShift(shift)) {
                        if (!shift.getWorkers().isEmpty()) {

                            //Not in the exclusion list.
                            if (!switchShift.getToExclude().contains(shift)) {
                                switchShift.setShiftTarget(shift);

                                return switchShift;
                            }
                        }
                    }
                }
            }
        }
        */
                
        return null;
    }

    /**
     * 
     * @param worker
     * @return 
     */
    public LocationShift getSuitableShift(Worker worker) {
        
        for ( Workday workday : getWorkdays() ) {
            for (Location location : getLocations()) {
                
                List<LocationShift> shifts = location.getShifts(workday);
                for (LocationShift shift : shifts ) {
                    if (!shift.isMaximumWorkersReached()) {

                            logger.info(this.getClass().getSimpleName() + " --> LocationShift  "
                            + shift.toString() + " choosen for "+ worker.getName()+ " on "+workday.getName() );
                        
                            return shift;

                    }
                }
                
            }
        }
        return null;
    }

    /**
     * 
     * @param worker
     * @return 
     */
    public LocationShift getStrictSuitableShift(Worker worker) {
        
        for ( Workday workday : getWorkdays() ) {
            for (Location location : getLocations()) {
                List<LocationShift> shifts = location.getShifts(workday);
                
                for (LocationShift shift : shifts ) {
                    if (!shift.isMaximumWorkersReached()) {
                        
                        if( ! worker.isPlannedForShiftIgnoresLocation(shift) ){
                            
                            logger.info( "#STRICT : "+ this.getClass().getSimpleName() + " --> LocationShift  "
                            + shift.toString() + " choosen for "+ worker.getName()+ " on "+workday.getName() );
                        
                            return shift;
                        }
                        
                    }
                }
            }
        }
        return null;
    }

    public LocationShift XXX_getSuitableShift(Workday workday, Worker worker) {
        for (Workday iWorkday : getWorkdays()) {
            List<Location> locations = getLocations();
            for (Location location : locations) {
                for (LocationShift shift : location.getShifts(iWorkday)) {
                    if (!shift.isMaximumWorkersReached()) {
                        if (!worker.isPlannedForShift(shift)) {
                            return shift;
                        }
                    }
                }
            }
        }
        return null;
    }

    public UserShiftBlock getWorktimeInformation(int rowIndex, int columnIndex) {
        Worker worker = getWorkers().get(rowIndex);
        Workday workday = getWorkdays().get(columnIndex - 1);

        UserShiftBlock block = new UserShiftBlock(worker, workday);
        List<LocationShift> shifts = worker.getShifts();
        for (LocationShift s : shifts) {
            if (s.getWorkday().equals(workday)) {
                block.getShifts().add(s);
            }
        }

        return block;
    }

    
    
    public LocationShift getShiftInformation(Location location, int rowIndex, int columnIndex) {

        Workday workday = getWorkdays().get(columnIndex);
        List<LocationShift> shifts = location.getShifts(workday);
        if (shifts.size() > rowIndex) {
            return shifts.get(rowIndex);
        }
        return null;
    }    public void setShiftInformation(Location location, LocationShift shift, int rowIndex, int columnIndex) {
        Workday workday = getWorkdays().get(columnIndex);
        List<LocationShift> shifts = location.getShifts(workday);
        if (shifts.size() > rowIndex) {
            shifts.set(rowIndex, shift);
        }
    }
    
    
    /*
    public int getShiftRowLength() {
        int max = 0;
        for (Workday w : getWorkdays()) {
            if (w.getShifts().size() > max) {
                max = w.getShifts().size();
            }
        }
        return max;
    }
    * 
    */
    

    /**
     *
     * @return cost, the number of days compliance violation
     */
    public int getDayComplianceCost() {
        int cost = 0;
        for (int d = 0; d < getWorkdays().size(); d++) {
            Workday workday = getWorkdays().get(d);
            
            List<Location> locations = getLocations();
            for(Location location : locations){
                
                List<LocationShift> shiftsListOfDay = location.getShifts( workday );
                for (int s = 0; s < shiftsListOfDay.size(); s++) {
                    LocationShift shift = shiftsListOfDay.get(s);
                    List<Worker> shiftWorkers = shift.getWorkers();
                    for (int w = 0; w < shiftWorkers.size(); w++) {
                        Worker worker = shiftWorkers.get(w);

                        if (!worker.isWillingForWorkday(workday)) {
                            cost += DAY_COST;
                        }
                    }
                }
            }
        }
        return cost;
    }

    /**
     * @return cost , the number of shifts compliance violation
     */
    public int getShiftComplianceCost() {
        int cost = 0;
        for (int d = 0; d < getWorkdays().size(); d++) {
            Workday workday = getWorkdays().get(d);

            List<Location> locations = getLocations();
            for(Location location : locations){
                List<LocationShift> shiftsListOfDay = location.getShifts( workday );
                for (int s = 0; s < shiftsListOfDay.size(); s++) {
                    LocationShift shift = shiftsListOfDay.get(s);
                    List<Worker> shiftWorkers = shift.getWorkers();
                    for (int w = 0; w < shiftWorkers.size(); w++) {
                        Worker worker = shiftWorkers.get(w);

                        if (!worker.isWillingForShift(shift)) {
                            cost += SHIFT_COST;
                        }
                    }
                }   
            }
        }
        return cost;
    }

    /**
     *
     * @param worker
     * @param shift
     * @return
     */
    public int getContinuityCost() {
        int cost = 0;
        for (Workday workday : getWorkdays()) {
            List<Worker> workers = getMutableWorkers();
            for(Worker worker : workers){
                int dayCost = worker.getContinuityCost(workday, this);
                cost += dayCost;
                
                /*
                if(dayCost>0){
                    logger.info( " ### CONTINUITY_VIOLATION : "+ this.getClass().getSimpleName() + " --> Worker  "
                            + worker.getName()+ " on "+workday.getName() );
                }
                */
            }
        }
        return cost;
    }
    
    /**
     *
     * @param worker
     * @param shift
     * @return
     */
    public int getLocationConsistencyCost() {
        int cost = 0;
        for (Workday workday : getWorkdays()) {
            
            int dayCost = 0;
            List<Worker> locationWorkers = new ArrayList<Worker>();
            List<Location> locations = getLocations();
            for(Location location : locations){
                List<Worker> workers = location.getWorkers(workday);
                for(Worker worker : workers){
                    if(!locationWorkers.contains(worker)){
                        locationWorkers.add(worker);
                    }
                    else {
                        dayCost += LOCATION_CONSISTENCY_COST;
                    }
                }
            }
            
            cost += dayCost;
        }
        return cost;
    }
    
        /**
     *
     * @param worker
     * @param shift
     * @return
     */
    public int getShiftCompactnessCost() {
        int cost = 0;
        
        for(Worker worker : this.getMutableWorkers() ){
            int compactnessViolation = worker.getCompactnessViolation();
            if(compactnessViolation > 0){
                cost += (compactnessViolation*SHIFT_COMPACTNESS_COST);
            }
        }
        return cost;
    }

    /**
     *
     * @return
     */
    public int getTotalCost() {
        final int DAY_PREFERENCE_CONSTRAINT = getDayComplianceCost();
        final int SHIFT_PREFERENCE_CONSTRAINT = getShiftComplianceCost();
        final int CONTINUITY_PREFERENCE_CONSTRAINT = getContinuityCost();
        final int LOCATION_CONSISTENCY_CONSTRAINT = getLocationConsistencyCost();
        final int SHIFT_COMPACTNESS_CONSTRAINT = getShiftCompactnessCost();

        return (DAY_PREFERENCE_CONSTRAINT + SHIFT_PREFERENCE_CONSTRAINT 
                + CONTINUITY_PREFERENCE_CONSTRAINT + LOCATION_CONSISTENCY_CONSTRAINT 
                + SHIFT_COMPACTNESS_CONSTRAINT);
    }

    /**
     *
     */
    public List<Worker> getShiftVolunteers(LocationShift shift) {
        List<Worker> volunteers = new ArrayList<Worker>();
        for (Worker w : getMutableWorkers()) {
            if (w.isWillingForShift(shift)) {
                volunteers.add(w);
            }
        }
        return volunteers;
    }

    public Worker getWorkerByName(String name) {
        for (Worker w : getWorkers()) {
            if (w.getName().equalsIgnoreCase(name)) {
                return w;
            }
        }
        return null;
    }

    // TODO 
    public void updateVolunteers(LocationShift shift, List<Worker> volunteers) {
        /*
         * for(Worker w : getWorkers() ){ if( w.isWillingForShift(shift) ){
         * w.removePreference(shift); //w.getPreferences().remove(shift);
         *
         * System.out.println("Removed from the mix we had : "+ w.getName() ); }
         * }
         *
         * shift.setVolunteers( new ArrayList<Worker>() );
         *
         * // Link with the new ones... for (Worker worker : volunteers) {
         * worker.addPreferences(shift);
         *
         * System.out.println("In the mix we found : " + worker.getName()); }
         */
    }

        
    /**
     * 
     * @param workday
     * @param shift
     * @return 
     */
    public LocationShift getPreviousShift(LocationShift shift) {
        Location location = shift.getLocation();
        Workday workday = shift.getWorkday();
        
        if( location != null ){
            List<LocationShift> shifts = location.getShifts(workday);
            for(LocationShift tempShift : shifts){
                if( (tempShift.getStart()+LocationShift.MIN_TIME) == shift.getStart() && ((tempShift.getEnd()+LocationShift.MIN_TIME) == shift.getEnd()) ){
                    return tempShift;
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param shift
     * @return 
     */
    public LocationShift getNextShift(LocationShift shift) {
        Location location = shift.getLocation();
        Workday workday = shift.getWorkday();
        
        if( location != null ){
            List<LocationShift> shifts = location.getShifts(workday);
            for(LocationShift tempShift : shifts){
                if( (tempShift.getStart()-LocationShift.MIN_TIME) == shift.getStart() && ((tempShift.getEnd()-LocationShift.MIN_TIME) == shift.getEnd()) ){
                    return tempShift;
                }
            }
        }
        return null;
    }

}
