/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.Color;

/**
 *
 * @author Landry Ngani
 */
public class WorkerColor {
    
    private int redValue, greenValue, blueValue;

    public WorkerColor() {
    }

    
    public WorkerColor(int redValue, int greenValue, int blueValue){
        this.redValue = redValue;
        this.greenValue = greenValue;
        this.blueValue = blueValue;
    }
    
    @JsonIgnore
    public Color getColor() {
        return new Color(redValue, greenValue, blueValue);
    }


    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    
    
}
