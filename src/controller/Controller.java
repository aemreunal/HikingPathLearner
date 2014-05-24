package controller;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import model.Maze;
import model.QMatrix;

import java.util.Scanner;

public class Controller {
    private Scanner scanner;

    private int nValue;
    private int numStates; // = number of cells on the board
    private double rValue;

    private QMatrix qMatrix;
    private Maze maze;

    public static void main(String[] args) {
        System.out.println("Welcome to the minimum energy hiking path learner, written by A. Emre Unal.");
        new Controller();
    }

    public Controller() {
        scanner = new Scanner(System.in);
        requestParameters();
        initSystem();
    }

    private void requestParameters() {
        System.out.print("Please input the 'N' value (the maze size will be 'N'-by-'N'): ");
        nValue = scanner.nextInt();
        numStates = (int) Math.pow(nValue, 2);
        System.out.print("Please input the 'r' value ('r'% of the maze will have hills): ");
        rValue = scanner.nextDouble();
    }

    private void initSystem() {
        qMatrix = new QMatrix(nValue);
        maze = new Maze(nValue, rValue, qMatrix);
    }
}
