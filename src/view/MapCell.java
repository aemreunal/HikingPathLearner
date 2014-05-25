package view;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import model.Action;
import model.TerrainType;
import model.Maze;
import model.QMatrix;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class MapCell extends JPanel {
    public static final int CELL_WIDTH = 80;
    public static final int CELL_HEIGHT = 80;
    private static final int BORDER_THICKNESS = 1;
    private static final int GRID_HORIZONTAL_GAP = 10;
    private static final int GRID_VERTICAL_GAP = 2;

    private final QMatrix qMatrix;

    private final int state;
    private JLabel stateLabel;
    private JLabel rightRewardLabel;
    private JLabel leftRewardLabel;
    private JLabel upRewardLabel;
    private JLabel downRewardLabel;

    private DecimalFormat numFormat;

    public MapCell(int state, Maze maze, QMatrix qMatrix) {
        this.qMatrix = qMatrix;
        this.state = state;
        this.numFormat = new DecimalFormat("#.####");
        setLayout(new GridLayout(5, 1, GRID_HORIZONTAL_GAP, GRID_VERTICAL_GAP));
        setBorder(new LineBorder(Color.BLACK, BORDER_THICKNESS));
        initBackgroundColor(maze.getTerrainType(state));
        initLabels();
        update(7);
    }

    private void initBackgroundColor(TerrainType type) {
        if (type == TerrainType.GROUND) {
            setBackground(Color.WHITE);
        } else {
            setBackground(new Color(204, 120, 50));
        }
    }

    private void initLabels() {
        stateLabel = new JLabel("S: " + state);
        this.add(stateLabel);
        rightRewardLabel = new JLabel("R: ");
        this.add(rightRewardLabel);
        leftRewardLabel = new JLabel("L: ");
        this.add(leftRewardLabel);
        upRewardLabel = new JLabel("U: ");
        this.add(upRewardLabel);
        downRewardLabel = new JLabel("D: ");
        this.add(downRewardLabel);
    }

    public void update(int currStateOfAgent) {
        updateLabel(rightRewardLabel, Action.RIGHT);
        updateLabel(leftRewardLabel, Action.LEFT);
        updateLabel(upRewardLabel, Action.UP);
        updateLabel(downRewardLabel, Action.DOWN);

        if (currStateOfAgent == state) {
            // Agent is in this state/cell
            setBorder(new LineBorder(Color.RED, BORDER_THICKNESS * 3));
        } else {
            // Agent is in another state/cell
            setBorder(new LineBorder(Color.BLACK, BORDER_THICKNESS));
        }
    }

    public void updateLabel(JLabel label, Action action) {
        double reward = qMatrix.getReward(state, action);
        if (reward == -Double.MAX_VALUE) {
            label.setText(action.text + "-Inf");
        } else {
            label.setText(action.text + numFormat.format(qMatrix.getReward(state, action)));
        }
    }
}
