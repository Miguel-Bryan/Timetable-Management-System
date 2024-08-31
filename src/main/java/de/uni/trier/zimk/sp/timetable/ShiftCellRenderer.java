/**
 *
 */
package de.uni.trier.zimk.sp.timetable;

import de.uni.trier.zimk.sp.timetable.oo.*;
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
public class ShiftCellRenderer implements TableCellRenderer {

    /*
     * private Timetable timetable;
     *
     * public ShiftCellRenderer(Timetable timetable){ this.timetable =
     * timetable; }
     */
    public ShiftCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        LocationShift shift = (LocationShift) value;
        JPanel panel = new JPanel(new BorderLayout());

        if (shift != null) {
            // Background & Foreground 
            if (row % 2 == 0) {

                if (shift.getWorkers().isEmpty()) {
                    panel.setBackground(Color.WHITE);
                } else {
                    
                    
                    if (! shift.isMatchingDayPrefences()) {
                        panel.setBackground(Color.RED);
                    } else {
                        if ( ! shift.isMatchingShiftPrefences()) {
                            panel.setBackground(Color.ORANGE);
                        } else {
                            panel.setBackground(Color.WHITE);
                        }
                    }
                }
                /*
                 * panel.setBackground(isSelected ? (
                 * !(block.matchingDayPrefences()) ? (
                 * !(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED
                 * ) : new Color(230,238,245) ) : Color.WHITE );
                 */

            } else {

                if (shift.getWorkers().isEmpty()) {
                    panel.setBackground(new Color(212, 218, 222));
                } else {
                    if (! shift.isMatchingDayPrefences()) {
                        panel.setBackground(Color.RED);
                    } else {
                        if (! shift.isMatchingShiftPrefences()) {
                            panel.setBackground(Color.ORANGE);
                        } else {
                            panel.setBackground(new Color(212, 218, 222));
                        }
                    }
                }
                /*
                 * panel.setBackground(isSelected ? (
                 * !(block.matchingDayPrefences()) ?
                 * (!(block.matchingShiftPrefences()) ? Color.ORANGE : Color.RED
                 * ) : new Color(230,238,245)) : new Color(212, 218, 222) );
                 */
            }

            panel.setToolTipText(shift.toString());

            panel.setMinimumSize(new Dimension(100, 100));

            JPanel container = new JPanel();
            container.setLayout(new FlowLayout(FlowLayout.LEFT));
            for (Worker worker : shift.getWorkers()) {
                JPanel ipanel = new JPanel();
                JLabel ilabel = new JLabel(worker.getName());
                ipanel.add(ilabel);

                container.add(ipanel);
                if (!worker.isWillingForShift(shift)) {
                    ipanel.setBackground(Color.GRAY);
                } else {
                    ipanel.setBackground(ipanel.getParent().getBackground());
                }
            }
            panel.add(container, BorderLayout.CENTER);
            container.setBackground(container.getParent().getBackground());

            JLabel statusLabel = new JLabel();
            if (shift.isChanged()) {
                statusLabel.setText("");
                statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                statusLabel.setOpaque(false);
                statusLabel.setBackground(Color.BLUE);
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/modified_icon.png")));
            }
            panel.add(statusLabel, BorderLayout.EAST);
        }


        return panel;
    }
}
