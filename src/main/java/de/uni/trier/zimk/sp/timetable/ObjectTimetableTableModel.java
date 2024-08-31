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
public class ObjectTimetableTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	
	private String [] headers;
	private TimetableState state;
	
	
	public ObjectTimetableTableModel(String [] headers){
		this.headers = headers;		
	}
	
	public ObjectTimetableTableModel(String [] headers, TimetableState state){
		this.headers = headers;
		this.state = state;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
            if( state == null ){
                return 0;
            }
            return state.getWorkers().size();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
        return headers[col];
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return headers.length;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {
            if(columnIndex == 0){
                return String.class;
            }
            else {
                return UserShiftBlock.class;
            }
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
            if( columnIndex == 0 ){
                return state.getWorkers().get(rowIndex).toMoreDetailsString();
            }
            else {
                return state.getWorktimeInformation(rowIndex, columnIndex);
            }
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex) {
		return state.getWorkers().get(rowIndex);
	}
	
	/**
	 * 
	 * @param shiftsValues
	 */
	public void setValueList(TimetableState state) {
		this.state = state;
		this.fireTableDataChanged();
	}

}
