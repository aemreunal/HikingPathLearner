package model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

/*
 *---------------------------------------------------------------
 * The bottom-right cell/state has number = 1
 * The top-left end cell/state has number = numStates^2
 *
 *  |----|----|----|----|---------|
 *  | n*n|....|....|....|(n*(n-1))+1|
 *  |----|----|----|----|---------|
 *  |....|....|....|....|.........|
 *  |----|----|----|----|---------|
 *  | 3n |....|....|....|   2n+1  |
 *  |----|----|----|----|---------|
 *  | 2n |....|....| n+2|    n+1  |
 *  |----|----|----|----|---------|
 *  | 1n |....|....|  2 |      1  |
 *  |----|----|----|----|---------|
 *
 *
 * The state-value matrix has:
 *   - Row index = state number
 *   - Column index = action number
 *
 * Action number to movement direction mapping:
 *   - 1 = Right
 *   - 2 = Left
 *   - 3 = Up
 *   - 4 = Down
 *---------------------------------------------------------------
 */

import global.Actions;

public class QMatrix {
    private int numStates;
    private double[][] stateValueMatrix;

    public QMatrix(int numStates) {
        this.numStates = numStates;
        this.stateValueMatrix = new double[numStates][Actions.values().length];
    }
}
