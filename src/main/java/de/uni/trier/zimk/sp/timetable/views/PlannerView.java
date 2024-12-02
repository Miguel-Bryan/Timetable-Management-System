/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.views;

import de.uni.trier.zimk.sp.timetable.configuration.ConfigurationEditorView;
import de.uni.trier.zimk.sp.timetable.configuration.ConfigurationTableModel;
import de.uni.trier.zimk.sp.timetable.location.LocationAwareTabView;
import de.uni.trier.zimk.sp.timetable.oo.*;
import de.uni.trier.zimk.sp.timetable.preferences.EditableDayShiftTimetableTableModel;
import de.uni.trier.zimk.sp.timetable.preferences.UsersAndPreferencesEditor;
import de.uni.trier.zimk.sp.timetable.preferences.UsersAndPreferencesModel;
import de.uni.trier.zimk.sp.timetable.print.PrintActionListener;
import de.uni.trier.zimk.sp.timetable.print.PrintStringConst;
import de.uni.trier.zimk.sp.timetable.print.SaveStateActionListener;
import de.uni.trier.zimk.sp.timetable.util.TimeTableListener;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/**
 * @author bryan
 *
 */
public class PlannerView extends JFrame implements TimeTableListener {

	private static final long serialVersionUID = 1L;
        
        //private TimetableTableModel timetableTableModel;
	//private TimetableTableModel preferencesTableModel;
        
	//private ObjectTimetableTableModel objectTimetableTableModel;
	//private DayShiftTimetableTableModel dayShiftTimetableTableModel;
        private EditableDayShiftTimetableTableModel usersAndPreferencesTableModel;
        
        //private List<LocationAwareTableModel> locationAwareTableModels;
        private List<ConfigurationTableModel> configurationTableModels;
        
        //private DualViewPanel dualViewPanel;
	
        private JButton next;
        
        private JButton print;
        private JButton save;
        private JButton exit;
                
        private JLabel statusLabel;
        private JCheckBox continiousBox;
        
