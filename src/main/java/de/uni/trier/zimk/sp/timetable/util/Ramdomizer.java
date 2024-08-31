/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.util;

import de.uni.trier.zimk.sp.timetable.oo.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author landry.ngani
 *
 */
public class Ramdomizer {

    
    public static int RANDOMDAYX = 0;
    public static int RANDOMSHIFTX = 0;
	
	/*
	 * 
	 *
	public static List<Shift> createPreferences(Worker worker) {
		
		List<Shift> preferences = new ArrayList<Shift>();
		List<Workday> workdays = Workday.getAll();
		
		while ( worker.getDebit() >= preferences.size() ) {
			
			// Up to 2 days ***
			int loop = 2 +(int)(Math.random()*2);
			for( int i = 0; i < loop; i++ ){
				
				int randomDay = (int)(Math.random()*workdays.size());
				Workday workday = workdays.get(randomDay);
				
				int randomShift = (int)(Math.random()* (workday.getShifts().size()-1) );
				
//				System.out.println("number of shifts for "+ workday.getName() + " : "+ workday.getShifts().size() );
//				System.out.println("Generated number : "+ randomShift );
				
				Shift s = workday.getShifts().get(randomShift);
				Shift s1 = workday.getShifts().get(randomShift+1);
				
				preferences.add(s);
				preferences.add(s1);
			}
			
		}

		return preferences;
	}
	
        
	public static List<Shift> completeUnsufficientPreferencesS(Worker worker) {
		
		List<TimePeriod> preferences = worker.getPreferences();
		List<Workday> workdays = Workday.getAll();
		double missing = worker.getDebit() - preferences.size(); 
		
		// Up to 3 days ***
		int loop = (int)missing;
		for( int i = 0; i < loop; i++ ){
			
			int randomDay = (int)(Math.random()*workdays.size());
			Workday workday = workdays.get(randomDay);
			
			int randomShift = (int)(Math.random()* (workday.getShifts().size()-1) );
			Shift s = workday.getShifts().get(randomShift);
			Shift s1 = workday.getShifts().get(randomShift+1);
			
			if( ! preferences.contains(s) ){
				preferences.add(s);
			}
			if( ! preferences.contains(s1) ){
				preferences.add(s1);
			}
		}

		return preferences;
	}
        */
	
	/**
	 * 
	 * @param workdays
	 * @return the randomly choose worker
	 *
	public static Worker randomChooseWorkerFromState(List<Workday> workdays){
	    
	    int randomDay = new Random().nextInt(workdays.size());
	    Ramdomizer.RANDOMDAY = randomDay;
	    Workday day = workdays.get(randomDay);
	    
	    List<Shift> shiftListOfDay = day.getShifts();
	    int randomShift = new Random().nextInt(shiftListOfDay.size());
	    Ramdomizer.RANDOMSHIFT = randomShift;
	    Shift shift = shiftListOfDay.get(randomShift);
	    
	    List<Worker> shiftWorkerList = shift.getWorkers();
	     int randomWorker = new Random().nextInt(shiftWorkerList.size());
		
	    return shiftWorkerList.get(randomWorker);
	}
	*/
	
        /**
	 * 
	 * @param workdays
	 * @return the randomly choose worker
	 */
	public static Workday randomChooseWorkday(List<Workday> workdays){
	    
	    /** First, randomly choose a day */
	    int randomDay = new Random().nextInt(workdays.size());
	    Workday day = workdays.get(randomDay);
         
            return day;
        }
	/**
	 * 
	 * @param workdays
	 * @return the randomly choose worker
	 */
	public static SwitchHelper randomChooseWorkerAndShiftFromState(TimetableState state){
            
            LocationShift shift = null;
            boolean empty = true;
            while(empty){
                /** First, randomly choose a day */
                List<Workday> workdays = state.getWorkdays();
                int randomDay = new Random().nextInt(workdays.size());
                Workday day = workdays.get(randomDay);

                /* Second, randomly choose a Location */
                List<Location> locations = state.getLocations();
                int randomLocation = new Random().nextInt(locations.size());
                Location location = locations.get(randomLocation);

                /* Third, randomly choose a shift */
                List<LocationShift> shiftListOfDay = location.getShifts(day); //day.getShifts();
                int randomShift = new Random().nextInt(shiftListOfDay.size());
                shift = shiftListOfDay.get(randomShift);
            
                randomShift = new Random().nextInt(shiftListOfDay.size());
                shift = shiftListOfDay.get(randomShift);
                
                empty = shift.getWorkers().isEmpty();
            }
	    
	    /* Then, randomly choose an employee */
	    List<Worker> shiftWorkerList = shift.getWorkers();
	    int randomWorker = new Random().nextInt(shiftWorkerList.size());
	    Worker worker = shiftWorkerList.get(randomWorker);
                     
	    SwitchHelper helper = new SwitchHelper(shift, null, worker );
	    return helper;
	}
        
        
        /**
	 * 
	 * @param workdays
	 * @return the randomly choose worker
	 */
	public static SwitchHelper randomChooseWorkerAndShiftFromState(TimetableState state, Workday workday){
	   
            /* Second, randomly choose a Location */
            List<Location> locations = state.getLocations();
            int randomLocation = new Random().nextInt(locations.size());
	    Location location = locations.get(randomLocation);
            
	    /* Third, randomly choose a shift */
	    List<LocationShift> shiftListOfDay = location.getShifts(workday);
	    int randomShift = new Random().nextInt(shiftListOfDay.size());
	    LocationShift shift = shiftListOfDay.get(randomShift);
	    
	    /* Then, randomly choose an employee */
	    List<Worker> shiftWorkerList = shift.getWorkers();
	    int randomWorker = new Random().nextInt(shiftWorkerList.size());
	     
	    SwitchHelper helper = new SwitchHelper(shift, null, shiftWorkerList.get(randomWorker) );
	    return helper;
	    
	}
	
}
