package puzzles.jam.model;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;
import puzzles.jam.solver.Jam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a configuration of the Jam game.
 *
 * @author Austin Couch
 */
public class JamConfig implements Configuration {

    static final char BLANK = '.';
    /** number of rows in the board */
    private static int numRows;
    /** number of columns in the board */
    private static int numCols;
    /** 2D to represent the gameboard */
    private char[][] board;
    /** an arraylist containing all of the named cars to iterate through */
    private Car[] cars;
    /** a board configuration with that solves the puzzle */
    private char[][] goal;

    /**
     * Creates the initial config for a Jam puzzle
     *
     * @param fileName file to be read that contains game data
     * @throws IOException if error occurs while reading from the file
     */
    public JamConfig(String fileName) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String[] fields = in.readLine().split("\\s+");

        numRows = Integer.parseInt(fields[0]);
        numCols = Integer.parseInt(fields[1]);
        this.board = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numCols; j++){
                this.board[i][j] = BLANK;
            }
        }

        int numCars = Integer.parseInt(in.readLine());
        this.cars = new Car[numCars];
        for (int i = 0; i < numCars; i++){
            String[] carData = in.readLine().split("\\s+");
            this.cars[i] = new Car(carData[0].charAt(0), Integer.parseInt(carData[1]),
                    Integer.parseInt(carData[2]), Integer.parseInt(carData[3]), Integer.parseInt(carData[4]));
        }
        in.close();
        this.reorganizeBoard();
    }

    /**
     * Copy constructor for JamConfig
     * @param other JamConfig to be copied to this config
     */
    public JamConfig(JamConfig other, Car[] cars){
        this.board = new char[numRows][numCols];
        this.cars = new Car[cars.length];
        for (int i = 0; i < cars.length; i++){
            this.cars[i] = new Car(cars[i].getName(), cars[i].getStartRow(), cars[i].getStartCol(),
                    cars[i].getEndRow(), cars[i].getEndCol());
        }

        for(int row = 0; row < numRows; ++row) {
            System.arraycopy(other.board[row], 0, this.board[row], 0, numCols);
        }
        this.reorganizeBoard();
    }

    /**
     * Helper function to reorganize board representation of cars based on their start and end points
     */
    public void reorganizeBoard(){
        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numCols; j++){
                this.board[i][j] = BLANK;
            }
        }
        for (Car car : this.cars) {
            if (car.isHorizontal()) {
                int startRow = car.getStartRow();
                int startCol = car.getStartCol();
                int endCol = car.getEndCol();

                for (int i = startCol; i <= endCol; i++) {
                    this.board[startRow][i] = car.getName();
                }
            } else {
                int startCol = car.getStartCol();
                int startRow = car.getStartRow();
                int endRow = car.getEndRow();

                for (int i = startRow; i <= endRow; i++) {
                    this.board[i][startCol] = car.getName();
                }
            }
        }
    }


    /**
     * Checks if the current config is the solution to the puzzle.
     *
     * @return true if it is the solution; false otherwise
     */
    @Override
    public boolean isSolution() {
        boolean result = false;
        int col = numCols - 1;
        for (int i = 0; i < numRows; i++){
            if (this.board[i][col] == 'X'){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Generates a collection of possible successors given a
     * current config of a puzzle.
     *
     * @return collection of configs
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        ArrayList<Configuration> successors = new ArrayList<>();

        for (int i = 0; i < this.cars.length; i++){
            JamConfig copy1 = new JamConfig(this, this.cars);
            JamConfig copy2 = new JamConfig(this, this.cars);
            //JamConfig comparisonCopy = new JamConfig(this, this.cars);
            if (this.cars[i].isHorizontal()){
                if (this.cars[i].getStartCol() != 0
                        && this.board[this.cars[i].getStartRow()][this.cars[i].getStartCol() - 1] == BLANK){

                    copy1.cars[i].setStartCol(copy1.cars[i].getStartCol() - 1);
                    copy1.cars[i].setEndCol(copy1.cars[i].getEndCol() - 1);
                    copy1.reorganizeBoard();
                    successors.add(copy1);
                }
                if (this.cars[i].getEndCol() != numCols - 1
                        && this.board[this.cars[i].getStartRow()][this.cars[i].getEndCol() + 1] == BLANK) {

                    copy2.cars[i].setStartCol(copy2.cars[i].getStartCol() + 1);
                    copy2.cars[i].setEndCol(copy2.cars[i].getEndCol() + 1);
                    copy2.reorganizeBoard();
                    successors.add(copy2);
                }
            }
            else{
                if (this.cars[i].getStartRow() != 0 &&
                        this.board[this.cars[i].getStartRow() - 1][this.cars[i].getStartCol()] == BLANK){
                    copy1.cars[i].setStartRow(copy1.cars[i].getStartRow() - 1);
                    copy1.cars[i].setEndRow(copy1.cars[i].getEndRow() - 1);
                    copy1.reorganizeBoard();
                    successors.add(copy1);
                }
                if (this.cars[i].getEndRow() != numRows - 1 &&
                        this.board[this.cars[i].getEndRow() + 1][this.cars[i].getStartCol()] == BLANK){
                    copy2.cars[i].setStartRow(copy2.cars[i].getStartRow() + 1);
                    copy2.cars[i].setEndRow(copy2.cars[i].getEndRow() + 1);
                    copy2.reorganizeBoard();
                    successors.add(copy2);
                }
            }
        }
        return successors;
    }

    /**
     * Gets the ultimate goal config.
     *
     * @return Configuration that represents the goal
     */
    @Override
    public Configuration getGoalConfig() {return null;}

    @Override
    public boolean equals(Object o){
        boolean result = false;
        if (o instanceof JamConfig){
            JamConfig j = (JamConfig) o;
            result = Arrays.deepEquals(this.board, j.board);
        }
        return result;
    }

    @Override
    public int hashCode(){return Arrays.deepHashCode(this.board);}

    /**
     * @return the current state of the configuration as a string
     */
    @Override
    public String display() {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                output.append(this.board[i][j]);
                output.append("\s");
            }
            output.append("\n");
        }
        return output.toString();
    }

    /** public accessor method to return cars for use in JamModel */
    public Car[] getCars(){
        return this.cars;
    }

    /** public accessor method to return board for use in JamModel */
    public char[][] getBoard(){
        return this.board;
    }

    /** public accessor method to return the number of rows for use in JamModel */
    public int getNumRows(){
        return numRows;
    }

    /** public accessor method to return the number of columns for use in JamModel */
    public int getNumCols(){
        return numCols;
    }
}
