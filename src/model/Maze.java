package model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import global.TerrainType;

import java.util.Random;

public class Maze {
    private TerrainType[][] maze;

    private final int nValue;
    private final int numHills;

    public Maze(int nValue, double rValue) {
        this.nValue = nValue;
        this.numHills = (int) (Math.pow(nValue, 2) * rValue);
        this.maze = new TerrainType[nValue][nValue];
        initMaze();
    }

    private void initMaze() {
        placeRandomHills();

        // Fill the rest of the maze with ground tiles
        for (int xCoor = 0; xCoor < nValue; xCoor++) {
            for (int yCoor = 0; yCoor < nValue; yCoor++) {
                if (getTerrainType(xCoor, yCoor) != TerrainType.HILL) {
                    setTerrainType(xCoor, yCoor, TerrainType.GROUND);
                }
            }
        }

    }

    private void placeRandomHills() {
        Random randomGen = new Random();
        int xCoor;
        int yCoor;

        // Place random hills on the maze
        for (int i = 0; i < numHills; i++) {
            // Find a tile that isn't a hill
            do {
                xCoor = randomGen.nextInt(nValue);
                yCoor = randomGen.nextInt(nValue);
            } while (getTerrainType(xCoor, yCoor) == TerrainType.HILL);
            setTerrainType(xCoor, yCoor, TerrainType.HILL);
        }
    }

    public TerrainType getTerrainType(int xCoor, int yCoor) {
        return maze[xCoor][yCoor];
    }

    public void setTerrainType(int xCoor, int yCoor, TerrainType type) {
        maze[xCoor][yCoor] = type;
    }
}
