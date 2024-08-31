/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.print;

import de.uni.trier.zimk.sp.timetable.oo.Location;
import de.uni.trier.zimk.sp.timetable.oo.OrganisationalConfiguration;
import de.uni.trier.zimk.sp.timetable.oo.TimetableState;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Landry Ngani
 */
public class LocationPrintTabView extends JPanel {
    
    private JTabbedPane tabbedPane;
     
    //private JLabel actionLabel;
     
    private Timetable timetable;
    private OrganisationalConfiguration configuration;
    
    /**
     * 
     */
    public LocationPrintTabView(OrganisationalConfiguration configuration, Timetable timetable){
       
        this.tabbedPane = new JTabbedPane();
        this.configuration = configuration;
        this.timetable = timetable;
        
        //for(Location location : configuration.getLocations()){
        for(Location location : timetable.getTimetableState().getLocations()){
            
            LocationPrintTableModel configurationTableModel = new LocationPrintTableModel(configuration, location);
            LocationPrintTimetableView editorView = new LocationPrintTimetableView(configuration, timetable, configurationTableModel);
                    
            this.tabbedPane.addTab( location.getName() , new javax.swing.ImageIcon(getClass().getResource("/icons/location-icon.png")), editorView, "Settings "+ location.getName() );
        }
        
        /*
        actionLabel = new JLabel("  Timetable - SS 2012 ");
        actionLabel.setFont( new Font("Arial", Font.PLAIN, 18) );
        actionLabel.setHorizontalAlignment( JLabel.CENTER);
        */
        
        this.setLayout( new BorderLayout());
        //this.add( actionLabel , BorderLayout.NORTH );
        this.add( tabbedPane , BorderLayout.CENTER );
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
    
    /**
     * 
     * @param state 
     */
    public void stateToDisplay(TimetableState state){
        
        this.configuration = state.getOrganisationalConfiguration();
        for(Component component : this.tabbedPane.getComponents() ){
            if( component instanceof LocationPrintTimetableView){
                ((LocationPrintTimetableView)component).stateToDisplay(state);
            }
        }
    }
    
}
