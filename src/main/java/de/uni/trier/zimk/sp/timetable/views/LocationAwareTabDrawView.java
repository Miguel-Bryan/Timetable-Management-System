/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.views;

import de.uni.trier.zimk.sp.timetable.oo.Location;
import de.uni.trier.zimk.sp.timetable.oo.OrganisationalConfiguration;
import de.uni.trier.zimk.sp.timetable.oo.TimetableState;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Landry Ngani
 */
public class LocationAwareTabDrawView extends JPanel {
    
    private JTabbedPane tabbedPane;
     private JLabel actionLabel;
     
    private Timetable timetable;
    private OrganisationalConfiguration configuration;
    
    /**
     * 
     */
    public LocationAwareTabDrawView(OrganisationalConfiguration configuration, Timetable timetable){
       
        this.tabbedPane = new JTabbedPane();
        this.configuration = configuration;
        this.timetable = timetable;
        
        
        for(Location location : timetable.getTimetableState().getLocations()){
        //for(Location location : configuration.getLocations()){
            TimetableDrawPanel editorView = new TimetableDrawPanel(configuration, location, timetable.getTimetableState());
                    
            this.tabbedPane.addTab( location.getName() , new javax.swing.ImageIcon(getClass().getResource("/icons/class-location.png")), editorView, "Plan - "+ location.getName() );
        }
        
        actionLabel = new JLabel("Actions : ");
	actionLabel.setFont( new Font("Arial", Font.BOLD, 14) );
        
        this.setLayout( new BorderLayout());
        this.add( actionLabel , BorderLayout.NORTH );
        this.add( tabbedPane , BorderLayout.CENTER );
    }
    
    /**
     * 
     * @param state 
     */
    public void stateToDisplay(TimetableState state){
        
        for(Component component : this.tabbedPane.getComponents() ){
            if( component instanceof TimetableDrawPanel){
                ((TimetableDrawPanel)component).stateToDisplay(state);
            }
        }
        
        /*
        this.tabbedPane.removeAll();
        //this.tabbedPane = new JTabbedPane();
        this.configuration = state.getOrganisationalConfiguration();
        for(Location location : configuration.getLocations()){
            TimetableDrawPanel editorView = new TimetableDrawPanel(configuration, location, timetable.getTimetableState());
            this.tabbedPane.addTab( location.getName() , new javax.swing.ImageIcon(getClass().getResource("/icons/location-icon.png")), editorView, "Plan - "+ location.getName() );
        }
        
        this.tabbedPane.revalidate();
        */
    }
    
    /**
	 * @return the actionLabel
	 */
	public JLabel getActionLabel() {
            return actionLabel;
	}

	/**
	 * @param actionLabel the actionLabel to set
	 */
	public void setActionLabel(JLabel actionLabel) {
            this.actionLabel = actionLabel;
	}
        
	public void messageToDisplay(String message) {
            actionLabel.setText(message);
	}
    
}
