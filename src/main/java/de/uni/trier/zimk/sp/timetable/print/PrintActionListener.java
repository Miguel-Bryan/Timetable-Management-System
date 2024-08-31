/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import de.uni.trier.zimk.sp.timetable.oo.OrganisationalConfiguration;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import de.uni.trier.zimk.sp.timetable.views.PlannerView;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import javax.swing.JTabbedPane;
/**
 *
 * @author Landry Ngani
 */
public class PrintActionListener implements ActionListener {
    
    
    private PlannerView plannerView;
    private OrganisationalConfiguration configuration;
    private Timetable timetable;
    
    private static PrintDialog printDialog;
    
    /**
     * 
     * @param plannerView
     * @param configuration
     * @param timetable 
     */
    public PrintActionListener(PlannerView plannerView, OrganisationalConfiguration configuration, Timetable timetable) {
        this.plannerView = plannerView;
        this.configuration =  configuration;
        this.timetable = timetable;
    }
    
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equalsIgnoreCase(PrintStringConst.DISPLAY_PRINT_DIALOG)){
            
            //if(printDialog == null){
                printDialog = new PrintDialog(plannerView, configuration, timetable);
            //}
            printDialog.setVisible(true);
            
        }
        else {
            if(e.getActionCommand().equalsIgnoreCase(PrintStringConst.PRINT_TIMETABLE)){
                
                JTabbedPane tabbedPane = printDialog.getLocationPrintTabView().getTabbedPane();
                for(Component component : tabbedPane.getComponents() ){
                    if( component instanceof LocationPrintTimetableView){
                        
                        LocationPrintTimetableView panelToPrint = ((LocationPrintTimetableView)component);
                        
                           //Print the panel to PDF
                        Document document = new Document(PageSize.A4.rotate()); 
                        
                        try {
                            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home")+ 
                                    File.separator + panelToPrint.getLocationAwarePrintTableModel().getValueList().getName()+".pdf"));
                            
                            System.out.println("Printing document : "+ System.getProperty("user.home")+ 
                                    File.separator + panelToPrint.getLocationAwarePrintTableModel().getValueList().getName()+".pdf");
                            
                            document.open();
                            PdfContentByte contentByte = writer.getDirectContent();
                            PdfTemplate template = contentByte.createTemplate(panelToPrint.getWidth(), panelToPrint.getHeight());
                            Graphics2D g2 = template.createGraphics(panelToPrint.getWidth(), panelToPrint.getHeight());
                            g2.scale(0.70, 0.80);
                            panelToPrint.print(g2);
                            g2.dispose();
                            contentByte.addTemplate(template, 5, -120);
                            
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                        finally{
                                
                            if(document.isOpen()){
                                document.close();
                            }
                        }
                        
                    }
                }
            }
            else {
                if(e.getActionCommand().equalsIgnoreCase(PrintStringConst.EXIT_PRINT_DIALOG)){
                    printDialog.setVisible(false);
                    //printDialog.dispose();
                }
            }
        }
        
    }
    
    class PanelCreation {
        private int width;
        private int height;

        public PanelCreation (int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
        
        
        
    }
}
