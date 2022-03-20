package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.*;

/**
 * The model in the MVC design pattern for the Hoppers puzzle.
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, HoppersClientData>> observers = new LinkedList<>();

    /** the initial config loaded, without any moves made */
    private HoppersConfig initialConfig;

    /** the current configuration */
    private HoppersConfig currentConfig;

    /** data to be passed to the view */
    private HoppersClientData data;

    /**
     * Creates an model with an current config to
     * be changed later, as well as a reference to an
     * unmodified config.
     *
     * @param config current config to be modified later
     */
    public HoppersModel(HoppersConfig config) {
        this.initialConfig = config;
        this.currentConfig = config;
    }

    /**
     * @return data with a message to be shown to the user
     */
    public HoppersClientData getData() {
        return this.data;
    }

    /**
     * @return the current state of the board
     */
    public HoppersConfig getCurrentConfig() {
        return this.currentConfig;
    }

    /**
     * Advances the state of the puzzle by one move, if possible.
     */
    public void hint() {
        Solver solver = new Solver();
        List<Configuration> path;
        Optional<List<Configuration>> solved = solver.solve(this.currentConfig);
        if (solved.isPresent()) {
            path = solved.get();
        } else {
            sendDataAndAlert("No solution for this puzzle\n");
            return;
        }
        if (path.size() > 1) {
            this.currentConfig = (HoppersConfig) path.get(1);
            sendDataAndAlert("Performed next step\n");
        } else {
            sendDataAndAlert("Puzzle already solved\n");
        }
    }

    /**
     * Loads a Hoppers puzzle file.
     *
     * @param filename location and name of puzzle file
     */
    public void load(String filename) {
        try {
            HoppersConfig config = new HoppersConfig(filename);
            this.initialConfig = config;
            this.currentConfig = config;
            sendDataAndAlert("Loaded " + filename + "\n");
        } catch (IOException e) {
            sendDataAndAlert("Invalid filename\n");
        }
    }

    /**
     * Moves a selected frog across the board.
     *
     * @param startRow starting row of jumping frog
     * @param startCol starting col of jumping frog
     * @param destRow destination row of jumping frog
     * @param destCol destination col of jumping frog
     */
    public void select(int startRow, int startCol, int destRow, int destCol) {
        HoppersConfig copyConfig = new HoppersConfig(this.currentConfig);
        char[][] copyBoard = copyConfig.getBoard().clone();
        char currentSpace = copyBoard[startRow][startCol];

        for (Integer[] coordinates : copyConfig.getMovableSpaces(startRow, startCol)) {
            if (!(coordinates[0] == destRow && coordinates[1] == destCol)) {
                continue;
            }
            switch (currentSpace) {
                case HoppersConfig.RED_FROG -> copyBoard[coordinates[0]][coordinates[1]] = HoppersConfig.RED_FROG;
                case HoppersConfig.GREEN_FROG -> copyBoard[coordinates[0]][coordinates[1]] = HoppersConfig.GREEN_FROG;
            }
            copyBoard[coordinates[2]][coordinates[3]] = HoppersConfig.EMPTY;
            copyBoard[startRow][startCol] = HoppersConfig.EMPTY;
            this.currentConfig = copyConfig;
            sendDataAndAlert("Move made: (" + startRow + ", " + startCol + ") -> (" + destRow + ", " + destCol + ")" + "\n");
            return;
        }
        sendDataAndAlert("Invalid destination\n");
    }

    /**
     * @return true if given row and column contains a frog on the current board state; false otherwise
     */
    public boolean isPresent(int row, int col) {
        if (!this.currentConfig.isFrog(this.currentConfig.getBoard()[row][col])) {
            sendDataAndAlert("Space cannot be selected -- no frog present\n");
            return false;
        }
        return true;
    }

    /**
     * Reloads the initial config, which resets puzzle to initial state.
     */
    public void reset() {
        this.currentConfig = this.initialConfig;
        sendDataAndAlert("Board reset\n");
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, HoppersClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(HoppersClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Sets this.data to a new HoppersClientData with a message
     *
     * @param message string to be passed into HoppersClientData
     */
    private void sendDataAndAlert(String message) {
        this.data = new HoppersClientData(message);
        alertObservers(this.data);
    }
}
