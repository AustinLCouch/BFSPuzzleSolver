package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The main Water program that takes command
 * line args to create an initial config and
 * solve it.
 *
 * @author Hritik "Ricky" Gupta
 */
public class Water {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Water amount bucket1 bucket2 ..."));
        }

        Solver solver = new Solver();
        List<Configuration> path = new LinkedList<>();

        ArrayList<Integer> totalCapacities = new ArrayList<>();
        ArrayList<Integer> currentCapacities = new ArrayList<>();

        for (int i = 1; i < args.length; ++i) {
            totalCapacities.add(Integer.parseInt(args[i]));
            currentCapacities.add(0);
        }

        WaterConfig config = new WaterConfig(
                totalCapacities,
                currentCapacities,
                Integer.parseInt(args[0])
        );

        Optional<List<Configuration>> solved = solver.solve(config);

        if (solved.isPresent()) {
            path = solved.get();
        }

        System.out.println("Amount: " + args [0] + ", Buckets: " + totalCapacities);
        solver.displaySolution(path);
    }
}
