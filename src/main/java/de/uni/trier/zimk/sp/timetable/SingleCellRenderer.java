/**
 * 
 */
package de.uni.trier.zimk.sp.timetable;

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

/**
 * @author landry.ngani
 *
 */
public class SingleCellRenderer implements TableCellRenderer {

	private Timetable timetable;
	
	public SingleCellRenderer(Timetable timetable, int x){
		this.timetable = timetable;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		String stringValue = (String)value;
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setToolTipText( stringValue );
		
		panel.setMinimumSize(new Dimension(100,100));
		JTextArea label = new JTextArea(stringValue);
		label.setEditable(false);  
		label.setCursor(null);  
		label.setOpaque(false);  
		label.setFocusable(false); 
		
		label.setWrapStyleWord(true);  
		label.setLineWrap(true); 
		
		label.setFont( new Font("Arial", Font.PLAIN, 11) );
		panel.add(label, BorderLayout.CENTER);
		
                //TODO
                /*
		// Background & Foreground 
		if( row%2 == 0 ){
			panel.setBackground(isSelected ? 
					(!timetable.getTimetableState().dayInPreference(isSelected, row, column) ? 
							(!timetable.getTimetableState().shiftInPreference(isSelected, row, column) ? Color.ORANGE : Color.RED ) : new Color(230,238,245)) : Color.WHITE );
		}
		else {
			panel.setBackground(isSelected ? 
					(!timetable.getTimetableState().dayInPreference(isSelected, row, column) ? 
							(!timetable.getTimetableState().shiftInPreference(isSelected, row, column) ? Color.ORANGE : Color.RED ) : new Color(230,238,245)) : new Color(212, 218, 222) );
		}
                */
		
		/*
		if( ! timetable.getTimetableState().dayInPreference(isSelected, row, column) ){
			panel.setBackground(isSelected ? Color.RED : Color.ORANGE );
		}
		else {
			if( timetable.getTimetableState().shiftInPreference(isSelected, row, column) ){
				panel.setBackground(isSelected ? Color.ORANGE : Color.RED );
			}
		}
		*/
				
		return panel;
	}

	

}
