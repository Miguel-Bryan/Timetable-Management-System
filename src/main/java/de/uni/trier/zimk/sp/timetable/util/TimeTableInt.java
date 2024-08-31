/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.util;

import java.util.ArrayList;
import java.util.List;

import de.uni.trier.zimk.sp.timetable.oo.TimetableState;
import de.uni.trier.zimk.sp.timetable.oo.Workday;

/**
 * @author pkendzo
 *
 */
interface TimeTableInt
{

    /**
     * initialize the first solution
     */
    public List<Workday> initStartSolution();
    
    /**
     * calculate the cost of a state. A state is a list of workdays
     * States can be differentiate between each other by the assignment of his shifts
     */
    public int calculateCostOf(List<Workday> workdays);
    
    /**
     * use to permute two workers
     * @param w1
     * @param w2
     * @param targetShift the shift where the random worker will be move to
     * @return
     */
//    public boolean permuteWorker(Shift targetShift, Worker randomWorkder, Worker targetWorker);
    
    /**
     * 
     * @param wordays
     * @return
     */
    public TimetableState generateNextBestState(TimetableState state);
    
    /**
     * 
     * @param state list of Workdays to clone
     * @return the cloned state
     */
    public List<Workday> cloneState(List<Workday> state);
    
    /**
     * 
     * @param neighborhood
     * @return
     */
    public TimetableState getNeighborhoodBestState(ArrayList<TimetableState> neighborhood);
    
    
    /**
     * 
     * @return
     */
    public TimetableState generateTimeTable();
}
