package puzzles.common.solver;

import java.util.*;

/**
 * Solves a given puzzle using a BFS and backtracking algorithm.
 * Utilizes methods given in the Configuration interface.
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public class Solver {

    /** number of configs generated so far */
    private int numConfigs;
    /** unique number of configs generated so far */
    private int uniqueNumConfigs;

    /**
     * Sets the default values for how many configs have been generated.
     * Both are equal to 1, because there is always 1 config generated --
     * the provided one.
     */
    public Solver() {
        this.numConfigs = 1;
        this.uniqueNumConfigs = 1;
    }

    /**
     * Solves a particular puzzle using the BFS algorithm.
     *
     * @param config the initial config the puzzle begins in
     * @return an Optional containing the list of configs generated to
     * get to solution, or Optional.empty() if no solution exists.
     */
    public Optional<List<Configuration>> solve(Configuration config) {
        List<Configuration> queue = new LinkedList<>();
        Map<Configuration, Configuration> predMap = new HashMap<>();
        List<Configuration> path = new LinkedList<>();
        ArrayList<Configuration> uniqueConfigs = new ArrayList<>();
        Configuration goalConfig = config.getGoalConfig();

        queue.add(config);
        predMap.put(config, null);
        uniqueConfigs.add(config);

        while (!queue.isEmpty()) {
            Configuration currConfig = queue.remove(0);
            if (!uniqueConfigs.contains(currConfig)) {
                ++this.uniqueNumConfigs;
                uniqueConfigs.add(currConfig);
            }
            if (currConfig.isSolution()) {
                goalConfig = currConfig;
                break;
            }
            for (Configuration childConfig : currConfig.getSuccessors()) {
                ++this.numConfigs;
                if (!predMap.containsKey(childConfig)) {
                    queue.add(childConfig);
                    predMap.put(childConfig, currConfig);
                }
            }
        }

        if (predMap.containsKey(goalConfig)) {
            Configuration currConfig = goalConfig;
            while (!currConfig.equals(config)) {
                path.add(0, currConfig);
                currConfig = predMap.get(currConfig);
            }
            path.add(0, config);
        }

        if (path.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(path);
    }

    /**
     * @return number of configurations generated
     */
    public int getNumConfigs() {
        return numConfigs;
    }

    /**
     * @return unique number of configurations generated
     */
    public int getUniqueNumConfigs() {
        return uniqueNumConfigs;
    }

    /**
     * Prints the solution of a given config to standard output
     *
     * @param path constructed from BFS predecessor map
     */
    public void displaySolution(List<Configuration> path) {
        System.out.println("Total configs: " + this.getNumConfigs());
        System.out.println("Unique configs: " + this.getUniqueNumConfigs());
        if (path.isEmpty()) {
            System.out.println("No solution");
        } else {
            for (int i = 0; i < path.size(); ++i) {
                System.out.println("Step " + i + ": " + path.get(i).display());
            }
        }
    }
}
