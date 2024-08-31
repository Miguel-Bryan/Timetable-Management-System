/**
 *
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.views.PlannerView;
import de.uni.trier.zimk.sp.timetable.oo.*;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import de.uni.trier.zimk.sp.timetable.views.LoginDialog;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author landry.ngani
 *
 */
public class Planner {

    private static Logger logger = Logger.getLogger(Planner.class);
    private Timetable timetable;

    // private List<Timetable> timetables;
    private LoginDialog loginView;
    private PlannerView plannerView;

    /**
     *
     */
    public Planner() {

        //Config.saveSampleWorkers();
        //System.out.println("Workers done");
        //Config.saveSampleConfig();
        //System.out.println("Org Config done");

        OrganisationalConfiguration orgConfiguration = Config.loadOrganisationalConfiguration();
        List<Workday> workdays = orgConfiguration.getWorkdays();

        // WorkerConfiguration workerConfiguration = Config.loadWorkers();
        List<Worker> workers = orgConfiguration.getWorkers();

        // this.timetable = new Timetable(workdays, workers);
        // this.timetable = new Timetable(orgConfiguration, workerConfiguration);
        this.timetable = new Timetable(orgConfiguration);

        loginView = new LoginDialog(plannerView, this.timetable);
        loginView.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        loginView.display();

        plannerView = new PlannerView(this.timetable);

    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable base) {
        this.timetable = base;
    }

    /**
     * @return the plannerView
     */
    public PlannerView getPlannerView() {
        return plannerView;
    }

    /**
     * @param plannerView the plannerView to set
     */
    public void setPlannerView(PlannerView plannerView) {
        this.plannerView = plannerView;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        logger.info(Planner.class.getSimpleName() + " -- > Timetable - @2024");

        // Config.saveSampleWorkers();

        Planner planner = new Planner();
        new PlannerActionListener(planner, planner.getTimetable());

        logger.info(planner.getTimetable().getTimetableState().getWorkdays().size() + " Workdays");
        logger.info(planner.getTimetable().getTimetableState().getWorkers().size() + " Workers");

        /*
         * logger.info( "\n*********** INITIAL RANDOM *************" +
         * "\n"+planner.getTimetable().toStringWithShifts()+
         * "\n*************************************");
         */
        planner.loadData();

        /*
         * Initialize a first possible solution
         */
        planner.getTimetable().initStartSolution();
        logger.info("StartSolution initialized.");
       
        /*
         * logger.info( "\n*********** INITIAL SOLUTION *************" +
         * "\n"+planner.getTimetable().toStringWithShifts()+
         * "\n*************************************");
         */
        planner.loadData();

        /*
         * Generate time table
         */
        logger.info("Ready for generation of timetable.");
        planner.getTimetable().generateTimeTable();
        logger.info("Timetable generated.");

    }

    /**
     * Load state's data in the tables
     */
    public void loadData() {
        if (this.getTimetable().getTimetableState() != null) {
            getPlannerView().stateToDisplay(this.getTimetable().getTimetableState());
        }
    }

    public void loadPrefrences() {
        getPlannerView().stateToDisplay(this.getTimetable().getTimetableState());
    }

    public void loadDatas(String[][] rowData) {
        // getPlannerView().getTimetableTableModel().setValueList(rowData);
        // getPlannerView().getObjectTimetableTableModel().setValueList(this.getTimetable().getTimetableState());

        getPlannerView().getUsersAndPreferencesTableModel().setValueList(this.getTimetable().getTimetableState());
        // getPlannerView().getDayShiftTimetableTableModel().setValueList(this.getTimetable().getTimetableState());

    }

    public void shutdown() {
        logger.info("Shutting down the system.");
        logger.info("Good bye.");
        System.exit(0);
    }

}
