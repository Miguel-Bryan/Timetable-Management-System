/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.util;

import de.uni.trier.zimk.sp.timetable.oo.TimetableState;
import de.uni.trier.zimk.sp.timetable.oo.Worker;

/**
 * @author me
 *
 */
public interface TimeTableListener {

	public void messageToDisplay( String message);
        
        public void infoToDisplay( String message);
        
        public void statusToDisplay( String message);
	
	public void permutationExecuted( String resumee);
	
	public void stateToDisplay( TimetableState state);

        public void endStateReached(TimetableState state);

        public void workerLoggedIn(Worker worker);
        
}
