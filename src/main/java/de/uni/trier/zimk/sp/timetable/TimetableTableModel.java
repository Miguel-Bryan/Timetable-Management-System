/**
 * 
 */
package de.uni.trier.zimk.sp.timetable;

import javax.swing.table.AbstractTableModel;

/**
 * @author landry.ngani
 *
 */
public class TimetableTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	
	private String [] headers;
	private String[][] shiftsValues;
	
	
	public TimetableTableModel(String [] headers){
		this.headers = headers;
		this.shiftsValues = new String[headers.length][0];
		
		for(int i = 0; i < headers.length; i++){
			for(int j = 0; j < shiftsValues[i].length; j++){
				this.shiftsValues[i][j] = "";	
			}
		}
		
	}
	
	public TimetableTableModel(String [] headers, String[][] shiftsValues){
		this.headers = headers;
		this.shiftsValues = shiftsValues;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if( this.shiftsValues[0] == null ){
			return 0;
		}
		return shiftsValues.length;
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
	public Class getColumnClass(int c) {
		return String.class;
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
		try {
			return shiftsValues[rowIndex][columnIndex];
		}
		catch(Exception e){
			return null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex) {
		return shiftsValues[rowIndex];
	}
	
	public void setValueAt(Object value, int rowIndex, int columnIndex){
		shiftsValues[rowIndex][columnIndex] = value.toString();
		this.fireTableDataChanged();
	}
	
	/**
	 * 
	 * @param shiftsValues
	 */
	public void setValueList(String[][] shiftsValues) {
		this.shiftsValues = shiftsValues;
		this.fireTableDataChanged();
	}

}
