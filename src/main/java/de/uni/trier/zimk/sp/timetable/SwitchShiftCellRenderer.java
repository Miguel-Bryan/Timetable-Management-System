/**
 * 
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.oo.Shift;
import de.uni.trier.zimk.sp.timetable.oo.SwitchShift;
import de.uni.trier.zimk.sp.timetable.oo.UserShiftBlock;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.awt.*;
import javax.swing.JLabel;

/**
 * @author landry.ngani
 *
 */
public class SwitchShiftCellRenderer implements TableCellRenderer {

	private Timetable timetable;
	
	public SwitchShiftCellRenderer(Timetable timetable){
		this.timetable = timetable;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		UserShiftBlock block = (UserShiftBlock)value;
		JPanel panel = new JPanel(new BorderLayout());
                
                /*
		panel.setToolTipText( block.toString() );
		
		panel.setMinimumSize(new Dimension(100,100));
		
                JPanel container = new JPanel();
                container.setLayout( new FlowLayout(FlowLayout.LEFT) );
                for( Shift shift : block.getShifts() ){
                    JPanel ipanel = new JPanel();
                    JLabel ilabel = new JLabel( shift.toStringInTimetable() );
                    ipanel.add(ilabel);
                    
                    container.add(ipanel);
                    if(shift.isChanged() ){
                        ipanel.setBackground(Color.GRAY);
                    }
                    else{
                        ipanel.setBackground( ipanel.getParent().getBackground() );
                    }
                }
                panel.add(container, BorderLayout.CENTER);
                container.setBackground( container.getParent().getBackground() );
                
                JLabel statusLabel = new JLabel();
                if(block.isLastModified()){
                    statusLabel.setText("");
                    statusLabel.setFont( new Font("Arial", Font.PLAIN, 18) );
                    statusLabel.setOpaque(false);  
                    statusLabel.setBackground(Color.BLUE);
                    statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/modified_icon.png")));
                }
                panel.add( statusLabel, BorderLayout.EAST );
                */
                
                
		// Background & Foreground 
		if( row%2 == 0 ){
                    
                    if(block.getShifts().isEmpty()){
                        panel.setBackground(Color.WHITE);
                    }
                    else {
                        if(block.unMatchingDayPrefences()){
                            panel.setBackground(Color.RED);
                        }
                        else {
                            if(block.unMatchingShiftPrefences()){
                                panel.setBackground(Color.ORANGE);
                            }
                            else {
                                panel.setBackground(Color.WHITE);
                            }
                        }
                    }
                    /*
                    panel.setBackground(isSelected ? 
                                ( !(block.matchingDayPrefences()) ? ( !(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED ) : new Color(230,238,245) ) : Color.WHITE );
                    */
                    
                }
		else {
                    
                    if(block.getShifts().isEmpty()){
                        panel.setBackground(new Color(212, 218, 222));
                    }
                    else {
                       if(block.unMatchingDayPrefences()){
                            panel.setBackground(Color.RED);
                        }
                        else {
                            if(block.unMatchingShiftPrefences()){
                                panel.setBackground(Color.ORANGE);
                            }
                            else {
                                panel.setBackground( new Color(212, 218, 222) );
                            }
                        } 
                    }
                        /*
			panel.setBackground(isSelected ? 
					( !(block.matchingDayPrefences()) ? (!(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED ) : new Color(230,238,245)) : new Color(212, 218, 222) );
                        */
                }
		
		panel.setToolTipText( block.toString() );
		
		panel.setMinimumSize(new Dimension(100,100));
		
                /*
                JTextArea label = new JTextArea(block.toString());
		label.setEditable(false);  
		label.setCursor(null);  
		label.setOpaque(false);  
		label.setFocusable(false); 
		
		label.setWrapStyleWord(true);  
		label.setLineWrap(true); 
		
		label.setFont( new Font("Arial", Font.PLAIN, 11) );
		panel.add(label, BorderLayout.CENTER);
                */
		
                JPanel container = new JPanel();
                container.setLayout( new FlowLayout(FlowLayout.LEFT) );
                for( LocationShift shift : block.getShifts() ){
                    JPanel ipanel = new JPanel();
                    JLabel ilabel = new JLabel( shift.toStringInTimetable() );
                    ipanel.add(ilabel);
                    
                    container.add(ipanel);
                    if(shift.isChanged() ){
                        ipanel.setBackground(Color.GRAY);
                    }
                    else{
                        ipanel.setBackground( ipanel.getParent().getBackground() );
                    }
                }
                panel.add(container, BorderLayout.CENTER);
                container.setBackground( container.getParent().getBackground() );
                
                JLabel statusLabel = new JLabel();
                if(block.isLastModified()){
                    statusLabel.setText("");
                    statusLabel.setFont( new Font("Arial", Font.PLAIN, 18) );
                    statusLabel.setOpaque(false);  
                    statusLabel.setBackground(Color.BLUE);
                    statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/modified_icon.png")));
                }
                panel.add( statusLabel, BorderLayout.EAST );
                
                
		return panel;
	}

	

}
