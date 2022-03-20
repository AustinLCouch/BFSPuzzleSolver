package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a configuration of the Hoppers puzzle
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */

public class HoppersConfig implements Configuration {

    /** character to represent a green frog */
    public static final char GREEN_FROG = 'G';
    /** character to represent a red frog */
    public static final char RED_FROG = 'R';
    /** character to represent an empty but valid space */
    public static final char EMPTY = '.';
    /** character to represent an invalid space */
    public static final char INVALID = '*';

    /** number of rows of board */
    public static int numRows;
    /** number of columns of board */
    public static int numCols;
    /** 2D array representing board state */
    private char[][] board;

    /**
     * Creates the initial config of a Hoppers puzzle.
     *
     * @param filename file to be read that contains game data
     * @throws IOException if error occurs whilst reading from file
     */
    public HoppersConfig(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));

        String[] fields = in.readLine().split("\\s+");
        numRows = Integer.parseInt(fields[0]);
        numCols = Integer.parseInt(fields[1]);

        this.board = new char[numRows][numCols];

        for (int row = 0; row < numRows; ++row) {
            fields = in.readLine().split("\\s+");
            for (int col = 0; col < numCols; ++col) {
                this.board[row][col] = fields[col].charAt(0);
            }
        }

        in.close();
    }

    /**
     * Copy constructor for HoppersConfig.
     *
     * @param other HoppersConfig to be copied into this
     *              config.
     */
    public HoppersConfig(HoppersConfig other) {
        this.board = new char[numRows][numCols];
        for(int row = 0; row < numRows; ++row) {
            System.arraycopy(other.board[row], 0, this.board[row], 0, numCols);
        }
    }

    /**
     * @return 2D array of current board state
     */
    public char[][] getBoard() {
        return this.board;
    }

    @Override
    public boolean isSolution() {
        int totalFrogs = 0;
        char finalFrog = GREEN_FROG;
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                char currentSpace = this.board[row][col];
                if (currentSpace == INVALID) {
                    continue;
                }
                if (currentSpace == RED_FROG ||
                        currentSpace == GREEN_FROG) {
                    totalFrogs += 1;
                    finalFrog = currentSpace;
                }
            }
        }
        return totalFrogs == 1 && finalFrog == RED_FROG;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        ArrayList<Configuration> successors = new ArrayList<>();
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                char currentSpace = this.board[row][col];
                if (!isFrog(currentSpace)) {
                    continue;
                }
                for (Integer[] coordinates : getMovableSpaces(row, col)) {
                    HoppersConfig successor = new HoppersConfig(this);
                    switch (currentSpace) {
                        case RED_FROG -> successor.board[coordinates[0]][coordinates[1]] = RED_FROG;
                        case GREEN_FROG -> successor.board[coordinates[0]][coordinates[1]] = GREEN_FROG;
                    }
                    successor.board[coordinates[2]][coordinates[3]] = EMPTY;
                    successor.board[row][col] = EMPTY;
                    successors.add(successor);
                }
            }
        }
        return successors;
    }

    /**
     * Checks if a space is a frog or not.
     *
     * @param space board space being analyzed.
     * @return true if char is a red or green frog, false otherwise
     */
    public boolean isFrog(char space) {
        return space == RED_FROG || space == GREEN_FROG;
    }

    /**
     * Generates an ArrayList of row, column pairs to be moved to,
     * based on a given position on the board.
     *
     * @param row current row position of frog
     * @param col current col position of frog
     * @return ArrayList of coordinates frog can be moved to
     */
    public ArrayList<Integer[]> getMovableSpaces(int row, int col) {
        //first two values in array are new coordinates of jumping frog
        //second two values in array are coordinates of frog to be removed
        ArrayList<Integer[]> movableSpaces = new ArrayList<>();

        if (row - 2 >= 0) {
            if (col - 2 >= 0) {
                if (this.board[row - 1][col - 1] == GREEN_FROG && this.board[row - 2][col - 2] == EMPTY) {
                    movableSpaces.add(new Integer[]{row - 2, col - 2, row - 1, col - 1});
                }
            }
            if (col + 2 <= numCols - 1) {
                if (this.board[row - 1][col + 1] == GREEN_FROG && this.board[row - 2][col + 2] == EMPTY) {
                    movableSpaces.add(new Integer[]{row - 2, col + 2, row - 1, col + 1});
                }
            }
        }
        if (row + 2 <= numRows - 1) {
            if (col - 2 >= 0) {
                if (this.board[row + 1][col - 1] == GREEN_FROG && this.board[row + 2][col - 2] == EMPTY) {
                    movableSpaces.add(new Integer[]{row + 2, col - 2, row + 1, col - 1});
                }
            }
            if (col + 2 <= numCols - 1) {
                if (this.board[row + 1][col + 1] == GREEN_FROG && this.board[row + 2][col + 2] == EMPTY) {
                    movableSpaces.add(new Integer[]{row + 2, col + 2, row + 1, col + 1});
                }
            }
        }

        if (row % 2 == 0 && col % 2 == 0) {
            if (row - 4 >= 0) {
                if (this.board[row - 2][col] == GREEN_FROG  && this.board[row - 4][col] == EMPTY) {
                    movableSpaces.add(new Integer[]{row - 4, col, row - 2, col});
                }
            }
            if (row + 4 <= numRows - 1) {
                if (this.board[row + 2][col] == GREEN_FROG && this.board[row + 4][col] == EMPTY) {
                    movableSpaces.add(new Integer[]{row + 4, col, row + 2, col});
                }
            }
            if (col - 4 >= 0) {
                if (this.board[row][col - 2] == GREEN_FROG && this.board[row][col - 4] == EMPTY) {
                    movableSpaces.add(new Integer[]{row, col - 4, row, col - 2});
                }
            }
            if (col + 4 <= numCols - 1) {
                if (this.board[row][col + 2] == GREEN_FROG && this.board[row][col + 4] == EMPTY) {
                    movableSpaces.add(new Integer[]{row, col + 4, row, col + 2});
                }
            }
        }

        return movableSpaces;
    }

    @Override
    public String display() {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                output.append(this.board[row][col]);
                output.append("\s");
            }
            output.append("\n");
        }
        //output.deleteCharAt(output.length() - 1);
        return output.toString();
    }

    @Override
    public Configuration getGoalConfig() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof HoppersConfig) {
            HoppersConfig h = (HoppersConfig) o;
            result = Arrays.deepEquals(this.board, h.board);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
