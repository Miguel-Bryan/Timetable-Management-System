/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import java.util.Comparator;

/**
 *
 * @author me
 */
public class WorkerComparable implements Comparator<Worker> {

    public int compare(Worker worker1, Worker worker2) {
        return worker1.getName().compareTo( worker2.getName() );
    }
    
}
