package com.miproducts.miwatch.utilities;

/**
 * Use these functions to get default position for the PickingMod and others
 * Created by ladam_000 on 7/19/2015.
 */
public class ModPositionFunctions {

    /**
     * Get the left side of the Timer
     * @param canvasWidth - width of the canvas.
     * @return position Timer should be
     */
   static public int getLeftTimerPosition(int canvasWidth){
        int xPosition = (canvasWidth / 2) - (canvasWidth / 10);
        return xPosition;
    }

    /**
     * essentially the same thing as @getLeftTimerPosition
     * @param canvasHeight
     * @return
     */
    static public int getTopTimerPosition(int canvasHeight){
        int yPosition = (int)(canvasHeight * .33);
        return yPosition;
    }

    static public int getLeftDatePosition(int canvasWidth){
        return canvasWidth / 5;
    }

    static public int getTopDatePosition(int canvasHeight){
        return (int) (canvasHeight * .20);
    }
}
