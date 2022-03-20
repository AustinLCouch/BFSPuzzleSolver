package puzzles.common.solver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single config of the Puzzle.
 * Solver relies on these methods being present within a
 * given puzzle, so all configs of any puzzle must implement
 * this interface.
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public interface Configuration {
    /**
     * Checks if the current config is the solution to the puzzle.
     *
     * @return true if it is the solution; false otherwise
     */
    boolean isSolution();

    /**
     * Generates a collection of possible successors given a
     * current config of a puzzle.
     *
     * @return collection of configs
     */
    Collection<Configuration> getSuccessors();

    /**
     * @return the current state of the configuration as a string
     */
    String display();

    /**
     * Gets the ultimate goal config.
     *
     * @return Configuration that represents the goal
     */
    Configuration getGoalConfig();
}
