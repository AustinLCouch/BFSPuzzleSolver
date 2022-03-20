package puzzles.jam.model;

/**
 * This will represent a car and any of its useful data to be used and iterated over in JamConfig
 *
 * @author Austin Couch
 */
public class Car {
    /** the unique name char of the car */
    private char name;
    /** starting row of the car */
    private int startRow;
    /** starting column of the car */
    private int startCol;
    /** ending row of the car */
    private int endRow;
    /** ending column of the car */
    private int endCol;
    /** boolean to indicate if the car moves horizontal or vertical */
    private boolean isHorizontal;

    /**
     * Creates a car with its name and values, as well as determining if it lays horizontal or not
     * @param name name of the car
     * @param startRow starting row of the car
     * @param startCol starting column of the car
     * @param endRow ending row of the car
     * @param endCol ending column of the car
     */
    public Car(char name, int startRow, int startCol, int endRow, int endCol){
        this.name = name;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;

        this.isHorizontal = this.startRow == this.endRow;
    }

    public boolean isHorizontal(){
        return this.isHorizontal;
    }

    /** getter method for name value */
    public char getName() {
        return this.name;
    }

    /** getter method for starting row value */
    public int getStartRow() {
        return this.startRow;
    }

    /** setter method for starting row */
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    /** getter method for starting column value */
    public int getStartCol() {
        return this.startCol;
    }

    /** setter method for starting column */
    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    /** getter method for ending row value */
    public int getEndRow() {
        return this.endRow;
    }

    /** setter method for ending row */
    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    /** getter method for ending column value */
    public int getEndCol() {
        return this.endCol;
    }

    /** setter method for ending column */
    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }
}
