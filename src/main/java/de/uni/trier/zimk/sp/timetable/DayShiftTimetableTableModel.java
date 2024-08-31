/**
 * 
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.oo.*;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author landry.ngani
 *
 */
public class DayShiftTimetableTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	
	private String [] headers;
	private TimetableState state;
        private Location location;
	

	public DayShiftTimetableTableModel(TimetableState state, Location location){
		this.state = state;
                this.location = location;
	}

        /*
        public DayShiftTimetableTableModel(String[] headers) {
            this.headers = headers;
        }
        */
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
            if( state == null ){
                return 0;
            }
            //return state.getShiftRowLength();
            return location.getMaxShiftsLength(state.getOrganisationalConfiguration());
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
            if( state != null ){
                return state.getWorkdays().get(col).getName();
            }
            else {
                return headers[col];
            }
        }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
            if( state == null ){
                return headers.length;
            }
            return state.getWorkdays().size();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {
            /*
            if(columnIndex == 0){
                return String.class;
            }
            else {
                return Shift.class;
            }
            */
                    
            return Shift.class;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int columnIndex) {
    	return false;
    }

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {    
            /*
            if( columnIndex == 0 ){
                return state.getWorkers().get(rowIndex).toMoreDetailsString();
            }
            else {
                return state.getShiftInformation(rowIndex, columnIndex);
            }
            */
            
            return state.getShiftInformation(location,rowIndex, columnIndex);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 * /
	public Object getValueAt(int rowIndex) {
		return state.getWorkers().get(rowIndex);
	}
        */
	
	/**
	 * 
	 * @param shiftsValues
	 */
	public void setValueList(TimetableState state) {
		this.state = state;
		this.fireTableDataChanged();
	}

}
