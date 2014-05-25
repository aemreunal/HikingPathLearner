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

/*
 * The main algorithm is as follows:
 * 1) Select action a and execute it
 * 2) Receive immediate reward r
 * 3) Observe new state s'
 * 4) Update table entry for Q(s, a) as either of:
 *     Q(s, a) = r(s, a) + y * maxQ(s', a')
 * 5) Move: record transition from s to s'
 */

public class Controller extends Thread implements Runnable {
    private static final double DISCOUNT_FACTOR = 0.8;
    public static final double AGENT_RANDOM_MOVE_CHANCE = 0.3;

    private static final double UPDATE_FREQUENCY_HZ = 5;
    private static final long ONE_SECOND_IN_MILLIS = 1000;
    private static final long THREAD_SLEEP_TIME_MILLIS = (long) (ONE_SECOND_IN_MILLIS / UPDATE_FREQUENCY_HZ);

    private QMatrix qMatrix;
    private Maze maze;
    private MapWindow window;
    private Agent agent;

    private int nValue;
    private int numStates; // = number of cells on the board
    private double rValue;

    public static void main(String[] args) {
        printProgramInfo();
        Controller controller = new Controller();
        controller.start();
    }

    private static void printProgramInfo() {
        System.out.println("*********************************************************");
        System.out.println("* Welcome to the minimum energy hiking path learner.");
        System.out.println("* Written by A. Emre Unal (emre.unal@ozu.edu.tr).");
        System.out.println("*");
        System.out.println("* Discount factor = " + DISCOUNT_FACTOR);
        System.out.println("* Agent random move chance = " + AGENT_RANDOM_MOVE_CHANCE);
        System.out.println("* (The update speed, discount factor and agent random");
        System.out.println("* move chance can be changed from the Controller.java");
        System.out.println("* file fields.)");
        System.out.println("*********************************************************");
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
        rValue = scanner.nextDouble() / 100;
    }

    private void initDataStructures() {
        qMatrix = new QMatrix(nValue);
        maze = new Maze(nValue, rValue, qMatrix);
        window = new MapWindow(nValue, maze, qMatrix);
    }

    @Override
    public void run() {
        agent = new Agent(nValue, qMatrix);
        while (true) {
            if(agent.getCurrentState() == numStates) {
                // Reached the end, create a new agent
                restartAgent();
            }

            // Select action
            Action selectedAction = agent.selectAction();

            // Execute action to get from state s to state s'
            agent.executeAction(selectedAction);

            // Receive immediate reward r(s, a)
            double immediateReward = qMatrix.getImmediateReward(agent.getPreviousState(), selectedAction);

            // Update table entry for Q(s, a) as: Q(s, a) = r(s, a) + y * maxQ(s', a')
            double newQValue = immediateReward + DISCOUNT_FACTOR * qMatrix.getQValue(agent.getCurrentState(), agent.pickMaxQValueAction());
            qMatrix.setQValue(agent.getPreviousState(), selectedAction, newQValue);

            // Update window to reflect state change
            window.update(agent.getCurrentState());

            pauseThread(THREAD_SLEEP_TIME_MILLIS);
        }
    }

    private void restartAgent() {
        agent = new Agent(nValue, qMatrix);

        pauseThread(ONE_SECOND_IN_MILLIS / 2);

        // Update window to reflect the new agent
        window.update(agent.getCurrentState());

        pauseThread(ONE_SECOND_IN_MILLIS / 2);
    }

    private void pauseThread(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println("Main thread sleep is interrupted!\nExiting.");
            System.exit(-1);
        }
    }
}
