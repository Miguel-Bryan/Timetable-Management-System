/**
 *
 */
package de.uni.trier.zimk.sp.timetable.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import de.uni.trier.zimk.sp.timetable.oo.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import org.apache.commons.codec.digest.DigestUtils;

import org.apache.log4j.Logger;

/**
 * @author landry.ngani
 *
 */
public class Timetable implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(Timetable.class);
    
    private OrganisationalConfiguration organisationalConfiguration;
    //private WorkerConfiguration workerConfiguration;

    private Worker loggedInWorker;
    
    private TimetableState state;
    private List<TimeTableListener> timeTableListeners;
    private boolean paused;
    private boolean pausable;
    
    private final int MAX_ROUND = 10;
    private final int MAX_SURVIVORS = 10;
    
    private final int BEST_POSSIBLE_COST = 0;

    /**
     *
     *
    public Timetable(List<Workday> workdays, List<Worker> workers) {

        this.state = new TimetableState(workdays, workers);
        timeTableListeners = new ArrayList<TimeTableListener>();

        pausable = true;
    }
    */

    public Timetable(OrganisationalConfiguration organisationalConfiguration) {
        //, WorkerConfiguration workerConfiguration
        
        this.organisationalConfiguration = organisationalConfiguration;
        //this.workerConfiguration = workerConfiguration;
        
        List<Workday> workdays = organisationalConfiguration.getWorkdays();
        List<Worker> workers = organisationalConfiguration.getWorkers();
        
        this.state = new TimetableState(organisationalConfiguration, workdays, workers);
        this.timeTableListeners = new ArrayList<TimeTableListener>();

        pausable = true;
        
    }

    
    public void registerTimeTableListener(TimeTableListener listener) {
        this.timeTableListeners.add(listener);
    }

    public Worker getLoggedInWorker() {
        return loggedInWorker;
    }

    public void setLoggedInWorker(Worker loggedInWorker) {
        this.loggedInWorker = loggedInWorker;
    } 
    
    public void fireWorkerLoggedIn(Worker worker) {
        this.setLoggedInWorker(worker);
        for (TimeTableListener listener : this.timeTableListeners) {
            listener.workerLoggedIn(worker);
        }
    }
    
    public void fireMessageEvent(String message) {
        for (TimeTableListener listener : this.timeTableListeners) {
            listener.messageToDisplay(message);
        }
        logger.info("FIREMESSAGEEVENT : "+message);
    }

    public void fireInfoEvent(String message) {
        for (TimeTableListener listener : this.timeTableListeners) {
            listener.infoToDisplay(message);
        }
    }

    public void fireStatusEvent(String message) {
        for (TimeTableListener listener : this.timeTableListeners) {
            listener.statusToDisplay(message);
        }
    }

    public void fireStateDisplayEvent(TimetableState state) {
        for (TimeTableListener listener : this.timeTableListeners) {
            listener.stateToDisplay(state);
        }
    }

    private void fireEndStateEvent() {
        for (TimeTableListener listener : this.timeTableListeners) {
            listener.endStateReached(state);
        }
    }

    public void fireStateChanged() {
        fireStateDisplayEvent(this.getTimetableState());
    }

    /**
     * @return the state
     */
    public TimetableState getTimetableState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setTimetableState(TimetableState state) {
        this.state = state;
    }

    /**
     * @return the paused
     */
    public synchronized boolean isPaused() {
        return paused;
    }

    /**
     * @param paused the paused to set
     */
    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * @param paused the paused to set
     */
    public synchronized void pause() {
        if (pausable) {
            this.paused = true;
        }
    }

    /**
     * @param paused the paused to set
     */
    public synchronized void forcePause() {
        this.paused = true;
    }

    /**
     * @param paused the paused to set
     */
    public synchronized void play() {
        this.paused = false;
    }

    /**
     * @return the pausable
     */
    public boolean isPausable() {
        return pausable;
    }

    /**
     * @param pausable the pausable to set
     */
    public void setPausable(boolean pausable) {
        this.pausable = pausable;
    }

    private synchronized boolean pauseIt() {
        while (isPaused()) {
            // Wait five seconds *** Actually not needed
            try {
                this.wait(2000);
            } catch (Exception e) {
            }
        }

        pause();
        return isPaused();
    }
    

    /**
     * 
     * @param username
     * @param password
     * @return 
     */
    public Worker tryLogin(String username, String password) {
        List<Worker> workers = organisationalConfiguration.getWorkers();
        for(Worker worker : workers){
            String passwordHash = DigestUtils.md5Hex(password);
            
            System.out.println(worker.getUsername()+" : "+passwordHash);
            
            if(worker.getUsername().equals(username) && worker.getPassword().equals( passwordHash ) ){   
                return worker;
            }
        }
        return null;
    }
    
    
    /*
     * Write the whole algorithm again to eliminate possible misleading
     * thoughts.
     */
    public List<Workday> initStartSolution() {

        pause();
        fireMessageEvent("Starting the initialization...");
        pauseIt();

         logger.info(this.getClass().getSimpleName() + " --> Workers size " +this.getTimetableState().getMutableWorkers().size() );
        
        for (Worker worker : state.getMutableWorkers()) {

            int counter = 0;
            while (!worker.isMaximumShiftsReached()) {
                
                fireMessageEvent("#LOOP_"+counter++ + " Worker : "+worker.getName() +" --> Details : "+ worker.toMoreDetailsString() );

                LocationShift shift = state.getSuitableShift(worker);
                if (shift != null) {

                    
                    if( ! worker.isPlannedForShiftIgnoresLocation(shift) ){
                        worker.addWorktime(shift);
                        
                        logger.info(this.getClass().getSimpleName() + " --> Suitable shift for " + worker + " : " + shift.toString());
                        pauseIt();
                    } 
                    else {
                        LocationShift altShift = state.getStrictSuitableShift(worker);
                        if (altShift != null) {
                            worker.addWorktime(altShift);

                            logger.info(this.getClass().getSimpleName() + " --> Suitable shift for " + worker + " : " + altShift.toString() );
                            pauseIt();  

                        } else {

                            SwitchShift switchShift = new SwitchShift(shift);
                            switchShift = state.getSwitchableShift(worker, switchShift);
                            if (switchShift.getShiftTarget() != null) {

                                Worker targetWorker = null;
                                while (targetWorker == null) {

                                    for (Worker tWorker : switchShift.getShiftTarget().getWorkers()) {
                                        if (!tWorker.isPlannedForShift(shift)) {
                                            targetWorker = tWorker;
                                            break;
                                        }
                                    }

                                    logger.info(this.getClass().getSimpleName() + " --> Actual SwitchShift : " + switchShift.toString());
                                    pauseIt();

                                    if (targetWorker == null) {
                                        // Update the list of Shifts to exclude ***
                                        switchShift.getToExclude().add(switchShift.getShiftTarget());
                                        switchShift = state.getSwitchableShift(worker, switchShift);
                                    }

                                }

                                logger.info(this.getClass().getSimpleName() + " --> Performing switch for " + worker + " on " + shift + " with " + targetWorker + " from " + switchShift);

                                SwitchHelper helper = new SwitchHelper(shift, targetWorker, worker);
                                helper.performInitSwitch(switchShift.getShiftTarget(), state);

                            } else {
                                logger.info(this.getClass().getSimpleName() + " --> No suitable Shift found.");
                            }

                            pauseIt();
                        }
                    }
                }
                else {
                    logger.info(this.getClass().getSimpleName() + " --> No suitable Shift found for "+worker.getName() );
                }
            }

        }

        //this.printMeOutWithWorktimes("Initialization.", "Initialization terminated");
        fireMessageEvent(this.getClass().getSimpleName() + " --> Initialization terminated.");
        pauseIt();
        forcePause();

        //logger.info( this.getClass().getSimpleName() +" -->  " + buffer.toString() );
        return state.getWorkdays();
    }

    /**
     *
     * @return
     */
    public TimetableState generateTimeTable() {

        int breakPoint = 0;
        //int actualCost = this.calculateCostOf(state, -1);
        int actualCost = state.getTotalCost();
        logger.info("generateTimeTable --> Initial cost : "+ actualCost);
        
        //TimetableState tmpState = null;
        List<TimetableState> survivors = new ArrayList<TimetableState>();
        survivors.add(state);

        while (breakPoint < MAX_ROUND) {

            System.out.println("1. Step > Breakpoint value : --> " + breakPoint);

            //tmpState = this.generateNextBestState(state);
            survivors = this.generateNextBestState(survivors);
            Collections.sort(survivors, new TimetableStateComparable());

            //int costOfNextBestState = this.calculateCostOf(tmpState, breakPoint);
            int costOfNextBestState = survivors.get(0).getTotalCost();

            // If we find an optimal state.
            if( costOfNextBestState == BEST_POSSIBLE_COST){
                this.state = survivors.get(0);
                this.fireStateDisplayEvent(state);
                
                forcePause();
            }
            
            if (costOfNextBestState == actualCost) {

                breakPoint++;
                //this.state = tmpState;

            } else {
                if (costOfNextBestState < actualCost) {

                    System.out.println("1. Step >Better condition was found : " + costOfNextBestState);

                    actualCost = costOfNextBestState;
                    this.state = survivors.get(0);
                    breakPoint = 0;

                } else {
                    breakPoint++;
                    continue;
                }
            }
        }

        // Now we should proceed with daily permutation.
        fireMessageEvent(this.getClass().getSimpleName() + "It shouldn't exist any daily violation. ==> Shift permutation. ");
        forcePause();

        breakPoint = 0;
        while (breakPoint < MAX_ROUND) {

            System.out.println("2. Step > Breakpoint value::" + breakPoint);

            //tmpState = this.generateNextBestStateShiftwise(state);
            survivors = this.generateNextBestStateShiftwise(survivors);
            Collections.sort(survivors, new TimetableStateComparable());

            //int costOfNextBestState = this.calculateCostOf(tmpState, breakPoint);
            int costOfNextBestState = survivors.get(0).getTotalCost();

            // If we find an optimal state.
            if( costOfNextBestState == BEST_POSSIBLE_COST){
                this.state = survivors.get(0);
                this.fireStateDisplayEvent(state);
                
                forcePause();
            }
            
            if (costOfNextBestState == actualCost) {
                breakPoint++;
                //this.state = tmpState;

            } else {
                if (costOfNextBestState < actualCost) {

                    System.out.println("2. Step > Better condition was found : " + costOfNextBestState);
                    actualCost = costOfNextBestState;

                    this.state = survivors.get(0);
                    breakPoint = 0;

                } else {
                    breakPoint++;
                    continue;
                }
            }
        }

        //int finalCost = this.calculateCostOf(state, -1);
        int finalCost = state.getTotalCost();
        
        System.out.println("FINAL END COST::" + finalCost);
        fireMessageEvent("FINAL COST : " + finalCost);
        fireEndStateEvent();

        return state;
    }

    /**
     *
     * @param states
     * @return
     */
    public List<TimetableState> generateNextBestState(List<TimetableState> states) {

        ArrayList<TimetableState> currentStateNeighborhood = new ArrayList<TimetableState>();
        List<TimetableState> survivors;

        currentStateNeighborhood.add(state);


        for (TimetableState localState : states) {
            /**
             * generate a random worker
             */
            //SwitchHelper randomHelper = Ramdomizer.randomChooseWorkerAndShiftFromState(localState);
            SwitchHelper randomHelper = this.getSuitableWorkerAndShiftFromState(localState);
            
            //for( SwitchHelper randomHelper : randomHelpers){

                fireMessageEvent(this.getClass().getSimpleName() + " --> Worker " + randomHelper.getRandomWorker().getName() + " selected for NextBestSate.");
                pauseIt();

                for (int d = 0; d < localState.getWorkdays().size(); d++) {
                    Workday workday = localState.getWorkdays().get(d);

                    List<Location> locations = localState.getLocations();
                    for(Location location : locations){

                    List<LocationShift> shiftListOfDay = location.getShifts(workday);

                    for (int s = 0; s < shiftListOfDay.size(); s++) {
                        LocationShift shift = shiftListOfDay.get(s);

                        if (!randomHelper.getRandomWorker().isPlannedForShift(shift)) {

                            List<Worker> workerListOfShift = shift.getWorkers();
                            for (int w = 0; w < workerListOfShift.size(); w++) {

                                Worker targetWorker = workerListOfShift.get(w);
                                if (!targetWorker.isPlannedForShift(randomHelper.getShift())) {

                                    fireMessageEvent(this.getClass().getSimpleName() + " --> SWITCH ATTEMPT : " + randomHelper.getRandomWorker().getName() + " to Worker " + targetWorker.getName() + " on " + shift.toString());
                                    pauseIt();

                                    TimetableState cloneState = new TimetableState(localState);
                                    //cloneState.duplicateWorkersProperties(localState);

                                    randomHelper.setTargetWorker(targetWorker);
                                    try {

                                        cloneState.performSwitch(randomHelper, shift);
                                        //randomHelper.performSwitch(shift, cloneState);

                                    } catch (DuplicateBookingException ex) {
                                        java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    currentStateNeighborhood.add(cloneState);

                                    fireMessageEvent(this.getClass().getSimpleName() + " --> SWITCH PERFORMED : " + randomHelper.getRandomWorker().getName() + " to Worker " + targetWorker.getName() + " switched and cloned State saved.");
                                    //fireStateDisplayEvent(cloneState);
                                    
                                    this.fireMessageEvent("Neighborhood actually contains : " + currentStateNeighborhood.size());
                                    pauseIt();

                                } else {
                                    //fireMessageEvent(this.getClass().getSimpleName() + " --> SWITCH REVOKED : " + targetWorker.getName() + " is already planned for "+ randomHelper.getShift());
                                    pauseIt();
                                }
                            }
                        } else {
                            //fireMessageEvent(this.getClass().getSimpleName() + " --> SHIFT SKIPPED : " + randomHelper.getRandomWorker().getName() + " already planned in " + shift.toString() + "");    
                        }
                    }   

                   }
                }
            
            //}
            

        }

        /**
         * Get neighborhood best state : the state with the smallest state cost
         */
        TimetableState bestState = this.getNeighborhoodBestState(currentStateNeighborhood);
        survivors = this.getNeighborhoodSurvivors(currentStateNeighborhood);

        //int bestCost = state.getTotalCost();
        
        final int DAY_PREFERENCE_CONSTRAINT = bestState.getDayComplianceCost();
        final int SHIFT_PREFERENCE_CONSTRAINT = bestState.getShiftComplianceCost();
        final int CONTINUITY_PREFERENCE_CONSTRAINT = bestState.getContinuityCost();
        final int LOCATION_CONSISTENCY_CONSTRAINT = bestState.getLocationConsistencyCost();
        final int SHIFT_COMPACTNESS_CONSTRAINT = bestState.getShiftCompactnessCost();
        
        int bestCost = (DAY_PREFERENCE_CONSTRAINT + SHIFT_PREFERENCE_CONSTRAINT 
                + CONTINUITY_PREFERENCE_CONSTRAINT + LOCATION_CONSISTENCY_CONSTRAINT 
                + SHIFT_COMPACTNESS_CONSTRAINT );
        
        fireMessageEvent(this.getClass().getSimpleName() + " --> BestState found out of all cloned States --> " + bestCost 
                + " WITH DV = "+DAY_PREFERENCE_CONSTRAINT
                + " / SV = "+SHIFT_PREFERENCE_CONSTRAINT
                + " / CV = "+CONTINUITY_PREFERENCE_CONSTRAINT
                + " / LV = "+LOCATION_CONSISTENCY_CONSTRAINT
                + " / SCV = "+SHIFT_COMPACTNESS_CONSTRAINT);
        
        fireStateDisplayEvent(bestState);

        this.fireInfoEvent("Cost of the actual BestState : " + bestCost
                + " WITH DV = "+DAY_PREFERENCE_CONSTRAINT
                + " / SV = "+SHIFT_PREFERENCE_CONSTRAINT
                + " / CV = "+CONTINUITY_PREFERENCE_CONSTRAINT
                + " / LV = "+LOCATION_CONSISTENCY_CONSTRAINT
                + " / SCV = "+SHIFT_COMPACTNESS_CONSTRAINT);

        //forcePause();
        pauseIt();

        //this.printMeOut("generateNextBestState()", "Preferences.");
        return survivors;
    }

    /**
     *
     * @param states
     * @return
     */
    public List<TimetableState> generateNextBestStateShiftwise(List<TimetableState> states) {

        /**
         * Feasible solutions (states) around the current state
         */
        List<TimetableState> survivors;
        ArrayList<TimetableState> currentStateNeighborhood = new ArrayList<TimetableState>();


        for (TimetableState localState : states) {
            /**
             * generate a random worker
             */
            Workday workday = Ramdomizer.randomChooseWorkday(localState.getWorkdays());

            List<Location> locations = localState.getLocations();
            for(Location location : locations){

                    List<LocationShift> shiftListOfDay = location.getShifts(workday);
                    for (LocationShift shift : shiftListOfDay) {

                    List<LocationShift> shifts = location.getShifts(workday);
                    List<Worker> workers = shift.getWorkers();

                    for (Worker worker : workers) {
                        for (LocationShift targetShift : shifts) {
                            if (!targetShift.equals(shift)) {

                                for (Worker targetWorker : targetShift.getWorkers()) {

                                    if ((!worker.isPlannedForShift(targetShift)) && (!targetWorker.isPlannedForShift(shift))) {

                                        SwitchHelper randomHelper = new SwitchHelper(shift, targetWorker, worker);

                                        TimetableState cloneState = new TimetableState(localState);
                                        //cloneState.duplicateWorkersProperties(localState);

                                        randomHelper.setTargetWorker(targetWorker);
                                        try {

                                            cloneState.performSwitch(randomHelper, targetShift);
                                            //randomHelper.performSwitch(targetShift, cloneState);

                                        } catch (DuplicateBookingException ex) {
                                            java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                        currentStateNeighborhood.add(cloneState);

                                        fireMessageEvent(this.getClass().getSimpleName() + " --> SWITCH PERFORMED : " + randomHelper.getRandomWorker().getName() + " to Worker " + randomHelper.getTargetWorker().getName() + " switched and cloned State saved.");
                                        //fireStateDisplayEvent(cloneState);
                                        pauseIt();

                                    } else {
                                        fireMessageEvent(this.getClass().getSimpleName() + " --> SHIFT SKIPPED : " + worker.getName() + " ==> " + targetShift.toString()
                                                + " / " + targetWorker.getName() + " ==> " + shift.toString());
                                    }
                                }
                            } else {

                                fireMessageEvent(this.getClass().getSimpleName() + " --> SKIPPING  : " + shift.toString() + " to " + targetShift.toString() + " - Same shift.");
                                //forcePause();
                            }
                        }
                    }
                }
            }
            
        }
        
        /**
         * Get neighborhood best state : the state with the smallest state cost
         */
        TimetableState bestState = this.getNeighborhoodBestState(currentStateNeighborhood);
        survivors = this.getNeighborhoodSurvivors(currentStateNeighborhood);

        fireMessageEvent(this.getClass().getSimpleName() + " --> BestState found out of all cloned States --> " + bestState.getTotalCost() );
        fireStateDisplayEvent(bestState);
        
        final int DAY_PREFERENCE_CONSTRAINT = bestState.getDayComplianceCost();
        final int SHIFT_PREFERENCE_CONSTRAINT = bestState.getShiftComplianceCost();
        final int CONTINUITY_PREFERENCE_CONSTRAINT = bestState.getContinuityCost();
        final int LOCATION_CONSISTENCY_CONSTRAINT = bestState.getLocationConsistencyCost();
        final int SHIFT_COMPACTNESS_CONSTRAINT = bestState.getShiftCompactnessCost();
        
        int bestCost = (DAY_PREFERENCE_CONSTRAINT + SHIFT_PREFERENCE_CONSTRAINT 
                + CONTINUITY_PREFERENCE_CONSTRAINT + LOCATION_CONSISTENCY_CONSTRAINT 
                + SHIFT_COMPACTNESS_CONSTRAINT );
        
        fireMessageEvent(this.getClass().getSimpleName() + " --> BestState found out of all cloned States --> " + bestCost 
                + " WITH DV = "+DAY_PREFERENCE_CONSTRAINT
                + " / SV = "+SHIFT_PREFERENCE_CONSTRAINT
                + " / CV = "+CONTINUITY_PREFERENCE_CONSTRAINT
                + " / LV = "+LOCATION_CONSISTENCY_CONSTRAINT
                + " / SCV = "+SHIFT_COMPACTNESS_CONSTRAINT);
        
        fireStateDisplayEvent(bestState);

        this.fireInfoEvent("Cost of the actual BestState : " + bestCost
                + " WITH DV = "+DAY_PREFERENCE_CONSTRAINT
                + " / SV = "+SHIFT_PREFERENCE_CONSTRAINT
                + " / CV = "+CONTINUITY_PREFERENCE_CONSTRAINT
                + " / LV = "+LOCATION_CONSISTENCY_CONSTRAINT
                + " / SCV = "+SHIFT_COMPACTNESS_CONSTRAINT);

        //forcePause();
        pauseIt();

        //this.printMeOut("generateNextBestState()", "Preferences.");
        return survivors;

    }

        /**at de.uni.trier.zimk.sp.timetable.oo.TimetableState.getShiftRowLength(TimetableState.java:486)
     *
     * @param neighborhood
     * @return
     */
    public TimetableState getNeighborhoodBestState(ArrayList<TimetableState> neighborhood) {
        if (neighborhood.isEmpty()) {
            throw new IllegalArgumentException("Empty population. Please check reproduction procedure.");
        }

        Collections.sort(neighborhood, new TimetableStateComparable());
        return neighborhood.get(0);
    }

    /**
     *
     * @param neighborhood
     * @return
     */
    public List<TimetableState> getNeighborhoodSurvivors(ArrayList<TimetableState> neighborhood) {

        List<TimetableState> bestStates = new ArrayList<TimetableState>();
        Collections.sort(neighborhood, new TimetableStateComparable());

        for (TimetableState localState : neighborhood) {

            bestStates.add(localState);

            if (bestStates.size() == MAX_SURVIVORS) {
                return bestStates;
            }
        }

        return bestStates;
    }
    
    
    /**
     *
     */
    public void saveState() {
        ObjectOutputStream out = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            TimetableState localState = getTimetableState();
            
           /*
            
            //XStream xstream = new XStream(new StaxDriver());
            XStream xstream = new XStream(new DomDriver());
            
            xstream.alias("state", TimetableState.class);
            xstream.alias("workday", Workday.class);
            xstream.alias("worker", Worker.class);
            xstream.alias("shift", Shift.class);
            
            xstream.processAnnotations(TimetableState.class);
            xstream.processAnnotations(Workday.class);
            xstream.processAnnotations(Worker.class);
            xstream.processAnnotations(Shift.class);
            
            String xml = xstream.toXML(localState);

            System.out.println("\n\n-->\n"
                    + xml
                    + "\n<-- \n\n");

            File xmlFile = new File(Timetable.class.getResource("/de/uni/trier/zimk/sp/timetable/io/state.xml").getFile());
            out = new ObjectOutputStream(new FileOutputStream(xmlFile));
            
            //out.writeUTF(xml);        
            out.writeObject(xml);
            
            out.close();
            
            */
           
           Gson gson = new GsonBuilder().setPrettyPrinting().create();
           
           String jsonState = gson.toJson(localState);
           
           System.out.println("\n\n-->\n" + jsonState + "\n<-- \n\n");
           
           File jsonFIle = new File(Timetable.class.getResource("/de/uni/trier/zimk/sp/timetable/io/state.json").getFile());
           
           mapper.writeValue(jsonFIle, jsonState);

        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    /**
     *
     * @return
     */
    public void loadState() {

        forcePause();
        
        TimetableState state = null;

        //XStream xstream = new XStream(new StaxDriver());
        XStream xstream = new XStream(new DomDriver());
        
        xstream.alias("state", TimetableState.class);
        xstream.alias("workday", Workday.class);
        xstream.alias("worker", Worker.class);
        xstream.alias("shift", Shift.class);
        
        xstream.processAnnotations(TimetableState.class);
        xstream.processAnnotations(Workday.class);
        xstream.processAnnotations(Worker.class);
        xstream.processAnnotations(Shift.class);
        
        String userHome = System.getProperty("user.home");
        JFileChooser chooser = new JFileChooser(userHome);

        try {
            File xmlFile = new File(Timetable.class.getResource("/de/uni/trier/zimk/sp/timetable/io/state.xml").getFile());
            InputStream in = new FileInputStream(xmlFile);
            
            state = (TimetableState) xstream.fromXML(in);
            //state = (TimetableState) xstream.fromXML(xmlFile);
            
            logger.info( "State loaded with : "+ state.getWorkdays().size() + " / "+ state.getWorkers().size() );
            
            
            //PATCH
            /*
            for( Workday workday : state.getWorkdays() ){
                
                List<Shift> shifts = workday.getShifts();
                for( Shift shift : shifts ){
                    
                    if( shift.getEnd() == 18 ){
                        shift.setCapacity(2);
                    }
                    
                    if( workday.getName().equalsIgnoreCase("Friday") ){
                        if( shift.getEnd() == 16 ){
                            shift.setCapacity(1);
                        }
                    }
                }
            }
            */
            
            this.setTimetableState(state);
            fireStateDisplayEvent(state);

        }catch (Exception ex) {
            java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void loadStoredState() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 
     */
    private List<SwitchHelper> getSuitableWorkerAndShiftFromState_OLD(TimetableState localState) {
        LocationShift shift = null;
        
        
            boolean empty = true;
            while(empty){
                // First, randomly choose a day
                List<Workday> workdays = localState.getWorkdays();
                int randomDay = new Random().nextInt(workdays.size());
                Workday day = workdays.get(randomDay);

                // Second, randomly choose a Location
                List<Location> locations = localState.getLocations();
                int randomLocation = new Random().nextInt(locations.size());
                Location location = locations.get(randomLocation);

                // Third, randomly choose a shift
                List<LocationShift> shiftListOfDay = location.getShifts(day); //day.getShifts();
                int randomShift = new Random().nextInt(shiftListOfDay.size());
                shift = shiftListOfDay.get(randomShift);
                
                empty = shift.getWorkers().isEmpty();
            }
	    
	    /* Then, randomly choose an employee */
            //List<Worker> shiftWorkerList = shift.getWorkers();
	    List<Worker> shiftWorkerList = localState.getMutableWorkers();
	    int randomWorker = new Random().nextInt(shiftWorkerList.size());
	    Worker worker = shiftWorkerList.get(randomWorker);
               
            List<SwitchHelper>helpers = new ArrayList<SwitchHelper>();
            for( LocationShift switchShift : worker.getShifts() ){
                SwitchHelper helper = new SwitchHelper(switchShift, null, worker );
                helpers.add(helper);
            }
            
	    return helpers;
    }
    
    /**
     * 
     */
    private SwitchHelper getSuitableWorkerAndShiftFromState(TimetableState localState) {
        LocationShift shift = null;
  
	    
	    /* Then, randomly choose an employee */
            //List<Worker> shiftWorkerList = shift.getWorkers();
	    List<Worker> shiftWorkerList = localState.getMutableWorkers();
            Worker selectedWorker = null;
            for(Worker worker : shiftWorkerList){
                shift = worker.isWorkdayViolationFree();
                if( shift != null ){
                    selectedWorker = worker;
                    break;
                }
            }
            
            if( selectedWorker != null ){
                SwitchHelper helper =  new SwitchHelper(shift, null, selectedWorker );
                return helper;
            }
            else {
                for(Worker worker : shiftWorkerList){
                    shift = worker.isShiftViolationFree();
                    if( shift != null ){
                        selectedWorker = worker;
                        break;
                    }
                }
            }
            
            if( selectedWorker != null ){
                SwitchHelper helper =  new SwitchHelper(shift, null, selectedWorker );
                return helper;
            }
            else {
             
                      
                boolean empty = true;
                while(empty){
                    // First, randomly choose a day
                    List<Workday> workdays = localState.getWorkdays();
                    int randomDay = new Random().nextInt(workdays.size());
                    Workday day = workdays.get(randomDay);

                    // Second, randomly choose a Location
                    List<Location> locations = localState.getLocations();
                    int randomLocation = new Random().nextInt(locations.size());
                    Location location = locations.get(randomLocation);

                    // Third, randomly choose a shift
                    List<LocationShift> shiftListOfDay = location.getShifts(day); //day.getShifts();
                    int randomShift = new Random().nextInt(shiftListOfDay.size());
                    shift = shiftListOfDay.get(randomShift);

                    empty = shift.getWorkers().isEmpty();
                }
                
                int randomWorker = new Random().nextInt(shiftWorkerList.size());
                Worker worker = shiftWorkerList.get(randomWorker);

                SwitchHelper helper =  new SwitchHelper(shift, null, worker );
                return helper;
            
            
            }
    }


    
}
