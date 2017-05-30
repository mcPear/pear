package com.lab.gruszczynski.pear;

/**
 * Created by maciej on 30.05.17.
 */
public class GameLevel {
    final private double arrowStepRatio; //1 is default step

    public GameLevel(double arrowStepRatio) {
        this.arrowStepRatio = arrowStepRatio;
    }

    public double getArrowStepRatio() {
        return arrowStepRatio;
    }
}
