package model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import java.util.Random;

public class Agent {
    private static final double RANDOM_MOVE_CHANCE = 0.3;

    private QMatrix qMatrix;
    private int currentState;

    private Random random;

    public Agent(QMatrix qMatrix) {
        this.qMatrix = qMatrix;
        this.currentState = 1;
        random = new Random();
    }

    public Action selectAction() {
        if(random.nextDouble() < RANDOM_MOVE_CHANCE) {
            // Move randomly
            return Action.getAction(Action.NUM_ACTIONS);
        } else {
            // Pick highest reward move
            Action selectedAction = Action.UP;
            double selectedReward = -Double.MAX_VALUE;
            for (int i = 0; i < Action.NUM_ACTIONS; i++) {
                Action consideredAction = Action.getAction(i);
                double consideredReward = qMatrix.getReward(currentState, consideredAction);
                if(i == 0 || consideredReward > selectedReward) {
                    selectedAction = consideredAction;
                    selectedReward = consideredReward;
                }
            }
            return selectedAction;
        }
    }
}
