package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.solver.Jam;

import java.io.IOException;
import java.util.*;

/**
 * The model in the MVC design pattern for the Jam puzzle.
 *
 * @author Austin Couch
 */
public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, JamClientData>> observers = new LinkedList<>();

    /** the initial configuration from loading */
    private JamConfig initialConfig;
    /** the current configuration */
    private JamConfig currentConfig;
    /** data to be passed to the view */
    private JamClientData data;

    /** Creates model with the initial config unmodified from loading and creates a
     * current configuration to be modified later
     *
     * @param config current config to be modified later
     */
    public JamModel(JamConfig config){
        this.initialConfig = config;
        this.currentConfig = config;
    }

    /**
     * @return data and message to be sent to the view and displayed for the user
     */
    public JamClientData getData(){
        return this.data;
    }

    /**
     * @return the current configuration
     */
    public JamConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /** If a solution can be found, advance the puzzle by one move and display so
     *  If no solution, display so
     */
    public void hint(){
        Solver solver = new Solver();
        List<Configuration> path = new ArrayList<>();
        Optional<List<Configuration>> solved = solver.solve(this.currentConfig);

        if (solved.isPresent()){
            path = solved.get();
        }
        else{
            sendDataAndAlert("No solution for this puzzle\n");
            return;
        }

        if(path.size() > 1){
            this.currentConfig = (JamConfig) path.get(1);
            sendDataAndAlert("Performed next step\n");
        }
        else{
            sendDataAndAlert("Puzzle already solved\n");
        }
    }

    /**
     * Loads a Jam puzzle file
     *
     * @param filename name of the puzzle file
     */
    public void load(String filename){
        try{
            JamConfig config = new JamConfig(filename);
            this.initialConfig = config;
            this.currentConfig = config;
            sendDataAndAlert("Loaded " + filename + "\n");
        }
        catch (IOException e){
            sendDataAndAlert("Invalid filename\n");
        }
    }

    /**
     * Moves a selected car to a destination if possible
     *
     * @param startRow starting row of car
     * @param startCol starting col of car
     * @param destRow destination row of car
     * @param destCol destination column of car
     */
    public void select(int startRow, int startCol, int destRow, int destCol){
        JamConfig copyConfig = new JamConfig(this.currentConfig, this.currentConfig.getCars());
        //copyConfig.reorganizeBoard();
        String direction = "";
        char currentSpace = '\u0000';

        if (!isOpen(copyConfig, startRow, startCol)
                && 0 <= startRow && startRow < copyConfig.getNumRows()
                && 0 <= startCol && startCol < copyConfig.getNumCols()){
            currentSpace = copyConfig.getBoard()[startRow][startCol];
        }
        else{
            sendDataAndAlert("Invalid starting selection\n");
        }

        Car selectedCar = null;
        for (Car car : copyConfig.getCars()){
            if (currentSpace == car.getName()){
                selectedCar = car;
            }
        }
        if (selectedCar == null){
            sendDataAndAlert("Invalid starting selection\n");
            return;
        }

        if (isOpen(copyConfig, destRow, destCol)){
            if (selectedCar.isHorizontal()){
                if (destCol == selectedCar.getStartCol() - 1){
                    selectedCar.setStartCol(destCol);
                    selectedCar.setEndCol(selectedCar.getEndCol() - 1);
                    direction = "left";
                }
                else if (destCol == selectedCar.getEndCol() + 1){
                    selectedCar.setEndCol(destCol);
                    selectedCar.setStartCol(selectedCar.getStartCol() + 1);
                    direction = "right";
                }
                else{
                    sendDataAndAlert("Invalid destination selection\n");
                    return;
                }
            }
            else{
                if (destRow == selectedCar.getStartRow() - 1){
                    selectedCar.setStartRow(destRow);
                    selectedCar.setEndRow(selectedCar.getEndRow() - 1);
                    direction = "up";
                }
                else if (destRow == selectedCar.getEndRow() + 1){
                    selectedCar.setEndRow(destRow);
                    selectedCar.setStartRow(selectedCar.getStartRow() + 1);
                    direction = "down";
                }
                else{
                    sendDataAndAlert("Invalid destination selection\n");
                    return;
                }
            }
            copyConfig.reorganizeBoard();
            this.currentConfig = copyConfig;
            sendDataAndAlert("Move made: Car " + selectedCar.getName() + " -> " + direction + " one space\n" );
        }
        else{
            sendDataAndAlert("Invalid destination selection\n");
        }
    }

    /**
     * Determines if the current space on the board has a car in it or not and if it is within the board's bounds
     *
     * @param config the configuration to check the cell in
     * @param row board row to check
     * @param col board column to check
     * @return true if there is a car in the given cell, false otherwise
     */
    public boolean isOpen(JamConfig config, int row, int col){
        boolean result = false;
        if (0 <= row && row < config.getNumRows()
                && 0 <= col && col < config.getNumCols()
                && config.getBoard()[row][col] == JamConfig.BLANK){
            result = true;
        }
        return result;
    }

    /**
     * Reloads the initial configuration, resetting the puzzle
     */
    public void reset(){
        this.currentConfig = this.initialConfig;
        sendDataAndAlert("Board reset\n");
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, JamClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(JamClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Sets this.data to a new JamClientData with its message
     *
     * @param message string to be sent to the JamClientData / view
     */
    private void sendDataAndAlert(String message){
        this.data = new JamClientData(message);
        alertObservers(this.data);
    }
}
