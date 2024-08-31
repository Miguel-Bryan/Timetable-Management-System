/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.oo;

/**
 * @author landry
 *
 */
public class SwitchHelper {
	
	private LocationShift shift;
	private Worker targetWorker;
	private Worker randomWorker;
	
	
	public SwitchHelper(LocationShift shift, Worker targetWorker, Worker randomWorker) {
		this.shift = shift;
		this.targetWorker = targetWorker; 
		this.randomWorker = randomWorker; 
	}
	
	public SwitchHelper( SwitchHelper helper) {
		
		if( helper.getShift() == null || helper.getRandomWorker() == null ){
			throw new IllegalArgumentException("At least the Shift and the Random worker has to be assigned.");
		}
		
		this.shift = new LocationShift(helper.getShift());
		if( helper.getTargetWorker() != null ){
			this.targetWorker = new Worker( helper.getTargetWorker() ); 
		}
		
		this.randomWorker = new Worker( helper.getRandomWorker() ); 
	}

	
	public LocationShift getShift() {
		return shift;
	}
	public void setShift(LocationShift shift) {
		this.shift = shift;
	}
	public Worker getRandomWorker() {
		return randomWorker;
	}
	public void setRandomWorker(Worker worker) {
		this.randomWorker = worker;
	}
	public Worker getTargetWorker() {
		return targetWorker;
	}
	public void setTargetWorker(Worker targetWorker) {
		this.targetWorker = targetWorker;
	}


	public String toString(){
		return randomWorker.toString() + " TO "+ targetWorker.toString() + " on "+ shift.toString();
	}


	/**
	 * 
	 *
	public void performSwitch( Shift iShift) {
		
		if( getRandomWorker() == null || getTargetWorker() == null ){
			throw new IllegalStateException("Permutation isn't possible with no Random or Target Worker defined : null");
		}
		
		getRandomWorker().addWorktime(iShift);
		getRandomWorker().removeWorktime( getShift() );
		getTargetWorker().addWorktime( getShift() );
	}
	*/
	
	/**
	 * 
	 */
	public void performSwitch( LocationShift iShift, TimetableState state) throws DuplicateBookingException {
		
		Worker rWorker = state.getWorker( this.getRandomWorker() );
		Worker tWorker = state.getWorker( this.getTargetWorker() );
		
		System.out.println("" +this.getClass().getSimpleName()+ "========================> "+tWorker);
		System.out.println("" +this.getClass().getSimpleName()+ "========================> "+getTargetWorker());
		
		if( rWorker == null ){
			throw new IllegalStateException("Permutation isn't possible with no RandomWorker defined : null");
		}
			
		if(tWorker == null ){
			throw new IllegalStateException("Permutation isn't possible with no TargetWorker defined : null");
		}
        
                if( (! rWorker.isPlannedForShift(iShift)) && (! tWorker.isPlannedForShift(getShift())) ){
                    rWorker.addWorktime(iShift);
                    rWorker.removeWorktime( getShift() );

                    tWorker.addWorktime( getShift() );
                    tWorker.removeWorktime( iShift );
                    
                    // Save operation
                    state.setPrePermuted( getShift() );
                    state.setPostPermuted( iShift );
                }
                else {
                    throw new DuplicateBookingException("TargetWorker "+ tWorker.getName()+ " already booked for Shift : "+ getShift().toString()
                            + "=========> OR RandomWorker "+ rWorker.getName()+ " already booked for Shift : "+ iShift.toString() );

                }
		
	}
        
        
        public void performInitSwitch( LocationShift iShift, TimetableState state) {
		
		Worker rWorker = state.getWorker( this.getRandomWorker() );
		Worker tWorker = state.getWorker( this.getTargetWorker() );
		
		System.out.println("" +this.getClass().getSimpleName()+ "========================> "+tWorker);
		System.out.println("" +this.getClass().getSimpleName()+ "========================> "+getTargetWorker());
		
		if( rWorker == null ){
			throw new IllegalStateException("Permutation isn't possible with no RandomWorker defined : null");
		}
			
		if(tWorker == null ){
			throw new IllegalStateException("Permutation isn't possible with no TargetWorker defined : null");
		}

		rWorker.addWorktime(iShift);
		//rWorker.removeWorktime( getShift() );
		
		tWorker.addWorktime( getShift() );
		tWorker.removeWorktime( iShift );
	}

}
