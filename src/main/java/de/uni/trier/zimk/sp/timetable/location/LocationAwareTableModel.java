/**
 * 
 */
package de.uni.trier.zimk.sp.timetable.location;

import de.uni.trier.zimk.sp.timetable.oo.Location;
import de.uni.trier.zimk.sp.timetable.oo.LocationShift;
import de.uni.trier.zimk.sp.timetable.oo.OrganisationalConfiguration;
import de.uni.trier.zimk.sp.timetable.oo.Workday;
import javax.swing.table.AbstractTableModel;

/**
 * @author landry.ngani
 *
 */
public class LocationAwareTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
        
        
	private OrganisationalConfiguration configuration;
	private Location location;
        

	public LocationAwareTableModel(OrganisationalConfiguration configuration, Location location){
            this.configuration = configuration;
            this.location = location;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
            if( configuration == null ){
                return 0;
            }
            return location.getMaxShiftsLength(configuration);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
            if( configuration != null ){
                return configuration.getWorkdays().get(col).getName();
            }
           
            return "";
        }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
            if( configuration == null ){
                return 0;
            }
            return configuration.getWorkdays().size();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {     
            return LocationShift.class;
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

            Workday workday = configuration.getWorkdays().get(columnIndex);
            LocationShift shift = location.getShift(workday, rowIndex);
            
            return shift;
	}

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            LocationShift shift = (LocationShift)aValue;
            location.setShift(columnIndex, rowIndex, shift);

            this.fireTableDataChanged();
        }
    
	/**
	 * 
	 * @param shiftsValues
	 */
	public void setValueList(Location location) {
            this.location = location;
            this.fireTableDataChanged();
	}
        
        /**
	 * 
	 * @param shiftsValues
	 */
	public Location getValueList() {
            return this.location;
	}

}
