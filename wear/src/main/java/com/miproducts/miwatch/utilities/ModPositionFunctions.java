package com.miproducts.miwatch.utilities;

/**
 * Use these functions to get default position for the PickingMod and others
 * This will allow uys to keep the positions accurate. Whether we need them for Config or for Watchface
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


    // atm for regular mods, not config mods
    static public int getLeftDatePosition(int canvasWidth){
        return canvasWidth / 5;
    }

    static public int getTopDateConfigPosition(int canvasHeight){
        return (int) (canvasHeight * .18);
    }

    static public int getLeftDateConfigPosition(int canvasWidth){
        return canvasWidth/7;
    }

    static public float getTopEventConfigPosition(int canvasHeight){
        return (float) (canvasHeight * .40);
    }

    static public float getLeftEventConfigPosition(int canvasWidth){
        return (float) canvasWidth/10;
    }

    /**
     * APPLY THE OTHER 3 MODS TO THESE AND GET THEM SET
     * // atm for regular mods, not config mods
     static public int getLeftDatePosition(int canvasWidth){
     return canvasWidth / 5;
     }

     static public int getTopDateConfigPosition(int canvasHeight){
     return (int) (canvasHeight * .18);
     }

     static public int getLeftDateConfigPosition(int canvasWidth){
     return canvasWidth/7;
     }

     static public float getTopEventConfigPosition(int canvasHeight){
     return (float) (canvasHeight * .40);
     }

     static public float getLeftEventConfigPosition(int canvasWidth){
     return (float) canvasWidth/10;
     }
     *
     *
     *
     */

}
