/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.preferences;

import de.uni.trier.zimk.sp.timetable.oo.*;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author landry.ngani
 *
 */
public class EditableDayShiftTimetableTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	
	private String [] headers;
	private TimetableState state;
        private Location location;
	

	public EditableDayShiftTimetableTableModel(TimetableState state, Location location){
		this.state = state;
                this.location = location;
	}

        /*
        public EditableDayShiftTimetableTableModel(String[] headers) {
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
            return state.getShiftInformation(location, rowIndex, columnIndex);
	}

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        LocationShift shift = (LocationShift)aValue;
        state.setShiftInformation(location,shift, rowIndex, columnIndex);

        this.fireTableDataChanged();
    }
    
    
	/**
	 * 
	 * @param shiftsValues
	 */
	public void setValueList(TimetableState state) {
		this.state = state;
		this.fireTableDataChanged();
	}
        
        /**
	 * 
	 * @param shiftsValues
	 */
	public TimetableState getValueList() {
            return this.state;
	}

}
