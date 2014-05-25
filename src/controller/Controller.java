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
    private static final double DISCOUNT_FACTOR = 0.8;
    public static final double AGENT_RANDOM_MOVE_CHANCE = 0.3;

    private static final double UPDATE_FREQUENCY_HZ = 5;
    private static final long ONE_SECOND_IN_MILLIS = 1000;
    private static final long THREAD_SLEEP_TIME_MILLIS = (long) (ONE_SECOND_IN_MILLIS / UPDATE_FREQUENCY_HZ);

    private QMatrix qMatrix;
    private Maze maze;
    private MapWindow window;

    private int nValue;
    private int numStates; // = number of cells on the board
    private double rValue;

    public static void main(String[] args) {
        System.out.println("Welcome to the minimum energy hiking path learner, written by A. Emre Unal.");
        System.out.println("Discount factor = " + DISCOUNT_FACTOR);
        System.out.println("Agent random move chance = " + AGENT_RANDOM_MOVE_CHANCE);
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
        rValue = scanner.nextDouble() / 100;
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
                // Reached the end, create a new agent
                agent = new Agent(nValue, qMatrix);
                pauseThread(ONE_SECOND_IN_MILLIS);
                continue;
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

    private void pauseThread(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println("Main thread sleep is interrupted!\nExiting.");
            System.exit(-1);
        }
    }
}
