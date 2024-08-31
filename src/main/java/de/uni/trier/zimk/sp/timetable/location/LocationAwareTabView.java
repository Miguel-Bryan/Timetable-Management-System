/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.location;

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
public class LocationAwareTabView extends JPanel {
    
    private JTabbedPane tabbedPane;
     private JLabel actionLabel;
     
    private Timetable timetable;
    private OrganisationalConfiguration configuration;
    
    /**
     * 
     */
    public LocationAwareTabView(OrganisationalConfiguration configuration, Timetable timetable){
       
        this.tabbedPane = new JTabbedPane();
        this.configuration = configuration;
        this.timetable = timetable;
        
        //for(Location location : configuration.getLocations()){
        for(Location location : timetable.getTimetableState().getLocations() ){
            
            LocationAwareTableModel configurationTableModel = new LocationAwareTableModel(configuration, location);
            LocationAwareTimetableView editorView = new LocationAwareTimetableView(timetable, configurationTableModel);
                    
            this.tabbedPane.addTab( location.getName() , new javax.swing.ImageIcon(getClass().getResource("/icons/class-location.png")), editorView, "Settings "+ location.getName() );
        }
        
        actionLabel = new JLabel("Actions : ");
	actionLabel.setFont( new Font("Arial", Font.BOLD, 14) );
        actionLabel.setHorizontalAlignment(JLabel.CENTER);
        
        this.setLayout( new BorderLayout());
        this.add( actionLabel , BorderLayout.NORTH );
        this.add( tabbedPane , BorderLayout.CENTER );
    }
    
    /**
     * 
     * @param state 
     */
    public void stateToDisplay(TimetableState state){
        
        this.configuration = state.getOrganisationalConfiguration();
        for(Component component : this.tabbedPane.getComponents() ){
            if( component instanceof LocationAwareTimetableView){
                ((LocationAwareTimetableView)component).stateToDisplay(state);
            }
        }
        
        /*
        this.tabbedPane.removeAll();
        //this.tabbedPane = new JTabbedPane();
        
        this.configuration = state.getOrganisationalConfiguration();
        for(Location location : configuration.getLocations()){
            LocationAwareTableModel configurationTableModel = new LocationAwareTableModel(configuration, location);
            LocationAwareTimetableView editorView = new LocationAwareTimetableView(timetable, configurationTableModel);
                    
            this.tabbedPane.addTab( location.getName() , new javax.swing.ImageIcon(getClass().getResource("/icons/location-icon.png")), editorView, "Settings "+ location.getName() );
        }
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
