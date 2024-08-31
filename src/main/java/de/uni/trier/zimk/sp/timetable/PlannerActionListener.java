/**
 * 
 */
package de.uni.trier.zimk.sp.timetable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.uni.trier.zimk.sp.timetable.util.Timetable;

/**
 * @author landry.ngani
 *
 */
public class PlannerActionListener implements ActionListener {

	private Planner planner;
	private Timetable timetable;
	
	/**
	 * 
	 */
	public PlannerActionListener(Planner planner, Timetable timetable) {
		this.planner = planner;
		this.timetable = timetable;
		
		this.planner.getPlannerView().getNext().addActionListener( this );
		this.planner.getPlannerView().getContiniousBox().addActionListener( this );
                        this.planner.getPlannerView().getExit().addActionListener( this );
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == planner.getPlannerView().getNext() ){
			
			planner.loadData();
                        
			planner.loadPrefrences();
			
			timetable.play();
		}
		else {
			if( e.getSource() == planner.getPlannerView().getContiniousBox() ){
				timetable.setPausable( planner.getPlannerView().getContiniousBox().isSelected() );
			}
                        else {
                            if( e.getSource() == planner.getPlannerView().getExit() ){
                                planner.shutdown();
                            }
                        }
		}
	}

}
