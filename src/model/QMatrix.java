package model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

/*
 *---------------------------------------------------------------
 * The bottom-right cell/state has state index = 1
 * The top-left end cell/state has state index = nValue^2 = numStates
 *
 *                                      y:
 *    |----|----|----|----|-----------|
 *    | n*n|....|....|....|(n*(n-1))+1| n-1
 *    |----|----|----|----|-----------|
 *    |....|....|....|....|...........| ...
 *    |----|----|----|----|-----------|
 *    | 3n |....|....|....|   2n+1    |  2
 *    |----|----|----|----|-----------|
 *    | 2n |2n-1|....| n+2|    n+1    |  1
 *    |----|----|----|----|-----------|
 *    | 1n | n-1|....|  2 |      1    |  0
 *    |----|----|----|----|-----------|
 *  x: n-1  n-2  ...   1        0
 *
 *
 * The state-index matrix has:
 *   - Row index = state number
 *   - Column index = action number
 *---------------------------------------------------------------
 */

import global.Action;

public class QMatrix {
    private int nValue;
    private int numStates;
    private double[][] actionValueMatrix;

    public QMatrix(int nValue) {
        this.nValue = nValue;
        this.numStates = (int) Math.pow(nValue, 2);
        this.actionValueMatrix = new double[numStates][Action.values().length];
        setForbiddenDirectionRewards(nValue);
    }

    private void setForbiddenDirectionRewards(int nValue) {
        for (int i = 1; i <= nValue; i++) {
            // Going down on bottommost cells
            setReward(i, Action.DOWN, Double.MIN_VALUE);
            // Going left on leftmost cells
            setReward(i * nValue, Action.LEFT, Double.MIN_VALUE);
            // Going right on rightmost cells
            setReward(((i - 1) * nValue) + 1, Action.RIGHT, Double.MIN_VALUE);
            // Going up on upmost cells
            setReward((nValue * (nValue - 1)) + i, Action.UP, Double.MIN_VALUE);
        }
    }

    public double getReward(int state, Action action) {
        return actionValueMatrix[state][action.index];
    }

    public void setReward(int xCoor, int yCoor, Action action, double reward) {
        // Check if the cell is in bounds
        if ((xCoor >= 0 && xCoor < nValue) && (yCoor >= 0 && yCoor < nValue) ) {
            setReward(toStateIndex(xCoor, yCoor), action, reward);
        }
    }

    public void setReward(int state, Action action, double reward) {
        // -1 to convert from state index to array index.
        actionValueMatrix[state - 1][action.index] = reward;
    }

    public int toStateIndex(int xCoor, int yCoor) {
        return xCoor + (yCoor * nValue) + 1;
    }
}