        private LocationAwareTabView locationAwareTabView;
        private LocationAwareTabDrawView timetableDrawPanel;
    
	
	/**
	 * @param timetable 
	 * @param worker 
	 */
	public PlannerView(Timetable timetable){

                timetable.registerTimeTableListener(this);
                
                //ConfigurationEditor
                OrganisationalConfiguration configuration = Config.loadOrganisationalConfiguration();
                 Worker worker = timetable.getLoggedInWorker();
                 
               LocalDateTime myDateObj = LocalDateTime.now();
               DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
               String formattedDate = myDateObj.format(myFormatObj);
                 
		this.setTitle("Timetable ---  " + formattedDate);
                                this.setIconImage( new javax.swing.ImageIcon(getClass().getResource("/icons/calendar_empty.png")).getImage() );
		this.setLayout(new BorderLayout() );
		
                
                JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel actionPane = new JPanel(new BorderLayout());
                
                continiousBox = new JCheckBox( "", Boolean.TRUE);
                continiousBox.setSelectedIcon( new javax.swing.ImageIcon(getClass().getResource("/icons/next-stop-32.png")) );
                continiousBox.setToolTipText("Generate timetable");
                continiousBox.setIcon( new javax.swing.ImageIcon(getClass().getResource("/icons/next-continuous-32.png")) );
		continiousBox.setBorder( BorderFactory.createLineBorder(Color.DARK_GRAY, 2) );
                        
                next = new JButton("");
                next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play-32.png")));
                
                save = new JButton("");
                save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save-32.png")));
                save.setToolTipText("Save the current state.");
                
                print = new JButton("");
                print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/print-32.png")));
                print.setToolTipText("Print the current timetable.");
                print.setActionCommand(PrintStringConst.DISPLAY_PRINT_DIALOG);
                print.addActionListener( new PrintActionListener(this, configuration, timetable));
                
                exit = new JButton("");
                exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Exit.png")));
                exit.setToolTipText("Exit the PrintDialog.");
                
                
                JLabel logoLabel = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/icons/timetableLogo.png")));
                actionPane.add( getToolbar( worker.getRole() ), BorderLayout.CENTER );
		actionPane.add( logoLabel, BorderLayout.EAST );
                
		centerPanel.add(actionPane, BorderLayout.NORTH);
                
                //locationAwareTableModels = new ArrayList<LocationAwareTableModel>();
                configurationTableModels = new ArrayList<ConfigurationTableModel>();
 
                JTabbedPane tabbedPane = new JTabbedPane();
                
                locationAwareTabView = new LocationAwareTabView(configuration, timetable);
                tabbedPane.addTab("<html><h3>Timetable.</h3>Create/Consult Timetable.<html>", new javax.swing.ImageIcon(getClass().getResource("/icons/clock-icon.png")), locationAwareTabView, "Timetable");
                
                timetableDrawPanel = new LocationAwareTabDrawView(configuration, timetable);
                //tabbedPane.addTab("DRAWING.", new javax.swing.ImageIcon(getClass().getResource("/icons/clock-icon.png")), timetableDrawPanel, "PLAN");
                
                
                usersAndPreferencesTableModel = new EditableDayShiftTimetableTableModel(timetable.getTimetableState(), timetable.getTimetableState().getLocations().get(0));
                //usersAndPreferencesTableModel = new EditableDayShiftTimetableTableModel(timetable.getTimetableState(), configuration.getLocations().get(0));
                UsersAndPreferencesModel model = new UsersAndPreferencesModel(timetable);
                UsersAndPreferencesEditor usersAndPreferencesEditor = new UsersAndPreferencesEditor(model, usersAndPreferencesTableModel);
                usersAndPreferencesEditor.setTableModel(usersAndPreferencesTableModel);
                tabbedPane.addTab("<html><h3>Profile.</h3>Enter Preferences.<html>", new javax.swing.ImageIcon(getClass().getResource("/icons/user-32.png")), usersAndPreferencesEditor, "Workers/Preferences");
                
                /**
                 * Settings ***
                 */
                if( worker.getRole() == 2 ){
                    
                    JTabbedPane configTabbedPane = new JTabbedPane();
                    //for(Location location : configuration.getLocations()){
                    for(Location location : timetable.getTimetableState().getLocations()){
                        

                        ConfigurationTableModel configurationTableModel = new ConfigurationTableModel(configuration, location);
                        ConfigurationEditorView editorView = new ConfigurationEditorView(timetable, configurationTableModel);

                        configurationTableModels.add(configurationTableModel);
                        configTabbedPane.addTab( location.getName() , new javax.swing.ImageIcon(getClass().getResource("/icons/location-icon.png")), editorView, "Settings "+ location.getName() );
                    }
                    
                    tabbedPane.addTab("<html><h3>Settings.</h3>Global Settings.<html>", new javax.swing.ImageIcon(getClass().getResource("/icons/settings-icon.png")), configTabbedPane, "<html><h3>Settings.</h3>Globale Einstellungen.<html>");
                }
                
                //tabbedPane.addTab("Week-View - Worktimes.", new javax.swing.ImageIcon(getClass().getResource("/icons/tasks-icon.png")), dayHoursViewPanel, "Weekly");
                //tabbedPane.addTab("DualView.", new javax.swing.ImageIcon(getClass().getResource("/icons/dual-icon.png")), dualViewPanel, "DualView");
                
                
		centerPanel.add(tabbedPane, BorderLayout.CENTER);
		this.add(centerPanel, BorderLayout.CENTER);
		
		
		//JPanel actionPanel = new JPanel(new GridLayout(1,2));
		JPanel actionPanel = new JPanel(new BorderLayout());
                                
                statusLabel = new JLabel("Status:");
		statusLabel.setFont( new Font("Arial", Font.BOLD, 14) );
                
                //actionPanel.add(statusLabel);
		//actionPanel.add(next);
                actionPanel.add(statusLabel, BorderLayout.CENTER); 
                //actionPanel.add(next, BorderLayout.EAST);
                
		this.add(actionPanel, BorderLayout.SOUTH);
		
		this.setMinimumSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

        
        /**
         * 
         */
        private JPanel getToolbar(int role){
            
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            if( role == 2 ){
                toolbar.add(next);
                toolbar.add(continiousBox);
                toolbar.add(save);
            }
            
            toolbar.add(print);
            toolbar.add(exit);
                
            return toolbar;
        }
        
	/**
	 * @return the next
	 */
	public JButton getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(JButton next) {
		this.next = next;
	}
        
        /**
	 * @return the next
	 */
	public JButton getExit() {
		return exit;
	}
	
	/**
	 * @return the continiousBox
	 */
	public JCheckBox getContiniousBox() {
		return continiousBox;
	}

	/**
	 * @param continiousBox the continiousBox to set
	 */
	public void setContiniousBox(JCheckBox continiousBox) {
		this.continiousBox = continiousBox;
	}

        /**
         * 
         * @return 
         * /
        public ObjectTimetableTableModel getObjectTimetableTableModel() {
            return objectTimetableTableModel;
        }

        /**
         * 
         * @param objectTimetableTableModel 
         * /
        public void setObjectTimetableTableModel(ObjectTimetableTableModel objectTimetableTableModel) {
            this.objectTimetableTableModel = objectTimetableTableModel;
        }
        */
        
	/**
	 * @return the preferencesTableModel
	 * /
	public TimetableTableModel getPreferencesTableModel() {
		return preferencesTableModel;
	}

	/**
	 * @param preferencesTableModel the preferencesTableModel to set
	 * /
	public void setPreferencesTableModel(TimetableTableModel preferencesTableModel) {
		this.preferencesTableModel = preferencesTableModel;
	}

        public DayShiftTimetableTableModel getDayShiftTimetableTableModel() {
            return dayShiftTimetableTableModel;
        }

        public void setDayShiftTimetableTableModel(DayShiftTimetableTableModel dayShiftTimetableTableModel) {
            this.dayShiftTimetableTableModel = dayShiftTimetableTableModel;
        }
        * */

        public EditableDayShiftTimetableTableModel getUsersAndPreferencesTableModel() {
            return usersAndPreferencesTableModel;
        }

        public void setUsersAndPreferencesTableModel(EditableDayShiftTimetableTableModel usersAndPreferencesTableModel) {
            this.usersAndPreferencesTableModel = usersAndPreferencesTableModel;
        }
        
	

	@Override
	public void messageToDisplay(String message) {
            locationAwareTabView.messageToDisplay(message);
	}
        
        public void infoToDisplay(String message) {
            statusLabel.setText(message);
        }

	@Override
	public void permutationExecuted(String resumee) {
		
	}

	@Override
	public void stateToDisplay(TimetableState state) {
            //objectTimetableTableModel.setValueList(state);
            
            locationAwareTabView.stateToDisplay(state);
            usersAndPreferencesTableModel.setValueList(state);
            
            timetableDrawPanel.stateToDisplay(state);
            
            //dayShiftTimetableTableModel.setValueList(state);
            //dualViewPanel.stateToDisplay(state);
            
            getUsersAndPreferencesTableModel().setValueList( state );
            //getDayShiftTimetableTableModel().setValueList( state );
            getUsersAndPreferencesTableModel().setValueList( state );
        }

    public void statusToDisplay(String message) {
        
    }

    public void endStateReached(TimetableState state) {
        this.next.setEnabled(false);
    }

    public void workerLoggedIn(Worker worker) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    }
