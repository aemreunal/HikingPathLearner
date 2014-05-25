package controller;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import model.Action;
import model.Agent;
import model.Maze;
import model.QMatrix;
import view.MapWindow;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Controller extends Thread implements Runnable {
    private static final double LEARNING_RATE = 0.8;
    private static final double DISCOUNT_FACTOR = 0.8;
    private static final double UPDATE_FREQUENCY_HZ = 50;
    private static final long THREAD_SLEEP_TIME_MILLIS = (long) (1000 / UPDATE_FREQUENCY_HZ);

    private QMatrix qMatrix;
    private Maze maze;
    private MapWindow window;

    private int nValue;
    private int numStates; // = number of cells on the board
    private double rValue;

    public static void main(String[] args) {
        System.out.println("Welcome to the minimum energy hiking path learner, written by A. Emre Unal.");
        Controller controller = new Controller();
        controller.start();
    }

    public Controller() {
        requestParameters();
        initDataStructures();
    }

    private void requestParameters() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input the 'N' value (the maze size will be 'N'-by-'N'): ");
        nValue = scanner.nextInt();
        numStates = (int) Math.pow(nValue, 2);
        System.out.print("Please input the 'r' value ('r'% of the maze will have hills): ");
        rValue = scanner.nextDouble();
    }

    private void initDataStructures() {
        qMatrix = new QMatrix(nValue);
        maze = new Maze(nValue, rValue, qMatrix);
        window = new MapWindow(nValue, maze, qMatrix);
    }

    @Override
    public void run() {
        Agent agent = new Agent(nValue, qMatrix);
        while (true) {
            if(agent.getCurrentState() == numStates) {
                // Reached the end
                agent = new Agent(nValue, qMatrix);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.err.println("Main thread sleep is interrupted!\nExiting.");
                    System.exit(-1);
                }
            }

            Action selectedAction = agent.selectAction();
            agent.executeAction(selectedAction);
            double reward = qMatrix.getReward(agent.getPreviousState(), selectedAction);
            reward += DISCOUNT_FACTOR * qMatrix.getReward(agent.getCurrentState(), agent.pickMostRewardingAction());
            qMatrix.setReward(agent.getPreviousState(), selectedAction, reward);
            window.update(agent.getCurrentState());
            /*
             * - Select action a and execute it (Max reward action at that state)
             * - Receive immediate reward r
             * - Observe new state s'
             * - Update table entry for Q(s, a) as either of:
             *     Q(s, a) = r(s, a) + y * maxQ(s', a')
             *     Q(s, a) = B*[r(s, a) + y * maxQ(s', a') - Q(s, a)]
             * - Move: record transition from s to s'
             */
            try {
                TimeUnit.MILLISECONDS.sleep(THREAD_SLEEP_TIME_MILLIS);
            } catch (InterruptedException e) {
                System.err.println("Main thread sleep is interrupted!\nExiting.");
                System.exit(-1);
            }
        }
    }
}
