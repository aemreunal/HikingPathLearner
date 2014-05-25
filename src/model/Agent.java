package model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import java.util.Random;

public class Agent {
    public static final double RANDOM_MOVE_CHANCE = 0.3;

    private final int nValue;
    private QMatrix qMatrix;
    private int currentState = 1;
    private int previousState = -1;

    private Random random;

    public Agent(int nValue, QMatrix qMatrix) {
        this.nValue = nValue;
        this.qMatrix = qMatrix;
        random = new Random();
    }

    public Action selectAction() {
        if(random.nextDouble() < RANDOM_MOVE_CHANCE) {
            // Move randomly
            return pickRandomAction();
        } else {
            // Pick highest reward move
            return pickMostRewardingAction();
        }
    }

    private Action pickRandomAction() {
        Action selectedAction;
        do {
            selectedAction = Action.getAction(random.nextInt(Action.NUM_ACTIONS));
        } while (!canExecuteAction(selectedAction));
        return selectedAction;
    }

    public Action pickMostRewardingAction() {
        Action selectedAction = Action.UP;
        double selectedReward = -Double.MAX_VALUE;

        for (int i = 0; i < Action.NUM_ACTIONS; i++) {
            Action consideredAction = Action.getAction(i);
            double consideredReward = qMatrix.getQValue(currentState, consideredAction);
            if(i == 0 || (consideredReward > selectedReward && canExecuteAction(consideredAction))) {
                selectedAction = consideredAction;
                selectedReward = consideredReward;
            }
        }
        return selectedAction;
    }

    public void executeAction(Action action) {
        switch (action) {
            case RIGHT:
                setState(currentState - 1);
                break;
            case LEFT:
                setState(currentState + 1);
                break;
            case UP:
                setState(currentState + nValue);
                break;
            case DOWN:
                setState(currentState - nValue);
                break;
            default:
                break;
        }
    }

    public boolean canExecuteAction(Action action) {
        switch (action) {
            case RIGHT:
                return (currentState % nValue) > 1;
            case LEFT:
                return (currentState % nValue) != 0;
            case UP:
                return currentState <= nValue * (nValue - 1);
            case DOWN:
                return currentState > nValue;
            default:
                return false;
        }
    }

    private void setState(int newState) {
        previousState = currentState;
        currentState = newState;
    }

    public int getCurrentState() {
        return currentState;
    }

    public int getPreviousState() {
        return previousState;
    }
}
