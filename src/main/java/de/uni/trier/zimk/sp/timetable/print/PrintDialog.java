/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.print;

import de.uni.trier.zimk.sp.timetable.oo.OrganisationalConfiguration;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import de.uni.trier.zimk.sp.timetable.views.PlannerView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Landry Ngani
 */
class PrintDialog extends JDialog {
    

    private LocationPrintTabView locationPrintTabView;
    
    /**
     * 
     * @param plannerView
     * @param configuration
     * @param timetable 
     */
    public PrintDialog(PlannerView plannerView, OrganisationalConfiguration configuration, Timetable timetable) {
        super(plannerView, true);
        
        this.setLayout( new BorderLayout() );
        this.setMinimumSize(new Dimension( 1200, 835));
        this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton print = new JButton("");
        print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/print-32.png")));
        print.setToolTipText("Print the current timetable.");
        print.setActionCommand(PrintStringConst.PRINT_TIMETABLE);
        print.addActionListener( new PrintActionListener(plannerView, configuration, timetable) );
        
//        JButton save = new JButton("");
//        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save-32.png")));
//        save.setToolTipText("Save the current state.");
//        save.setActionCommand(PrintStringConst.SAVE_STATE);
//        save.addActionListener(new SaveStateActionListener(this, plannerView,configuration,timetable));
        
        JButton exit = new JButton("");
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Exit.png")));
        exit.setToolTipText("Exit the PrintDialog.");
        exit.setActionCommand(PrintStringConst.EXIT_PRINT_DIALOG);
        exit.addActionListener( new PrintActionListener(plannerView, configuration, timetable) );
        
        toolbar.add(print);
        //toolbar.add(save);
        toolbar.add(exit);
        
        this.add( toolbar, BorderLayout.NORTH );
        
        locationPrintTabView = new LocationPrintTabView(configuration, timetable);
        this.add( locationPrintTabView, BorderLayout.CENTER );
        
    }

    public LocationPrintTabView getLocationPrintTabView() {
        return locationPrintTabView;
    }

    public void setLocationPrintTabView(LocationPrintTabView locationPrintTabView) {
        this.locationPrintTabView = locationPrintTabView;
    }
    
    
    
}
